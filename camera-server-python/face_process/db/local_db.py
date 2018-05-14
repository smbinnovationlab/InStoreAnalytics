import sqlite3
from sqlalchemy import *
from sqlalchemy.orm import *
from .face_infor import FaceInfor, Base
from sqlalchemy.ext.declarative import declarative_base

class LocalDB():
    def __init__(self):
        engine = create_engine('sqlite:///./face.db', echo=True)
        self._DBSession = sessionmaker(bind=engine)
        Base.metadata.create_all(engine)

    def create_face_embeding(self,new_face_id,embed):
        session = self._DBSession()
        try:
            new_face = FaceInfor( faceid = new_face_id, embeding=embed)
            session.add(new_face)
            session.commit()
        finally:
            session.close()

    def query_face_embeding(self, face_id):
        session = self._DBSession()
        try:
            face = session.query(FaceInfor).filter(FaceInfor.faceid == face_id).one()
            return face.embeding
        finally:
            session.close()

    def update_face_embeding(self, face_id, embeding):
        session = self._DBSession()
        try:
            face = session.query(FaceInfor).filter(FaceInfor.faceid == face_id).one()
            face.embeding = embeding
            session.commit()
        finally:
            session.close()

    def delete_all_emebding(self):
        session = self._DBSession()
        try:
            face = session.query(FaceInfor).delete()
            session.commit()
        finally:
            session.close()

    def get_all_face_embeding(self):
        session = self._DBSession()
        try:
            result_all = session.query(FaceInfor).all()
            return result_all
        finally:
            session.close()