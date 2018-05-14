import numpy as np
import uuid
import threading
from .detectors import *
from .db.local_db import LocalDB
from .util.serializer import serialize,deserialize
from .util.read_write_lock import  ReadWriteLock

class FaceAPI(object):
    none_find_faceid = "-1"
    def __init__(self,face_rec_threshold=0.7):

        self.face_rec_threshold = face_rec_threshold

        self._embedings = None
        #self.wr_lock = ReadWriteLock()
        self._attr_isloaded = False
        self._det_isloaded = False
        self._previous_box = []
        self._track_increased_id = 0
        self._track_motion_threshold = 0.3
        self._blurry_detector = BlurryDetector()

    def _lazy_load_face_detect_model(self):
        if not self._det_isloaded:
            self._det_isloaded = True
            self._face_detecotr = FaceMTCNNDetector()


    def _lazy_load_face_attr_model(self):
        if not self._attr_isloaded:
            self._attr_isloaded = True
            self.db = LocalDB()
            self._load_face_embeding_fromdb()
            self._age_detector = AgeDetector()
            self._emotion_detector = EmotionDetector()
            self._gender_detector = GenderDetector()
            self._face_rec = FaceRecognition()



    def _load_face_embeding_fromdb(self):
        face_infors = self.db.get_all_face_embeding()
        self.embeding_to_faceid = dict()
        for index,face in enumerate(face_infors):
            array = deserialize(face.embeding)
            if(self._embedings is None):
                self._embedings = array
            else:
                self._embedings = np.concatenate((self._embedings, array), axis=0)
            for i in range( self._embedings.shape[0]- array.shape[0], self._embedings.shape[0]):
                self.embeding_to_faceid[i] = face.faceid
    def _generate_new_faceid(self):
        return str(uuid.uuid1())

    def detect_faces(self,imgs):
        self._lazy_load_face_detect_model()
        img_h, img_w, _ = np.shape(imgs)
        bounding_boxes, _ = self._face_detecotr.detect(imgs)
        bounding_boxes = [self._make_rect_images(box.astype(int), img_h, img_w) for box in bounding_boxes]
        return bounding_boxes

    def track_faces(self,imgs):
        self._lazy_load_face_detect_model()
        img_h, img_w, _ = np.shape(imgs)
        bounding_boxes,_ = self._face_detecotr.detect(imgs)
        #bounding_boxes =  [self._make_rect_images(self.bounding_boxes[index].astype(int),img_h,img_w) for index in range(bounding_boxes.shape[0])]
        bounding_boxes = [self._make_rect_images(box.astype(int), img_h, img_w) for box in bounding_boxes]
        out_box = [[0,box]  for box in bounding_boxes]
        if   len(self._previous_box) > 0 and len(bounding_boxes)>0:
            for (trackid,box) in self._previous_box:
                tmp = np.array([self._check_overlap(box,curbox) for curbox in bounding_boxes])
                index =  np.argmax(tmp)
                if(tmp[index] > 0) and out_box[index][0] == 0 and tmp[index] > self._track_motion_threshold:
                    out_box[index][0] = trackid

        for index,_ in enumerate(out_box):
            if out_box[index][0] == 0:
                out_box[index][0] = self._get_new_track_id()
        self._previous_box = out_box
        return out_box

        # Adjust the rectangle of detected faces

    def _make_rect_images(self, face_position, img_h, img_w):
        left, top, right, bottom = face_position[0], face_position[1], face_position[2], face_position[3]
        width = right - left
        height = bottom - top
        if width > height:
            gap = int((width - height) / 2)
            top = top - gap
            bottom = bottom + gap
        elif height > width:
            gap = int((height - width) / 2)
            left = left - gap
            right = right + gap
        width = right - left
        height = bottom - top
        # left = int(left - 0.2 * width) #max(int(left - 0.2 * width), 0)
        # top =  int(top - 0.2 * height) #max(int(top - 0.2 * height), 0)
        # right = int(right + 0.2 * width) #min(int(right + 0.2 * width), img_w - 1)
        # bottom = int(bottom + 0.2 * height) #min(int(bottom + 0.2 * height), img_h - 1)
        return (left, top, right, bottom)

    def _check_overlap(self, box1, box2):
        dx = min(box1[2], box2[2]) - max(box1[0], box2[0])
        dy = min(box1[3], box2[3]) - max(box1[1], box2[1])

        if (dx >= 0) and (dy >= 0):
            area1 = (box1[3] - box1[1]) * (box1[2] - box1[0])
            area2 = (box2[3] - box2[1]) * (box2[2] - box2[0])
            overlap = (dx * dy)
            return overlap / (area1 + area2 - overlap)
        return 0

    def _get_new_track_id(self):
        self._track_increased_id += 1
        return self._track_increased_id

    def detect_gender(self, imgs):
        self._lazy_load_face_attr_model()
        return self._gender_detector.detect(imgs)

    def detect_age(self,imgs):
        self._lazy_load_face_attr_model()
        return self._age_detector.detect(imgs)

    def detect_emotion(self,imgs):
        self._lazy_load_face_attr_model()
        return self._emotion_detector.detect(imgs)

    def detect_face_id_embeding(self,imgs):
        self._lazy_load_face_attr_model()
        face_embedings = self._face_rec.detect(imgs)
        out_face_id =[]

        #self.wr_lock.acquire_read()
        if self._embedings is None:
            return [(self.none_find_faceid,t) for t in face_embedings]

        for single in face_embedings:
            #Distance between emebding
            z2 = np.sqrt(np.sum(np.square(np.subtract(self._embedings, single)), 1))
            max_index = np.argmin(z2)
            if z2[max_index] < self.face_rec_threshold:
                out_face_id.append((self.embeding_to_faceid[max_index],single))
            else:
                out_face_id.append((self.none_find_faceid,single))
        #self.wr_lock.release_read()
        return out_face_id

    def detect_face_is_blurry(self,img):
        return self._blurry_detector.check_is_blurry(img)

    def register_new_face(self,embeding):
        #self.wr_lock.acquire_write()
        new_face_id = self._generate_new_faceid()
        embeding = embeding.reshape((1,128))
        if self._embedings is None:
            self._embedings = embeding
        else:
            self._embedings =  np.concatenate((self._embedings,embeding), axis=0)
        self.embeding_to_faceid[self._embedings.shape[0]-1] = new_face_id
        #self.wr_lock.release_write()
        self.db.create_face_embeding(new_face_id,serialize(embeding))
        return new_face_id


    def add_new_face(self,face_id, embeding):
        #self.wr_lock.acquire_write()
        embeding = embeding.reshape((1, 128))
        face_embeding =  self.db.query_face_embeding(face_id)
        array = deserialize(face_embeding)
        new_face_embeding = np.concatenate((array, embeding),axis=0)
        self._embedings = np.concatenate((self._embedings, embeding), axis=0)
        self.embeding_to_faceid[self._embedings.shape[0]-1] = face_id
        self.db.update_face_embeding(face_id,serialize(new_face_embeding))

    def clear_all_data(self):
        self.embeding_to_faceid.clear()
        self._embedings =None
        self.db.delete_all_emebding()