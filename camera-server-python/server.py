import cv2
import sys
import os
import repository.remote_repository as remote
from multiprocessing import Process, Queue
import multiprocessing
from tasks import *
from face_process.face_api import  FaceAPI
from web_socket import SocketServer
from image_server import ImageServer
from face_rec_server import  FaceRecServer
import threading


def run():

    service_layer_url = "https://localhost:30030/b1s/v1"
    service_layer_company = "Drone"
    service_layer_username = "manager"
    service_layer_password = "1234"
    analytcsi_server_url = "http://localhost:8080/app"

    print("main process:",os.getpid())
    face_api = FaceAPI()
    q = Queue()

    video_capture = cv2.VideoCapture(0)
    video_capture.set(cv2.CAP_PROP_FRAME_WIDTH, 1024)
    video_capture.set(cv2.CAP_PROP_FRAME_HEIGHT, 768)
    # Start image server in child process
    image_process = ImageServer(8733)
    pw = Process(target=image_process)
    pw.start()

    # start face recognization in child process
    face_rec_process =  FaceRecServer(face_api,q)
    fw = Process(target=face_rec_process)
    fw.start()

    #start socket server in current process
    socket_server = SocketServer(50030)
    socket_server.start()


    #run data sync
    # remote.set_base_url(analytcsi_server_url)
    # syncb1 = AutoSyncB1(analytcsi_server_url,service_layer_url,service_layer_company,service_layer_username,service_layer_password,face_api,q)
    # syncb1.run()

    #run monitor
    front =  FrontDoorMonitor(video_capture,face_api,q)
    back = BackDoorMonitor(video_capture,face_api)


    front.start()
    back.start()

    back.join()
    front.join()
    pw.terminate()
    fw.terminate()

if __name__ == '__main__':
    multiprocessing.freeze_support()
    sys.path.append(os.path.dirname(sys.path[0]))
    run()