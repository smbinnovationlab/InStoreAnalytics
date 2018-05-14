package com.sap.innolabs.shopanalystic.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BounceRate {

	private int total;
	private int bounce;
	@Id
	private String daytime;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getBounce() {
		return bounce;
	}
	public void setBounce(int bounce) {
		this.bounce = bounce;
	}
	public String getDaytime() {
		return daytime;
	}
	public void setDaytime(String daytime) {
		this.daytime = daytime;
	}	
}
