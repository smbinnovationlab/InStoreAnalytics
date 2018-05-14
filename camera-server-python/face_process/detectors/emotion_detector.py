from keras.models import load_model
from keras.backend.tensorflow_backend import set_session
import tensorflow as tf
import numpy as np
import cv2
import os


class EmotionDetector(object):
    def __init__(self):
        self._emotion_model_path = os.path.dirname(
            os.path.realpath(__file__)) + '/../models/fer2013_mini_XCEPTION.102-0.66.hdf5'
        self._model = load_model(self._emotion_model_path, compile=False)
        self._image_size = self._model.input_shape[1:3]
        self._emotion_labels = {0: 'angry', 1: 'disgust', 2: 'fear', 3: 'happy',
                                4: 'sad', 5: 'surprise', 6: 'neutral'}

        #Temp solution for emotion 0: angry, 1:frown, 2:happy, 3:neutral
        self._emotion_mapping_for_client = {0:0,1:1,2:1,3:2,4:1,5:1,6:3}

    def _preprocess_gray_input(self, x, v2=True):
        x = cv2.resize(cv2.cvtColor(x, cv2.COLOR_RGB2GRAY), self._image_size)
        # x = np.expand_dims(x, 0)
        x = np.expand_dims(x, -1)
        x = x.astype('float32')
        x = x / 255.0
        if v2:
            x = x - 0.5
            x = x * 2.0
        return x



    def detect(self, face_images):
        # temp = [cv2.cvtColor(image,cv2.COLOR_BGR2GRAY) for image in face_images]
        emotion_faces = [self._preprocess_gray_input(image, False) for image in face_images]


        emotion_faces = np.stack(emotion_faces)
        emotion_prediction = self._model.predict(emotion_faces)
        emotion_label_arg = np.argmax(emotion_prediction, 1)

        return [self._emotion_mapping_for_client[int(emotion_label_arg[i])] for i, _ in
                enumerate(emotion_label_arg)]

        #return [int(emotion_label_arg[i]) for i, _ in
        #        enumerate(emotion_label_arg)]
