import cv2
import os
import uuid
thumb_folder = './pics/'
def save_thumb(image):
    if not os.path.exists(thumb_folder):
        os.makedirs(thumb_folder)
    guid = str(uuid.uuid1())
    filename = guid + '.jpg'
    cv2.imwrite(thumb_folder+filename,image)
    return guid
