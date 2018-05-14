package com.sap.innolabs.shopanalystic.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class RevenueInfor implements Serializable {
	@Id
	private String time;
	private int value;
	@Id
	private String daytime;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getDaytime() {
		return daytime;
	}
	public void setDaytime(String daytime) {
		this.daytime = daytime;
	}
	
}
