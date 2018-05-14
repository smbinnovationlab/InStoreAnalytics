import cv2
class BlurryDetector(object):
    def __init__(self):
        self._threshhold = 80
    def check_is_blurry(self,img):
        gray = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)
        gray =  cv2.resize(gray, (100, 100))
        score =  cv2.Laplacian(gray, cv2.CV_64F).var()
        return False if score > self._threshhold else True
