import requests
import os
import random
import cv2
from repository.remote_repository import *
from threading import Thread
class AutoSyncThinClient():
    def __init__(self,thin_client_url,face_api,queue):
        super(AutoSyncThinClient, self).__init__()
        self._head ={"Content-Type": "application/json"}
        self._base = thin_client_url
        self._queue = queue
        self._pic_folder = './thinclient_pic/'
        self._ext = '.jpg'
        self._api = face_api
        if not os.path.exists(self._pic_folder):
            os.makedirs(self._pic_folder)
    def run(self):
        dic = {"schema": "INNO_BUYBUTTON", "password": "1234", "username": "manager"}
        s = requests.session()
        s.post(self._base + "/app-web/login.svc", headers=self._head, json=dic)

        bpinform = s.post(self._base + "/app-web/di.svc?object=OCRD&tableAlias=OCRD&action=Bo.Search&$select=CardCode,CardName,Picture,GroupCode",headers=self._head).json();
        if bpinform is not None and bpinform != '':
            items =  s.get(self._base + "/app-web/di.svc?object=OITM&tableAlias=OITM&action=Bo.Search&$select=ItemName",headers=self._head).json()
            item_dict = set()
            for item in items['data']:
                item_dict.add(item['ItemName'])
            for bp in bpinform['data']:
                cardName = bp["CardName"]
                cardCode = bp["CardCode"]
                group = bp["GroupCode"]
                if group is not None and group == 102:
                    pic = bp["Picture"];

                    if pic is not None and pic != '' and  not os.path.exists(self._pic_folder + pic+ self._ext):
                        pic_path = self._pic_folder + pic + self._ext
                        r = s.get(self._base +"/app-web/attachment.svc?type=picture&file=" + pic);
                        with open(pic_path,'wb') as pichandler:
                            pichandler.write(r.content)

                        currency = "30"
                        recentbuy = ""
                        recommand = ""
                        order = s.get(self._base+ "/app-web/odata2.svc/ORDR/ORDR()?$select=DocEntry&$filter=(((CardCode%20eq%20%27" + cardCode + "%27)%20and%20(DocStatus%20eq%20%27O%27)))&$top=124",headers=self._head).json()

                        if order is not None and len(order)>0:
                            orderEntry = order["d"]["results"][0]["DocEntry"]["value"]
                            detail = s.get(self._base+"/app-web/di.svc?object=ORDR&tableAlias=ORDR&action=Bo.Get&id=ORDR%2C" + str(orderEntry),headers=self._head).json()
                            rdr1 = detail["data"][0]["RDR1"]
                            currency = detail["data"][0]["DocTotal"]
                            for child in rdr1:
                                item_dict.remove(child["Dscription"])
                                recentbuy += child["Dscription"]+ ","

                        recentbuy = recentbuy.strip (',')
                        item_list = list(item_dict)
                        if len(item_list) > 0:
                            recommand = item_list[random.randint(0,len(item_list))-1]

                        faceid = self.register_customer_in_server(pic_path,(cardName, recentbuy, recommand,currency));
                        async_add_customer(faceid, cardName, recentbuy, recommand,currency)


    def register_customer_in_server(self,filename,infor):
        img = cv2.imread(filename)
        height, width, _ = img.shape
        bounding_boxes, _ = self._api.detect_faces(img)
        #min(int(right + 0.2 * width), img_w - 1)
        rec_face_images= []
        for face_position in bounding_boxes:
            left, top, right, bottom = max(face_position[0],0), max(face_position[1],0), min(face_position[2],width-1), min(face_position[3],height-1)

            image = img[top:bottom + 1, left:right + 1, :]
            rec_face_images.append(image)
        # face_images = []
        # face_position = bounding_boxes[0].astype(int)
        # left, top, right, bottom = face_position[0], face_position[1], face_position[2], face_position[3]
        # face_images.append(img[top:bottom + 1, left:right + 1, :])
        # result = self._api.detect_face_id_embeding(face_images)
        # for index, (faceid, embeding) in enumerate(result):
        #     need_record = False
        #     if faceid == self._api.none_find_faceid:
        #         faceid = self._api.register_new_face(embeding)
        #     return faceid
        if len(rec_face_images) > 0:
            self._queue.put((rec_face_images, infor))