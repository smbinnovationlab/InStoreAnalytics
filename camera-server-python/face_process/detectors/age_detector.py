import logging
import sys
import numpy as np
import cv2
import os
import tensorflow as tf
import keras.backend.tensorflow_backend as KTF
from keras.models import Model
from keras.layers import Input, Activation, add, Dense, Flatten, Dropout
from keras.layers.convolutional import Conv2D, AveragePooling2D
from keras.layers.normalization import BatchNormalization
from keras.regularizers import l2
from keras import backend as K


sys.setrecursionlimit(2 ** 20)
np.random.seed(2 ** 10)


class AgeDetector(object):
    def __init__(self):
        self.image_size = 64
        self.agegender_model_path = os.path.dirname(os.path.realpath(__file__))+'/../models/weights.18-4.06.hdf5'
        self._depth = 16
        self._k = 8
        self._dropout_probability = 0
        self._weight_decay = 0.0005
        self._use_bias = False
        self._weight_init = "he_normal"
        self._ages = np.arange(0, 101).reshape(101, 1)
        self._model = None
        if K.image_dim_ordering() == "th":
            logging.debug("image_dim_ordering = 'th'")
            self._channel_axis = 1
            self._input_shape = (3, self.image_size, self.image_size)
        else:
            logging.debug("image_dim_ordering = 'tf'")
            self._channel_axis = -1
            self._input_shape = (self.image_size, self.image_size, 3)
        #self._session =tf.Session(config=tf.ConfigProto(allow_soft_placement=True, log_device_placement=True,device_count={'cpu': 0}))
        #KTF.set_session(self._session)
        self._load_model(self.agegender_model_path)

        # Wide residual network http://arxiv.org/abs/1605.07146

    def _wide_basic(self, n_input_plane, n_output_plane, stride):
        def f(net):
            # format of conv_params:
            #               [ [kernel_size=("kernel width", "kernel height"),
            #               strides="(stride_vertical,stride_horizontal)",
            #               padding="same" or "valid"] ]
            # B(3,3): orignal <<basic>> block
            conv_params = [[3, 3, stride, "same"],
                           [3, 3, (1, 1), "same"]]

            n_bottleneck_plane = n_output_plane

            # Residual block
            for i, v in enumerate(conv_params):
                if i == 0:
                    if n_input_plane != n_output_plane:
                        net = BatchNormalization(axis=self._channel_axis)(net)
                        net = Activation("relu")(net)
                        convs = net
                    else:
                        convs = BatchNormalization(axis=self._channel_axis)(net)
                        convs = Activation("relu")(convs)

                    convs = Conv2D(n_bottleneck_plane, kernel_size=(v[0], v[1]),
                                   strides=v[2],
                                   padding=v[3],
                                   kernel_initializer=self._weight_init,
                                   kernel_regularizer=l2(self._weight_decay),
                                   use_bias=self._use_bias)(convs)
                else:
                    convs = BatchNormalization(axis=self._channel_axis)(convs)
                    convs = Activation("relu")(convs)
                    if self._dropout_probability > 0:
                        convs = Dropout(self._dropout_probability)(convs)
                    convs = Conv2D(n_bottleneck_plane, kernel_size=(v[0], v[1]),
                                   strides=v[2],
                                   padding=v[3],
                                   kernel_initializer=self._weight_init,
                                   kernel_regularizer=l2(self._weight_decay),
                                   use_bias=self._use_bias)(convs)

            # Shortcut Connection: identity function or 1x1 convolutional
            #  (depends on difference between input & output shape - this
            #   corresponds to whether we are using the first block in each
            #   group; see _layer() ).
            if n_input_plane != n_output_plane:
                shortcut = Conv2D(n_output_plane, kernel_size=(1, 1),
                                  strides=stride,
                                  padding="same",
                                  kernel_initializer=self._weight_init,
                                  kernel_regularizer=l2(self._weight_decay),
                                  use_bias=self._use_bias)(net)
            else:
                shortcut = net

            return add([convs, shortcut])

        return f

        # "Stacking Residual Units on the same stage"

    def _layer(self, block, n_input_plane, n_output_plane, count, stride):
        def f(net):
            net = block(n_input_plane, n_output_plane, stride)(net)
            for i in range(2, int(count + 1)):
                net = block(n_output_plane, n_output_plane, stride=(1, 1))(net)
            return net

        return f

        #    def create_model(self):

    def _create_graph(self):
        logging.debug("Creating model...")

        assert ((self._depth - 4) % 6 == 0)
        n = (self._depth - 4) / 6

        inputs = Input(shape=self._input_shape)

        n_stages = [16, 16 * self._k, 32 * self._k, 64 * self._k]

        conv1 = Conv2D(filters=n_stages[0], kernel_size=(3, 3),
                       strides=(1, 1),
                       padding="same",
                       kernel_initializer=self._weight_init,
                       kernel_regularizer=l2(self._weight_decay),
                       use_bias=self._use_bias)(inputs)  # "One conv at the beginning (spatial size: 32x32)"

        # Add wide residual blocks
        block_fn = self._wide_basic
        conv2 = self._layer(block_fn, n_input_plane=n_stages[0], n_output_plane=n_stages[1], count=n, stride=(1, 1))(
            conv1)
        conv3 = self._layer(block_fn, n_input_plane=n_stages[1], n_output_plane=n_stages[2], count=n, stride=(2, 2))(
            conv2)
        conv4 = self._layer(block_fn, n_input_plane=n_stages[2], n_output_plane=n_stages[3], count=n, stride=(2, 2))(
            conv3)
        batch_norm = BatchNormalization(axis=self._channel_axis)(conv4)
        relu = Activation("relu")(batch_norm)

        # Classifier block
        pool = AveragePooling2D(pool_size=(8, 8), strides=(1, 1), padding="same")(relu)
        flatten = Flatten()(pool)
        predictions_g = Dense(units=2, kernel_initializer=self._weight_init, use_bias=self._use_bias,
                              kernel_regularizer=l2(self._weight_decay), activation="softmax")(flatten)
        predictions_a = Dense(units=101, kernel_initializer=self._weight_init, use_bias=self._use_bias,
                              kernel_regularizer=l2(self._weight_decay), activation="softmax")(flatten)

        self._model = Model(inputs=inputs, outputs=[predictions_g, predictions_a])
        self._graph = tf.get_default_graph()

    def _load_model(self, model_path):
        self._create_graph()
        self._model.load_weights(model_path)

    #Only can process one image per time
    def detect(self, face_images):
        gender_faces = [cv2.resize(image, (self.image_size, self.image_size)) for image in face_images]
        gender_faces = np.stack(gender_faces)
        gender_faces_float = gender_faces.astype(np.float64)
        with self._graph.as_default():
            #KTF.set_session(self._session)
            results = self._model.predict(gender_faces_float)

        # gender_face =  cv2.resize(face_image, (self.image_size, self.image_size))
        # result = self.model.predict(gender_face)
        #1 Male, 0 Female
        predicted_genders = results[0]
        predicted_ages = results[1].dot(self._ages).flatten()
        #deduct_year = 0  # This predicted age is much older for Asian person. So try to deduct a fixed value.
        return [ int(predicted_ages[i]) for i,_ in enumerate(predicted_genders)]