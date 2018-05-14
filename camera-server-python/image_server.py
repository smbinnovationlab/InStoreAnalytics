import tornado.ioloop
import tornado.web
import sys
import os

class MyStaticHandler(tornado.web.StaticFileHandler):

    def set_default_headers(self):
        self.set_header("Access-Control-Allow-Origin", "*")
        self.set_header("Access-Control-Allow-Headers", "x-requested-with")
        self.set_header('Access-Control-Allow-Methods', 'POST, GET, OPTIONS')


class ImageServer():
    def __init__(self,port):
        self._port = port

    def __call__(self):
        path = __file__
        if getattr(sys, 'frozen', False):
            path = os.path.realpath(sys.argv[0])
        settings = {
            "static_path": os.path.join(os.path.dirname(path), "pics"),
            "xsrf_cookies": True,
        }
        app = tornado.web.Application([
            (r"/images/(.*)", MyStaticHandler,
             dict(path=settings['static_path'])),
        ])
        app.listen(self._port)
        tornado.ioloop.IOLoop.current().start()




