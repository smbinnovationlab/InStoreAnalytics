import time
import json
import requests
import tornado
from tornado.httpclient import *

#base_url = "https://servercenter0ad1619475.hana.ondemand.com/ServerCenter-0.0.1/app"
base_url = "http://localhost:8080/app"
def error_handle(func):
    def wrapper(*args, **kwargs):
        try:
            func(*args, **kwargs)
        except Exception as e:
            print(e)
    return wrapper

@error_handle
def async_add_visisit_record( faceid,  age,  gender,  beauty, emotion, picid,entertime):
    dic = {"faceID":faceid,"age":age,"gender":gender,"beautyScore":beauty,"picId":picid,"emotion":emotion,"enterTime":entertime.strftime("%Y-%m-%dT%H:%M:%S") }

    requests.post(base_url+'/Camera/VisitRecord',headers={"Content-Type":"application/json"}, json=dic,verify=False)

    # s = json.dumps(dic)
    # http_client = AsyncHTTPClient()
    # request = HTTPRequest(base_url+'/Camera/VisitRecord',method='POST', headers={"Content-Type":"application/json"}, body = s)
    # http_client.fetch(request,handle_r    equest)
@error_handle
def async_focus_customer_with_record(faceid,  age,  gender,  beauty, emotion, picid, entertime):
    dic =  {"faceID": faceid, "age": age, "gender": gender, "beautyScore": beauty, "picId": picid,"emotion":emotion,
           "enterTime": entertime.strftime("%Y-%m-%dT%H:%M:%S")}
    requests.post(base_url + '/Camera/FocusCustomer/'+faceid, headers={"Content-Type": "application/json"}, json=dic,
                             verify=False)

@error_handle
def async_focus_customer(faceid):
    requests.post(base_url + '/Camera/FocusCustomer/'+faceid, headers={"Content-Type": "application/json"},
                             verify=False)

@error_handle
def async_update_leavel(faceid,emotion):
    dic = {"faceID": faceid, "emotion": emotion, "leaveTime": time.strftime("%Y-%m-%dT%H:%M:%S", time.localtime())}
    requests.patch(base_url + '/Camera/VisitRecord',headers={"Content-Type":"application/json"}, json=dic,verify=False)

@error_handle
def async_add_customer(faceid,  name,  recentbuy,  recommand,  currency):

    dic = {"faceID": faceid, "name": name, "isVip": 1, "isCelebrity": 0, "currency": currency,
           "recentPurchased": recentbuy, "recommand": recommand}
    requests.patch(base_url + '/Camera/CustomerInfor',headers={"Content-Type":"application/json"}, json=dic,verify=False)

@error_handle
def sync_check_customer_exist(name):
    response = requests.get(base_url + '/Camera/CustomerInfor?name=' + tornado.escape(name), verify=False)
    if response != None and response != '':
        return True
    else:
        return False
