import tornado
import tornado.web
import tornado.websocket
import os
import cv2
import base64
from threading import Thread, Lock


class SocketServer(Thread):
    def __init__(self,port):
        super(SocketServer,self).__init__()
        self._port = port

    def run(self):
        app = _Application()
        app.listen(self._port)
        tornado.ioloop.IOLoop.current().start()

    @staticmethod
    def send(frame):
        cnt = cv2.imencode('.jpg', frame)[1]
        str = base64.b64encode(cnt.ravel())
        _ChatSocketHandler.send("data:image/jpg;base64," + str.decode('ascii'))

class _Application(tornado.web.Application):
    def __init__(self):
        handlers = [
            (r"/", _ChatSocketHandler),
            (r"/static", _MainHandler),
        ]
        settings = dict(
            template_path=os.path.join(os.path.dirname(__file__), "templates"),
            static_path=os.path.join(os.path.dirname(__file__), "static"),
            xsrf_cookies=True,
        )
        super(_Application, self).__init__(handlers, **settings)

class _MainHandler(tornado.web.RequestHandler):
    def get(self):
        self.render("index.html", messages=_ChatSocketHandler.cache)

class _ChatSocketHandler(tornado.websocket.WebSocketHandler):
    waiters = set()
    cache = []
    cache_size = 200
    locker = Lock()
    def check_origin(self, origin):
        return True

    def get_compression_options(self):
        # Non-None enables compression with default options.
        return {}

    def open(self):
        _ChatSocketHandler.locker.acquire()
        _ChatSocketHandler.waiters.add(self)
        _ChatSocketHandler.locker.release()

    def on_close(self):
        _ChatSocketHandler.locker.acquire()
        _ChatSocketHandler.waiters.remove(self)
        _ChatSocketHandler.locker.release()

    @classmethod
    def update_cache(cls, chat):
        cls.cache.append(chat)
        if len(cls.cache) > cls.cache_size:
            cls.cache = cls.cache[-cls.cache_size:]
    @classmethod
    def send(cls, chat):
        _ChatSocketHandler.locker.acquire()
        for waiter in cls.waiters:
            try:
                waiter.write_message(chat)
            except:
                pass
        _ChatSocketHandler.locker.release()
