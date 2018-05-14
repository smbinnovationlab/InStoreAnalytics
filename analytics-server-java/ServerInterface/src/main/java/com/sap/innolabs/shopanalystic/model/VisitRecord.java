package com.sap.innolabs.shopanalystic.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name="VisitRecord")
public class VisitRecord {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
//    @GeneratedValue(generator="SEQ_VISITRECORD_ID")
//    @SequenceGenerator(name="SEQ_VISITRECORD_ID",sequenceName="SEQ_VISITRECORD_ID", allocationSize=1)
	private Integer ID;
	
	@Column(name ="\"FaceID\"")
	private String FaceID;
	@Column(name ="\"Emotion\"")
	private Integer Emotion;
	@Column(name ="\"Age\"")
	private Integer Age;
	@Column(name ="\"Gender\"")
	private Integer Gender;
	@Column(name ="\"BeautyScore\"")
	private Integer BeautyScore;
	@Column(name ="\"EnterTime\"")
	private String  EnterTime;
	@Column(name ="\"LeaveTime\"")
	private String  LeaveTime;
	@Column(name ="\"PicId\"")
	private String PicId;
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public String getFaceID() {
		return FaceID;
	}
	public void setFaceID(String faceID) {
		FaceID = faceID;
	}
	public Integer getEmotion() {
		return Emotion;
	}
	public void setEmotion(Integer emotion) {
		Emotion = emotion;
	}
	public Integer getAge() {
		return Age;
	}
	public void setAge(Integer age) {
		Age = age;
	}
	public Integer getGender() {
		return Gender;
	}
	public void setGender(Integer gender) {
		Gender = gender;
	}
	public Integer getBeautyScore() {
		return BeautyScore;
	}
	public void setBeautyScore(Integer beautyScore) {
		BeautyScore = beautyScore;
	}
	public String getEnterTime() {
		return EnterTime;
	}
	public void setEnterTime(String enterTime) {
		EnterTime = enterTime;
	}
	public String getLeaveTime() {
		return LeaveTime;
	}
	public void setLeaveTime(String leaveTime) {
		LeaveTime = leaveTime;
	}
	public String getPicId() {
		return PicId;
	}
	public void setPicId(String picId) {
		PicId = picId;
	}
	
	
	
}
