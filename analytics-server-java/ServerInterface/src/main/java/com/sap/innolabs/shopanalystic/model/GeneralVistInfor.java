package com.sap.innolabs.shopanalystic.model;

import javax.persistence.SqlResultSetMapping;
import javax.persistence.Column;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;

//@SqlResultSetMapping(name="CustomerDetailsResult",classes={@ConstructorResult(
//	               targetClass=SumVisitInfor.class,
//	                 columns={
//	                    @ColumnResult(name="id"),
//	                    @ColumnResult(name="name"),
//	                    @ColumnResult(name="orderCount"),
//	                    @ColumnResult(name="avgOrder", type=Double.class)
//	                    }
//	          )
//	       })
@Entity
public class GeneralVistInfor {
	
	@Id
	private Integer totalNumber;
	
	private String malePercentage;
	private String femalePercentage;
	private Integer duration1Min;
	private Integer duration5Min;
	private Integer duration15Min;
	private Integer durationMore;
	
	private Integer happy;
	private Integer frown;
	private Integer angry;
	private Integer neutral;
	
	private String readLastTime;
	
	public String getMalePercentage() {
		return malePercentage;
	}
	public void setMalePercentage(String malePercentage) {
		this.malePercentage = malePercentage;
	}
	public String getFemalePercentage() {
		return femalePercentage;
	}
	public void setFemalePercentage(String femalePercentage) {
		this.femalePercentage = femalePercentage;
	}
	
	public Integer getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}
	
	public Integer getDuration1Min() {
		return duration1Min;
	}
	public void setDuration1Min(Integer duration1Min) {
		this.duration1Min = duration1Min;
	}
	public Integer getDuration15Min() {
		return duration15Min;
	}
	public void setDuration15Min(Integer duration15Min) {
		this.duration15Min = duration15Min;
	}
	public Integer getDuration5Min() {
		return duration5Min;
	}
	public void setDuration5Min(Integer duration5Min) {
		this.duration5Min = duration5Min;
	}
	public Integer getDurationMore() {
		return durationMore;
	}
	public void setDurationMore(Integer durationMore) {
		this.durationMore = durationMore;
	}
	public Integer getHappy() {
		return happy;
	}
	public void setHappy(Integer happy) {
		this.happy = happy;
	}
	public Integer getFrown() {
		return frown;
	}
	public void setFrown(Integer frown) {
		this.frown = frown;
	}
	public Integer getAngry() {
		return angry;
	}
	public void setAngry(Integer angry) {
		this.angry = angry;
	}
	public Integer getNeutral() {
		return neutral;
	}
	public void setNeutral(Integer neutral) {
		this.neutral = neutral;
	}
	public String getReadLastTime() {
		return readLastTime;
	}
	public void setReadLastTime(String readLastTime) {
		this.readLastTime = readLastTime;
	}
	
	
	
}
