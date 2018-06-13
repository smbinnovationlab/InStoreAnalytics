import requests
import re
import uuid
import os
import random
import cv2
from repository.remote_repository import *
from threading import Thread
class AutoSyncB1():
    def __init__(self, analytics_server ,service_layer_url, company, username,password ,face_api,queue):
        super(AutoSyncB1, self).__init__()
        self._head ={"Content-Type": "application/json"}
        self._base = service_layer_url
        self._queue = queue
        self._pic_folder = './thinclient_pic/'
        self._ext = '.jpg'
        self._api = face_api
        self._company = company
        self._username = username
        self._password = password
        self._analytics_server = analytics_server

        if not os.path.exists(self._pic_folder):
            os.makedirs(self._pic_folder)

    def generate_vip(self,sl,bp):
        pic_name = str(uuid.uuid1())
        r = sl.get(self._base + "/Attachments2("+str(bp["AttachmentEntry"])+")/$value",verify=False)
        if not os.path.exists(self._pic_folder + pic_name + self._ext):
            pic_path = self._pic_folder + pic_name + self._ext
            with open(pic_path, 'wb') as pichandler:
                pichandler.write(r.content)
            self.register_customer_in_server(pic_path,bp)
    def add_new_product(self,sl,analytics,item):
        r = sl.get(self._base + "/ItemImages(" + str(item["ItemCode"]) + ")/$value",verify=False)
        fname = item["Picture"]
        if(fname.endswith("jpg") or fname.endswith("bmp") or fname.endswith("png") or fname.endswith("gif")):
            pic_name = str(uuid.uuid1())
            pic_path = self._pic_folder + pic_name + self._ext
            with open(pic_path, 'wb') as pichandler:
                pichandler.write(r.content)

            #upload file
            file = open(pic_path, 'rb')
            files = {'file': file}
            fileReturn = analytics.post(self._analytics_server + "/Camera/ProductImage", files=files,verify=False)

            #upload product
            dic = {"Name": item["ItemName"], "Price": item["ItemPrices"][0]["Price"], "Notes": item["User_Text"],
                   "Pic": fileReturn.text}
            analytics.post(self._analytics_server + "/Camera/Product", headers=self._head, json=dic,verify=False)


    def run(self):
        try:
            dic = {"CompanyDB": self._company, "UserName": self._username, "Password": self._password}
            sl_session = requests.session()
            an_session = requests.session()
            ret = sl_session.post(self._base + "/Login", headers=self._head, json=dic,verify=False)
            ret = ret.json()

            if ret is not None and "SessionId" in ret:
                bps = sl_session.get(self._base + "/BusinessPartners?$select=CardCode,CardName,AttachmentEntry",
                                     headers=self._head, json=dic,verify=False).json()["value"]
                items = sl_session.get(self._base + "/Items?$select=ItemCode,Picture,ItemName,User_Text,ItemPrices,AttachmentEntry",
                                     headers=self._head, json=dic,verify=False).json()["value"]

                vips = an_session.get(self._analytics_server + "/Camera/CustomerInfors", headers=self._head,verify=False).json()
                all_products = an_session.get(self._analytics_server + "/Camera/Products", headers=self._head,verify=False).json()

                for bp in bps:
                    if bp["AttachmentEntry"] != None:
                        found =False
                        for vip in vips:
                            if vip["name"] == bp["CardName"]:
                                found =True;
                        if not found:
                            self.generate_vip(sl_session, bp)
                for itm in items:
                    if itm["Picture"] != None:
                        found = False
                        for product in all_products:
                            if product["name"] == itm["ItemName"]:
                                found =True
                        if not found:
                            self.add_new_product(sl_session,an_session,itm)
        except Exception as t:
            pass

    def register_customer_in_server(self,filename,infor):
        try:
            img = cv2.imread(filename)
            img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
            height, width, _ = img.shape
            bounding_boxes = self._api.detect_faces(img)
            #min(int(right + 0.2 * width), img_w - 1)
            rec_face_images= []
            for face_position in bounding_boxes:
                left, top, right, bottom = face_position[0], face_position[1], face_position[2], face_position[3]
                if left < 0 or top < 0 or right > width or bottom > height:
                    continue
                rec_face_images.append(img[top:bottom + 1, left:right + 1, :])
            if len(rec_face_images) > 0:
                self._queue.put((1,rec_face_images, [infor["CardName"],"0","0","0"]))
        except Exception:
            pass

