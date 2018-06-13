import datetime
import os
import pylru
from multiprocessing import Queue
from repository.remote_repository import *
from thumb.thumb_center import *
class FaceRecServer():
    def __init__(self,face_api,queue):
        self._api = face_api
        self._queue = queue;
        self.__face_rec_gap = 7  # Only do rec when face leave screen over 7 seconds.
        self._recognized_faces = pylru.lrucache(100)
    def __call__(self):
        print("child process:", os.getpid())
        self._api._lazy_load_face_attr_model()
        while True:
            result = self._queue.get(True)
            #register
            if result[0] == 1:
                (type, rec_face_images, customer_infor) = result
                if len(rec_face_images) > 0:
                    result = self._api.detect_face_id_embeding(rec_face_images)
                    self.register_customer(result,customer_infor)
            else:
                (type, rec_face_images, focus_index, is_force, customer_id) = result
                if len(rec_face_images) > 0:
                    result = self._api.detect_face_id_embeding(rec_face_images)

                    # record the visit information
                    age_result = self._api.detect_age(rec_face_images)
                    gender_result = self._api.detect_gender(rec_face_images)
                    emotion_result = self._api.detect_emotion(rec_face_images)
                    current = datetime.datetime.now()
                    for index, (faceid, embeding) in enumerate(result):
                        need_record = True
                        if faceid == self._api.none_find_faceid:
                            faceid = self._api.register_new_face(embeding)
                        else:
                            if faceid in self._recognized_faces:
                                if (current - self._recognized_faces[faceid]).seconds < self.__face_rec_gap:
                                    need_record = False

                        if need_record or index == focus_index:
                            pic_id = save_thumb(cv2.cvtColor(rec_face_images[index], cv2.COLOR_RGB2BGR))
                            if index == focus_index:
                                if is_force:
                                    async_focus_customer_with_record(customer_id, age_result[index], 1, 70,
                                                                     emotion_result[index],
                                                                     pic_id,
                                                                     current if need_record else self._recognized_faces[
                                                                         faceid])
                                else:
                                    async_focus_customer_with_record(faceid, age_result[index], gender_result[index],
                                                                     70, emotion_result[index], pic_id,
                                                                     current if need_record else self._recognized_faces[
                                                                         faceid])
                            else:
                                async_add_visisit_record(faceid, age_result[index], gender_result[index], 70,
                                                         emotion_result[index], pic_id, current)
                        self._recognized_faces[faceid] = current



    def register_customer(self,result, infor):
        for index, (faceid, embeding) in enumerate(result):
            if faceid == self._api.none_find_faceid:
                faceid = self._api.register_new_face(embeding)
            async_add_customer(faceid, infor[0],infor[1],infor[2],infor[3])
