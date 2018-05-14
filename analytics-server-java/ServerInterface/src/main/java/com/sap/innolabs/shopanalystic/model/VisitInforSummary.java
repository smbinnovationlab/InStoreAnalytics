package com.sap.innolabs.shopanalystic.model;

import java.util.List;

public class VisitInforSummary {

	private GeneralVistInfor general;
	private List<TimeVisitInfor> timeVisit;
	private List<AgeRangeInfor> ageRange;
	private List<BounceRate> bounceRate;
	private List<ConversionPipline> conversionPipline;
	private List<ReturnFrequency> returnFrequency;
	private List<ReturnRate> returnRate;
	private List<RevenueInfor> revenueInfor;
	private List<Sentiment> sentiment;
	
	public GeneralVistInfor getGeneral() {
		return general;
	}
	public void setGeneral(GeneralVistInfor general) {
		this.general = general;
	}
	
	public List<TimeVisitInfor> getTimeVisit() {
		return timeVisit;
	}
	public void setTimeVisit(List<TimeVisitInfor> timeVisit) {
		this.timeVisit = timeVisit;
	}
	public List<AgeRangeInfor> getAgeRange() {
		return ageRange;
	}
	public void setAgeRange(List<AgeRangeInfor> ageRange) {
		this.ageRange = ageRange;
	}
	public List<BounceRate> getBounceRate() {
		return bounceRate;
	}
	public void setBounceRate(List<BounceRate> bounceRate) {
		this.bounceRate = bounceRate;
	}
	public List<ConversionPipline> getConversionPipline() {
		return conversionPipline;
	}
	public void setConversionPipline(List<ConversionPipline> conversionPipline) {
		this.conversionPipline = conversionPipline;
	}
	public List<ReturnFrequency> getReturnFrequency() {
		return returnFrequency;
	}
	public void setReturnFrequency(List<ReturnFrequency> returnFrequency) {
		this.returnFrequency = returnFrequency;
	}
	public List<ReturnRate> getReturnRate() {
		return returnRate;
	}
	public void setReturnRate(List<ReturnRate> returnRate) {
		this.returnRate = returnRate;
	}
	public List<RevenueInfor> getRevenueInfor() {
		return revenueInfor;
	}
	public void setRevenueInfor(List<RevenueInfor> revenueInfor) {
		this.revenueInfor = revenueInfor;
	}
	public List<Sentiment> getSentiment() {
		return sentiment;
	}
	public void setSentiment(List<Sentiment> sentiment) {
		this.sentiment = sentiment;
	}
	
	
	public VisitInforSummary(GeneralVistInfor general, List<TimeVisitInfor> timeVisit, List<AgeRangeInfor> ageRange,
			List<BounceRate> bounceRate, List<ConversionPipline> conversionPipline,
			List<ReturnFrequency> returnFrequency, List<ReturnRate> returnRate, List<RevenueInfor> revenueInfor,
			List<Sentiment> sentiment) {
		super();
		this.general = general;
		this.timeVisit = timeVisit;
		this.ageRange = ageRange;
		this.bounceRate = bounceRate;
		this.conversionPipline = conversionPipline;
		this.returnFrequency = returnFrequency;
		this.returnRate = returnRate;
		this.revenueInfor = revenueInfor;
		this.sentiment = sentiment;
	}
}
