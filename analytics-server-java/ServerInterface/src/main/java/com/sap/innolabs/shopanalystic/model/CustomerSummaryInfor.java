package com.sap.innolabs.shopanalystic.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

public class CustomerSummaryInfor {
	private String FaceID;
	private String Name;
	private Integer RecentEmotion;
	private Integer Age;
	private Integer Gender;
	private String PicId;
	private Integer CurrentDuration;
	
	private Integer LastDuration;
	private Integer IsVip;
	
	private List<VisitRecord> VisitRecord;
	public String getFaceID() {
		return FaceID;
	}
	public void setFaceID(String faceID) {
		FaceID = faceID;
	}
	public Integer getRecentEmotion() {
		return RecentEmotion;
	}
	public void setRecentEmotion(Integer recentEmotion) {
		RecentEmotion = recentEmotion;
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
	public String getPicId() {
		return PicId;
	}
	public void setPicId(String picId) {
		PicId = picId;
	}
	public Integer getCurrentDuration() {
		return CurrentDuration;
	}
	public void setCurrentDuration(Integer currentDuration) {
		CurrentDuration = currentDuration;
	}
	public Integer getLastDuration() {
		return LastDuration;
	}
	public void setLastDuration(Integer lastDuration) {
		LastDuration = lastDuration;
	}
	public List<VisitRecord> getVisitRecord() {
		return VisitRecord;
	}
	public void setVisitRecord(List<VisitRecord> visitRecord) {
		VisitRecord = visitRecord;
	}
	public Integer getIsVip() {
		return IsVip;
	}
	public void setIsVip(Integer isVip) {
		IsVip = isVip;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
}
