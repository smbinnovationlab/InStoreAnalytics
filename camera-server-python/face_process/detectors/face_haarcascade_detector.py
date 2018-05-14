import cv2
import os
import numpy as np
class FaceHaarcascadeDetector(object):
    def __init__(self):
        self._face_detector_model_path = os.path.dirname(os.path.realpath(__file__)) + '/../models/haarcascade_frontalface_default.xml'
        self._face_detector = cv2.CascadeClassifier(self._face_detector_model_path)
        self._minsize = 50

    def detect(self,img):
        gray = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)
        faces = self._face_detector.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5,minSize=(self._minsize,self._minsize))
        total_boxes = np.empty((0, 4))
        ret = [(x[0],x[1],x[0]+x[2],x[1]+x[3]) for x in faces]
        points = [(x[0],x[1],x[2],x[3]) for x in faces]
        if len(ret) > 0:
            total_boxes = np.concatenate((total_boxes,np.array(ret)),axis=0)
        return  total_boxes,points


