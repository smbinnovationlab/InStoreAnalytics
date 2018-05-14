package com.sap.innolabs.shopanalystic.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ReturnRate {
	@Id
	private int newVisit;
	private int returnVisit;
	

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
