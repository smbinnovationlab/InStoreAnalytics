package com.sap.innolabs.shopanalystic.api;


import java.util.Date;
import java.util.List;

import com.sap.innolabs.shopanalystic.model.VisitImages;
import com.sap.innolabs.shopanalystic.model.VisitInforSummary;


public interface ShopReport {
	public VisitInforSummary GetVisitInforByDay(String date);
	public VisitInforSummary GetVisitInforByWeek(String begin, String end);
	public void GenerateFakeData();
	public List<VisitImages> GetVistiImagesInRoom();
}
