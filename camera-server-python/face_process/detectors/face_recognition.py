import tensorflow as tf
import cv2
import os
import numpy as np

class FaceRecognition:
    def __init__(self):
        # Load Face embeding model
        self.gpu_memory_fraction = 0.5
        self.face_rec_freeze_mode = os.path.dirname(os.path.realpath(__file__))+ '/../models/new_freeze.pb'
        self.face_img_size = 160 #160
        self.face_graph = tf.Graph()
        #os.environ['CUDA_VISIBLE_DEVICES'] = '0'
        with self.face_graph.as_default():
            with tf.device('/gpu:0'):
                gpu_options = tf.GPUOptions(per_process_gpu_memory_fraction=self.gpu_memory_fraction)
                gpu_options.allow_growth = True
                self.faceSession = tf.Session(config=tf.ConfigProto(gpu_options=gpu_options,allow_soft_placement=True, log_device_placement=False))
                # self.faceSession = tf.Session()
                with tf.gfile.GFile(self.face_rec_freeze_mode, "rb") as f:
                    graph_def = tf.GraphDef()
                    graph_def.ParseFromString(f.read())
                    tf.import_graph_def(
                        graph_def,
                        input_map=None,
                        return_elements=None,
                        op_dict=None,
                        producer_op_list=None
                    )
        self.images_placeholder = self.face_graph.get_tensor_by_name("import/input:0")
        self.embeddings = self.face_graph.get_tensor_by_name("import/embeddings:0")
        self.phase_train_placeholder = self.face_graph.get_tensor_by_name("import/phase_train:0")

    def _prewhiten(self,x):
        mean = np.mean(x)
        std = np.std(x)
        std_adj = np.maximum(std, 1.0 / np.sqrt(x.size))
        y = np.multiply(np.subtract(x, mean), 1 / std_adj)
        return y

    def detect(self,face_images):
        images = []
        for image in face_images:
            res_image = cv2.resize(image, (self.face_img_size, self.face_img_size))
            images.append(self._prewhiten(res_image))
        images = np.stack(images)
        feed_dict = {self.images_placeholder: images, self.phase_train_placeholder: False}
        emb = self.faceSession.run(self.embeddings, feed_dict=feed_dict)
        return emb;
