package com.sap.innolabs.shopanalystic.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ReturnFrequency implements Serializable {
	@Id
	private String time;
	private int newVisit;
	private int returnVisit;
	@Id
	private String daytime;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getDaytime() {
		return daytime;
	}
	public void setDaytime(String daytime) {
		this.daytime = daytime;
	}
	public int getNewVisit() {
		return newVisit;
	}
	public void setNewVisit(int newVisit) {
		this.newVisit = newVisit;
	}
	public int getReturnVisit() {
		return returnVisit;
	}
	public void setReturnVisit(int returnVisit) {
		this.returnVisit = returnVisit;
	}
	
	
}
