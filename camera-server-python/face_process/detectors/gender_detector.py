from keras.models import load_model
import keras.backend.tensorflow_backend as KTF
import tensorflow as tf
import numpy as np
import cv2
import os


class GenderDetector(object):
    def __init__(self):

        self._gender_model_path = os.path.dirname(
            os.path.realpath(__file__)) + '/../models/gender_mini_XCEPTION.135-0.91.hdf5'
        # config = tf.ConfigProto()
        # config.gpu_options.per_process_gpu_memory_fraction = 0.3
        # config.gpu_options.allow_growth = True
        # set_session(tf.Session(config=config))
        #self._session = tf.Session(
        #    config=tf.ConfigProto(allow_soft_placement=True, log_device_placement=False, device_count={'cpu': 0}))
        #KTF.set_session(self._session)
        self._gender_model = load_model(self._gender_model_path, compile=False)
        self._gender_image_size = self._gender_model.input_shape[1:3]
        self._gender_labels = {0: 'Female', 1: 'Male'}

    def _preprocess_gray_input(self, x, v2=True):
        x = cv2.resize(cv2.cvtColor(x, cv2.COLOR_RGB2GRAY), self._gender_image_size)
        # x = np.expand_dims(x, 0)
        x = np.expand_dims(x, -1)
        x = x.astype('float32')
        x = x / 255.0
        if v2:
            x = x - 0.5
            x = x * 2.0
        return x

    def detect(self, face_images):

        gender_faces = [self._preprocess_gray_input(image, False) for image in face_images]
        gender_faces = np.stack(gender_faces)
        #KTF.set_session(self._session)
        gender_prediction = self._gender_model.predict(gender_faces)
        gender_label_arg = np.argmax(gender_prediction, 1)

        return [int(gender_label_arg[i]) for i, _ in
                enumerate(gender_label_arg)]
