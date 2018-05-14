package com.sap.innolabs.shopanalystic.repository;

import java.util.List;

import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.ws.rs.PathParam;

import org.hibernate.jpa.QueryHints;

import com.sap.innolabs.shopanalystic.model.AgeRangeInfor;
import com.sap.innolabs.shopanalystic.model.BounceRate;
import com.sap.innolabs.shopanalystic.model.ConversionPipline;
import com.sap.innolabs.shopanalystic.model.CustomerInfor;
import com.sap.innolabs.shopanalystic.model.CustomerSummaryInfor;
import com.sap.innolabs.shopanalystic.model.GeneralVistInfor;
import com.sap.innolabs.shopanalystic.model.ReturnFrequency;
import com.sap.innolabs.shopanalystic.model.ReturnRate;
import com.sap.innolabs.shopanalystic.model.RevenueInfor;
import com.sap.innolabs.shopanalystic.model.Sentiment;
import com.sap.innolabs.shopanalystic.model.TimeVisitInfor;
import com.sap.innolabs.shopanalystic.model.VisitImages;
import com.sap.innolabs.shopanalystic.model.VisitRecord;

public interface IShopReportRepository {
	public GeneralVistInfor GetGeneralInforByDate(String date);
	public List<TimeVisitInfor> GetTimeVisitInforByDate(String date);
	public List<AgeRangeInfor> GetAgeInforByDate(String date);
	public GeneralVistInfor GetGeneralInforByWeek(String begin, String end);
	public List<TimeVisitInfor> GetTimeVisitInforByWeek(String begin, String end);
	public List<AgeRangeInfor> GetAgeInforByWeek(String begin, String end);
	public List<VisitImages> GetVistiImagesInRoom();
	public void GenerateMockData();
	public void AddVisitRecord(VisitRecord record);
	public void UpdateVistRecord(VisitRecord record);
	
	public CustomerInfor GetCustomerInfor(String name);
	public void UpdateCustomerInfor(CustomerInfor record);
	public void DeleteCustomerInfor(Integer faceId);
	public void DeleteAllCustomerInfor();
	
	public void FocusCustomer(String faceID,VisitRecord record);
	public String GetCurrentFocusFace();
	public CustomerSummaryInfor GetCustomerDetailInforamtion(String faceID);  
	
	
	
	public List<RevenueInfor> GetTimeRevenueByDate(String date) ;
	public List<RevenueInfor> GetTimeRevenueByWeek(String begin, String end) ;
	public List<BounceRate> GetBounceRateByDate(String date) ;
	public List<BounceRate> GetBounceRateByWeek(String begin,String end) ;
	public List<ReturnRate> GetReturnRateByWeek(String begin,String end) ;
	public List<ReturnRate> GetReturnRateByDate(String date) ;
	public List<ReturnFrequency> GetReturnFrequencyByDate(String date) ;
	public List<ReturnFrequency> GetReturnFrequencyByWeek(String begin, String end) ;
	public List<ConversionPipline> GetConversionPiplineByDate(String date) ;
	public List<ConversionPipline> GetConversionPiplineByWeek(String begin, String end) ;
	public List<Sentiment> GetSentimentByDate(String date) ;
	public List<Sentiment> GetSentimentByWeek(String begin, String end) ;

}
