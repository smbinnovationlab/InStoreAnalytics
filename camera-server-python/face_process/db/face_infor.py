from sqlalchemy import Column, String, Integer
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()

class FaceInfor(Base):
    # 表的名字:
    __tablename__ = 'FaceInfor'
    #__table_args__ = {'sqlite_autoincrement': True}

    # 表的结构:
    # id = Column(Integer, primary_key=True,autoincrement=True)
    faceid = Column(String(50), primary_key=True)
    embeding = Column(String(2000))