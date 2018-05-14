package com.sap.innolabs.shopanalystic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "\"CustomerInfor\"")
public class CustomerInfor {

	@Id
	@Column(name = "\"FaceID\"")
	private String FaceID;
	@Column(name = "\"Name\"")
	private String Name;
	@Column(name = "\"IsVip\"")
	private Integer IsVip;

	@Column(name = "\"IsCelebrity\"")
	private Integer IsCelebrity;

	@Column(name = "\"Currency\"")
	private Integer Currency;
	
	@Column(name = "\"RecentPurchased\"")
	private String RecentPurchased;
	
	@Column(name = "\"Recommand\"")
	private String Recommand;

	public String getFaceID() {
		return FaceID;
	}

	public void setFaceID(String faceID) {
		FaceID = faceID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Integer getIsVip() {
		return IsVip;
	}

	public void setIsVip(Integer isVip) {
		IsVip = isVip;
	}

	public Integer getIsCelebrity() {
		return IsCelebrity;
	}

	public void setIsCelebrity(Integer isCelebrity) {
		IsCelebrity = isCelebrity;
	}

	public Integer getCurrency() {
		return Currency;
	}

	public void setCurrency(Integer currency) {
		Currency = currency;
	}

	public String getRecentPurchased() {
		return RecentPurchased;
	}

	public void setRecentPurchased(String recentPurchased) {
		RecentPurchased = recentPurchased;
	}

	public String getRecommand() {
		return Recommand;
	}

	public void setRecommand(String recommand) {
		Recommand = recommand;
	}
	
	
}
