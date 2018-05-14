package com.sap.innolabs.shopanalystic.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class VisitImages {

	@Id
	private String picId;
	private String faceId;
	private boolean isVip;
	private boolean isCelebrity;
	private int visitCount;
	private String name;
	private String vipType;
	private int balance;
	private int spent;
	private int visitCountIn90;
	
	private String recentPurchased;
	private String recommand;
	
	public String getPicId() {
		return picId;
	}
	public void setPicId(String picId) {
		this.picId = picId;
	}
	public boolean isVip() {
		return isVip;
	}
	public void setVip(boolean isVip) {
		this.isVip = isVip;
	}
	public boolean isCelebrity() {
		return isCelebrity;
	}
	public void setCelebrity(boolean isCelebrity) {
		this.isCelebrity = isCelebrity;
	}
	public int getVisitCount() {
		return visitCount;
	}
	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVipType() {
		return vipType;
	}
	public void setVipType(String vipType) {
		this.vipType = vipType;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public int getSpent() {
		return spent;
	}
	public void setSpent(int spent) {
		this.spent = spent;
	}
	public int getVisitCountIn90() {
		return visitCountIn90;
	}
	public void setVisitCountIn90(int visitCountIn90) {
		this.visitCountIn90 = visitCountIn90;
	}
	public String getRecentPurchased() {
		return recentPurchased;
	}
	public void setRecentPurchased(String recentPurchased) {
		this.recentPurchased = recentPurchased;
	}
	public String getRecommand() {
		return recommand;
	}
	public void setRecommand(String recommand) {
		this.recommand = recommand;
	}
	public String getFaceId() {
		return faceId;
	}
	public void setFaceId(String faceId) {
		this.faceId = faceId;
	}
	
	
}
