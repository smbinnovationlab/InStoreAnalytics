package com.sap.innolabs.shopanalystic.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AgeRangeInfor {

	@Id
	private int age;
	private int femaleNumber;
	private int maleNumber;
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getFemaleNumber() {
		return femaleNumber;
	}
	public void setFemaleNumber(int femaleNumber) {
		this.femaleNumber = femaleNumber;
	}
	public int getMaleNumber() {
		return maleNumber;
	}
	public void setMaleNumber(int maleNumber) {
		this.maleNumber = maleNumber;
	}
	
}
