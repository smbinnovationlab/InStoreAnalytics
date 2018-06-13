import datetime
import numpy as np
import pylru
import datetime
from thumb.thumb_center import *
from threading import Thread
from web_socket import  SocketServer
from repository.remote_repository import *
class FrontDoorMonitor(Thread):
    def __init__(self,camera,face_api,queue):
        super(FrontDoorMonitor, self).__init__()
        self._camera = camera
        self._api = face_api
        self._queue = queue
        self._rec_frame_count = 5 #Check face if show in past 5 frames
        self._recognized_faces =pylru.lrucache(100)
        self._is_focused = False
        self._last_focused = None
        self._track_infors = {}
        self._show_inner_vedio = 0
        self._is_manual_mode = False
        self._force_capture =  False
        self._force_capture_user_id = '10000'

        self._start_capture_count = 0

    @staticmethod
    def show_focus_rect(frame, left, top, right, bottom, color):
        ext_size = 15
        thickness = 3
        cv2.line(frame, (left, top), (left + ext_size, top), color, thickness)
        cv2.line(frame, (left, top), (left, top + ext_size), color, thickness)

        cv2.line(frame, (right, top), (right - ext_size, top), color, thickness)
        cv2.line(frame, (right, top), (right, top + ext_size), color, thickness)

        cv2.line(frame, (left, bottom), (left + ext_size, bottom), color, thickness)
        cv2.line(frame, (left, bottom), (left, bottom - ext_size), color, thickness)

        cv2.line(frame, (right, bottom), (right - ext_size, bottom), color, thickness)
        cv2.line(frame, (right, bottom), (right, bottom - ext_size), color, thickness)
    @staticmethod
    def check_over_lap(face_box, rec_box):
        dx = min(face_box[2], rec_box[2]) - max(face_box[0], rec_box[0])
        dy = min(face_box[3], rec_box[3]) - max(face_box[1], rec_box[1])

        if (dx >= 0) and (dy >= 0):
            face_area = (face_box[3] - face_box[1]) * (face_box[2] - face_box[0])
            rect_area = (rec_box[3] - rec_box[1]) * (rec_box[2] - rec_box[0])
            overlap = (dx * dy)
            return max(overlap / face_area,overlap / rect_area)
        return 0
    def run(self):
        count = 0
        while True:
            ret, frame = self._camera.read()
            frame =  cv2.flip(frame,1)
            #gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            rec_img = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

            img_h, img_w, _ = np.shape(rec_img)
            face_infors = self._api.track_faces(rec_img)
            faces_count = len(face_infors)
            rec_face_images =[] # It is RGB style

            red_rec_width = red_rec_height = 200
            red_rec_left = int(img_w / 2 - red_rec_width / 2)
            red_rec_top = int(img_h / 2 - red_rec_height / 2)
            red_rec = (red_rec_left,red_rec_top,red_rec_left+red_rec_width,red_rec_top+red_rec_height)


            focus_face_index = -1
            record_focus = -1
            for (trackid,face_position) in face_infors:
                left, top, right, bottom = face_position[0], face_position[1], face_position[2], face_position[3]
                if left < 0 or top <0 or right > img_w or bottom > img_h:
                    continue
                image = rec_img[top:bottom + 1, left:right+1 , :]
                if not self._api.detect_face_is_blurry(image):
                    if FrontDoorMonitor.check_over_lap(face_position,red_rec) > 0.8:
                        focus_face_index = len(rec_face_images)
                        #Only allow cal red focus after 3 seconds. and record in 20th frame
                        if self._is_manual_mode:
                            if self._force_capture:
                                self._force_capture = False
                                rec_face_images.append(image)
                                print('Record:' + str(datetime.datetime.now()))
                        elif self._is_focused and self._start_capture_count  == 20 and (self._last_focused == None or  (datetime.datetime.now() - self._last_focused).seconds > 4 ):
                            rec_face_images.append(image)
                            self._track_infors[trackid] = self._rec_frame_count
                            self._last_focused = datetime.datetime.now()
                            record_focus = focus_face_index
                            print('Record:' + str(datetime.datetime.now()))

                    if trackid not in self._track_infors:
                        self._track_infors[trackid] = 1
                    else:
                        self._track_infors[trackid] = self._track_infors[trackid] + 1

                    if self._track_infors[trackid] == self._rec_frame_count:
                        if not self._is_manual_mode:
                            rec_face_images.append(image)
                    cv2.rectangle(frame, (left, top), (right, bottom), (0, 255, 0), 2)

            self._is_focused = focus_face_index >= 0
            if not self._is_focused:
                self._start_capture_count = 0
            else:

                self._start_capture_count = self._start_capture_count  + 1
            FrontDoorMonitor.show_focus_rect(frame, red_rec_left, red_rec_top, red_rec_left + red_rec_width,
                                             red_rec_top + red_rec_height, (0, 0, 255) if self._is_focused else (0, 255, 0))



            if len(rec_face_images) > 0:
                self._queue.put((0,rec_face_images,record_focus,self._is_manual_mode,self._force_capture_user_id))


            SocketServer.send(frame)

            cv2.putText(frame, ('Auto' if not self._is_manual_mode else 'Manual'), (0,30),
                        cv2.FONT_HERSHEY_COMPLEX_SMALL, 2, (255, 0, 0),
                        thickness=2, lineType=2)
            cv2.imshow('Video', frame)
            key = cv2.waitKey(1) & 0xFF