package com.sap.innolabs.shopanalystic.repository.imp;

import java.util.Date;
import java.util.List;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.NotImplementedException;
import org.hibernate.jpa.QueryHints;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sap.innolabs.shopanalystic.model.AgeRangeInfor;
import com.sap.innolabs.shopanalystic.model.BounceRate;
import com.sap.innolabs.shopanalystic.model.ConversionPipline;
import com.sap.innolabs.shopanalystic.model.CustomerInfor;
import com.sap.innolabs.shopanalystic.model.GeneralVistInfor;
import com.sap.innolabs.shopanalystic.model.ReturnFrequency;
import com.sap.innolabs.shopanalystic.model.ReturnRate;
import com.sap.innolabs.shopanalystic.model.RevenueInfor;
import com.sap.innolabs.shopanalystic.model.Sentiment;
import com.sap.innolabs.shopanalystic.model.TimeVisitInfor;
import com.sap.innolabs.shopanalystic.model.VisitImages;
import com.sap.innolabs.shopanalystic.model.VisitRecord;
import com.sap.innolabs.shopanalystic.repository.IShopReportRepository;

@Repository
@ConditionalOnProperty(name = "db.type", havingValue = "MSSQL")
public class ShopReportRepository_MSSQL extends BaseShopReportRepository {

	
	public GeneralVistInfor GetGeneralInforByDate(String date) {
		Query query = this.entityManager
				.createNativeQuery("select COUNT(*) totalNumber,'Day ' + convert(varchar,MAX(EnterTime),121)  as readLastTime, CAST(SUM(Gender)*100/COUNT(*) as varchar(5)) + '%' malePercentage,  CAST(100 - SUM(Gender)*100/COUNT(*) as varchar(5)) +'%' femalePercentage ,\n" + 
						"ISNULL(SUM(CASE WHEN during =0 THEN 1 ELSE 0 END),0) duration1Min,ISNULL(SUM(CASE WHEN during >0 and during <=5 THEN 1 ELSE 0 END),0) duration5Min,\n" +
						"CAST(ISNULL(Sum(CASE WHEN Emotion = 0 then 1 end),0)*100 /SUM(case when Emotion IS NOT NULL then 1 end) AS VARCHAR(5))  AS angry ,\n" + 
						"CAST(ISNULL(Sum(CASE WHEN Emotion = 1 then 1 end),0)*100 /SUM(case when Emotion IS NOT NULL then 1 end) AS VARCHAR(5))   AS frown ,\n" + 
						"CAST(ISNULL(Sum(CASE WHEN Emotion = 2 then 1 end),0)*100 /SUM(case when Emotion IS NOT NULL  then 1 else 0 end) AS VARCHAR(5))   AS happy ,\n" + 
						"CAST(100 - ISNULL(Sum(CASE WHEN Emotion = 0 then 1 end),0)*100 /SUM(case when Emotion IS NOT NULL then 1 end)  - ISNULL(Sum(CASE WHEN Emotion = 1 then 1 end),0)*100 /SUM(case when Emotion IS NOT NULL then 1 end) -ISNULL(Sum(CASE WHEN Emotion = 2 then 1 end),0)*100 /SUM(case when Emotion IS NOT NULL then 1 end)   AS VARCHAR(5))  AS neutral ,\n" +
						"ISNULL(SUM(CASE WHEN during >5 and during <=15 THEN 1 ELSE 0 END),0) duration15Min, ISNULL(SUM(CASE WHEN during >15 THEN 1 ELSE 0 END),0) durationMore\n" + 
						"from \n" + 
						"(select DATEDIFF(SECOND,EnterTime,LeaveTime)/60 as during, * from VisitRecord where Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and DateDiff(Day,EnterTime,?1) = 0) A", GeneralVistInfor.class);
	
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, date);
		List<GeneralVistInfor> results = query.getResultList();
		if(results != null && results.size()>0)
		{
			return results.get(0);
		}
		return null;
	}
	
	
	
	public List<TimeVisitInfor> GetTimeVisitInforByDate(String date) {
		Query query = this.entityManager
				.createNativeQuery("select Datepart(HOUR,EnterTime) time,CAST( convert(date,EnterTime) AS varchar(20)) daytime, COUNT(*) number from VisitRecord\n" + 
						"where Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and DateDiff(Day,EnterTime,?1) = 0 \n" + 
						"group by Datepart(HOUR,EnterTime),CAST( convert(date,EnterTime) AS varchar(20)) order by time\n" +
						"", TimeVisitInfor.class);
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, date);
		List results = query.getResultList();
		return results;
	}
	
	public List<AgeRangeInfor> GetAgeInforByDate(String date) {
		Query query = this.entityManager
				.createNativeQuery("select (age/10)*10 as age,SUM(GENDER) AS maleNumber,SUM(ABS(GENDER-1))as femaleNumber from VisitRecord\n" + 
						"where Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE()  and DateDiff(Day,EnterTime,?1) = 0\n" + 
						"group by age/10 order by age/10", AgeRangeInfor.class);
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, date);
		List results = query.getResultList();
		return results;
	}
	
	public GeneralVistInfor GetGeneralInforByWeek(String begin, String end) {
		if(end == null || end.length() ==0 )
		{
			return null;
		}
		Query query = this.entityManager
				.createNativeQuery("select COUNT(*) totalNumber, 'Week ' + convert(varchar,MAX(EnterTime),121) as readLastTime, CAST(SUM(Gender)*100/COUNT(*) as varchar(5)) + '%' malePercentage,  CAST(100 - SUM(Gender)*100/COUNT(*) as varchar(5)) +'%' femalePercentage ,\n" + 
						"ISNULL(SUM(CASE WHEN during =0 THEN 1 ELSE 0 END),0) duration1Min,ISNULL(SUM(CASE WHEN during >0 and during <=5 THEN 1 ELSE 0 END),0) duration5Min,\n" +
						"CAST(ISNULL(Sum(CASE WHEN Emotion = 0 then 1 end),0)*100 /SUM(case when Emotion IS NOT NULL  then 1 end) AS VARCHAR(5))  AS happy ,\n" + 
						"CAST(ISNULL(Sum(CASE WHEN Emotion = 1 or Emotion = 2 then 1 end),0)*100 /SUM(case when Emotion IS NOT NULL  then 1 end) AS VARCHAR(5))   AS frown ,\n" + 
						"CAST(ISNULL(Sum(CASE WHEN Emotion = 3 then 1 end),0)*100 /SUM(case when Emotion IS NOT NULL then 1 end) AS VARCHAR(5))   AS angry ,\n" + 
						"CAST(100 - ISNULL(Sum(CASE WHEN Emotion = 0 then 1 end),0)*100 /SUM(case when Emotion IS NOT NULL then 1 end)  - ISNULL(Sum(CASE WHEN Emotion = 1 OR Emotion =2 then 1 end),0)*100 /SUM(case when Emotion IS NOT NULL then 1 end) -ISNULL(Sum(CASE WHEN Emotion = 3 then 1 end),0)*100 /SUM(case when Emotion IS NOT NULL then 1 end)   AS VARCHAR(5))   AS neutral ,\n" +
						"ISNULL(SUM(CASE WHEN during >5 and during <=15 THEN 1 ELSE 0 END),0) duration15Min, ISNULL(SUM(CASE WHEN during >15 THEN 1 ELSE 0 END),0) durationMore\n" + 
						"from \n" + 
						"(select DATEDIFF(SECOND,EnterTime,LeaveTime)/60 as during, * from VisitRecord where Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and EnterTime > ?1 and EnterTime < ?2 +' 23:59:59' ) A", GeneralVistInfor.class);
	
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, begin);
        query.setParameter(2, end);
		List<GeneralVistInfor> results = query.getResultList();
		if(results != null && results.size()>0)
		{
			return results.get(0);
		}
		return null;
	}
	
	public List<TimeVisitInfor> GetTimeVisitInforByWeek(String begin, String end) {
		if(end == null || end.length() ==0 )
		{
			return null;
		}
		Query query = this.entityManager	
				.createNativeQuery("select Datepart(HOUR,EnterTime) time,CAST( convert(date,EnterTime) AS varchar(20)) daytime,COUNT(*) number from VisitRecord\n" + 
						"where Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and EnterTime > ?1 and EnterTime < ?2 +' 23:59:59' \n" + 
						"group by Datepart(HOUR,EnterTime),CAST( convert(date,EnterTime) AS varchar(20)) order by daytime, time", TimeVisitInfor.class);
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, begin);
        query.setParameter(2, end);
		List results = query.getResultList();
		return results;
	}
	
	public List<AgeRangeInfor> GetAgeInforByWeek(String begin, String end) {
		if(end == null || end.length() ==0 )
		{
			return null;
		}
		Query query = this.entityManager
				.createNativeQuery("select (age/10)*10 as age,SUM(GENDER) AS maleNumber,SUM(ABS(GENDER-1))as femaleNumber from VisitRecord\n" + 
						"where Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE()  and EnterTime > ?1 and EnterTime < ?2 +' 23:59:59' \n" + 
						"group by age/10 order by age/10", AgeRangeInfor.class);
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, begin);
        query.setParameter(2, end);
		List results = query.getResultList();
		return results;
	}
	
	
	public List<VisitImages> GetVistiImagesInRoom() {
		Query query = this.entityManager
				.createNativeQuery("			SELECT A.FaceId,A.VisitCount, B.PicId,ISNULL(C.IsVip,0) as IsVip,ISNULL(C.IsCelebrity,0) as IsCelebrity, \n" + 
						"C.Name, 'VIP' VipType, ISNULL(C.Currency,0) balance ,30 spent, 6 visitCountIn90 ,C.recentPurchased, C.recommand  FROM \n" + 
						"						(SELECT T1.FaceID,T1.ID, COUNT(*) VisitCount FROM \n" + 
						"						(select top 20 FaceID, MAX(ID) ID  from VisitRecord	g			\n" + 
						"						where Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE()  and DateDiff(Day,EnterTime, GETDATE()) = 0 and LeaveTime is null\n" + 
						"						group by FaceID order by ID desc) T1 \n" + 
						"						left join VisitRecord T2 on T1.FaceID = T2.FaceID  group by T1.FaceID,T1.ID) A \n" + 
						"						inner join VisitRecord B on A.ID = B.ID  and PicId is not null \n" + 
						"						left join CustomerInfor C on B.FaceID= C.FaceID", VisitImages.class);
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
		List results = query.getResultList();
		return results;
	}
	
	public void GenerateMockData()
	{
		StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("GenearateMockData");
		query.registerStoredProcedureParameter("UntilCurrentTime", Integer.class, ParameterMode.IN);
		query.setParameter("UntilCurrentTime", 0);
		query.execute();
		
		query = this.entityManager.createStoredProcedureQuery("GenerateMockProductionInfor");
		query.execute();
	}
	
	
	public List<RevenueInfor> GetTimeRevenueByDate(String date) {
		Query query = this.entityManager
				.createNativeQuery("select Datepart(HOUR,EnterTime) time,CAST( convert(date,EnterTime) AS varchar(20)) daytime, SUM(Case when Spent is null then 0 else Spent end) value from VisitRecord\n" + 
						"where Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and DateDiff(Day,EnterTime,?1) = 0 \n" + 
						"group by Datepart(HOUR,EnterTime),CAST( convert(date,EnterTime) AS varchar(20)) order by time\n" +
						"", RevenueInfor.class);
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, date);
		List results = query.getResultList();
		return results;
	}
	
	public List<RevenueInfor> GetTimeRevenueByWeek(String begin, String end) {
		if(end == null || end.length() ==0 )
		{
			return null;
		}
		Query query = this.entityManager	
				.createNativeQuery("select Datepart(HOUR,EnterTime) time,CAST( convert(date,EnterTime) AS varchar(20)) daytime,SUM(Case when Spent is null then 0 else Spent end) value from VisitRecord\n" + 
						"where Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and EnterTime > ?1 and EnterTime < ?2 +' 23:59:59' \n" + 
						"group by Datepart(HOUR,EnterTime),CAST( convert(date,EnterTime) AS varchar(20)) order by daytime,time", RevenueInfor.class);
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, begin);
        query.setParameter(2, end);
		List results = query.getResultList();
		return results;
	}
	
	public List<BounceRate> GetBounceRateByDate(String date) {
		
		Query query = this.entityManager	
				.createNativeQuery("select CAST( convert(date,EnterTime) AS varchar(20)) daytime,SUM(Case when Spent is null or Spent =0 then 1 else 0 end) bounce,COUNT(*) total from VisitRecord\n" + 
						"where Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and DateDiff(Day,EnterTime,?1) = 0 \n" + 
						"group by CAST( convert(date,EnterTime) AS varchar(20)) order by daytime", BounceRate.class);
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, date);
		List results = query.getResultList();
		return results;
	}
	
	public List<BounceRate> GetBounceRateByWeek(String begin,String end) {
		
		Query query = this.entityManager	
				.createNativeQuery("select CAST( convert(date,EnterTime) AS varchar(20)) daytime,SUM(Case when Spent is null or Spent =0 then 1 else 0 end) bounce,COUNT(*) total from VisitRecord\n" + 
						"where Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and EnterTime > ?1 and EnterTime < ?2 +' 23:59:59' \n" + 
						"group by CAST( convert(date,EnterTime) AS varchar(20)) order by daytime", BounceRate.class);
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, begin);
        query.setParameter(2, end);
		List results = query.getResultList();
		return results;
	}
	
	public List<ReturnRate> GetReturnRateByWeek(String begin,String end) {
//		
		Query query = this.entityManager	
				.createNativeQuery("SELECT  SUM(CASE WHEN visitcount>0 THEN 1 ELSE 0 END) returnVisit,SUM(CASE WHEN visitcount>0 THEN 0 ELSE 1 END) newVisit \n" + 
						"FROM(select SUM(CASE WHEN B.FaceID is null then 0 else 1 end) AS visitcount,A.FaceID from \n" + 
						"(SELECT * FROM VisitRecord \n" + 
						"WHERE  Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and EnterTime > ?1 and EnterTime < ?2 +' 23:59:59' \n" + 
						") A left join \n" + 
						"(SELECT * FROM VisitRecord \n" + 
						"WHERE  Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and EnterTime < ?1 \n" + 
						") B on A.FaceID = B.FaceID \n" + 
						"GROUP BY A.FaceID ) T", ReturnRate.class);
//		Query query = this.entityManager	
//				.createNativeQuery(" SELECT  SUM(CASE WHEN Age <18 THEN 1 ELSE 0 END) returnVisit , SUM(CASE WHEN Age >= 18 THEN 1 ELSE 0 END)  newVisit FROM VisitRecord \n" + 
//						"WHERE  Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and EnterTime > ?1 and EnterTime < ?2 +' 23:59:59'", ReturnRate.class);
//        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, begin);
        query.setParameter(2, end);
		List results = query.getResultList();
		return results;
	}
	
	public List<ReturnRate> GetReturnRateByDate(String date) {
		
		Query query = this.entityManager	
				.createNativeQuery("SELECT  SUM(CASE WHEN visitcount>0 THEN 1 ELSE 0 END) returnVisit,SUM(CASE WHEN visitcount>0 THEN 0 ELSE 1 END) newVisit \n" + 
						"FROM(select SUM(CASE WHEN B.FaceID is null then 0 else 1 end) AS visitcount,A.FaceID from \n" + 
						"(SELECT * FROM VisitRecord \n" + 
						"WHERE  Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and DateDiff(Day,EnterTime,?1) = 0  \n" + 
						") A left join \n" + 
						"(SELECT * FROM VisitRecord \n" + 
						"WHERE  Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and EnterTime < ?1 \n" + 
						") B on A.FaceID = B.FaceID \n" + 
						"GROUP BY A.FaceID ) T", ReturnRate.class);
//		Query query = this.entityManager	
//				.createNativeQuery(" SELECT  SUM(CASE WHEN Age <18 THEN 1 ELSE 0 END) returnVisit , SUM(CASE WHEN Age >= 18 THEN 1 ELSE 0 END)  newVisit FROM VisitRecord \n" + 
//						"WHERE  Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and DateDiff(Day,EnterTime,?1) = 0  ", ReturnRate.class);
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, date);
		List results = query.getResultList();
		return results;
	}
	
	public List<ReturnFrequency> GetReturnFrequencyByDate(String date) {
		
		Query query = this.entityManager	
				.createNativeQuery(" SELECT  SUM(CASE WHEN visitcount>0 THEN 1 ELSE 0 END) returnVisit,SUM(CASE WHEN visitcount>0 THEN 0 ELSE 1 END) newVisit,T.daytime,T.time\n" + 
						"FROM( \n" + 
						"select SUM(CASE WHEN B.FaceID is null then 0 else 1 end) AS visitcount,A.FaceID,Datepart(HOUR,A.EnterTime) time,CAST( convert(date,A.EnterTime) AS varchar(20)) daytime from \n" + 
						"(SELECT * FROM VisitRecord \n" + 
						"WHERE  Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and DateDiff(Day,EnterTime,?1) = 0 \n" + 
						") A left join \n" + 
						"(SELECT * FROM VisitRecord \n" + 
						"WHERE  Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and EnterTime < ?1 \n" + 
						") B on A.FaceID = B.FaceID\r\n" + 
						"group by A.FaceID,Datepart(HOUR,A.EnterTime),CAST( convert(date,A.EnterTime) AS varchar(20)) \n" + 
						" ) T \n" + 
						" group by T.daytime,T.[time] order by daytime,time", ReturnFrequency.class);
		
//		Query query = this.entityManager	
//				.createNativeQuery("SELECT Datepart(HOUR,EnterTime) time,CAST( convert(date,EnterTime) AS varchar(20)) daytime, SUM(CASE WHEN Age <18 THEN 1 ELSE 0 END) returnVisit , SUM(CASE WHEN Age >= 18 THEN 1 ELSE 0 END)  newVisit FROM VisitRecord \n" + 
//						"WHERE  Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and DateDiff(Day,EnterTime,?1) = 0  \n" + 
//						"group by Datepart(HOUR,EnterTime),CAST( convert(date,EnterTime) AS varchar(20)) ORDER BY daytime, time", ReturnFrequency.class);
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, date);
		List results = query.getResultList();
		return results;
	}
	
	
	public List<ReturnFrequency> GetReturnFrequencyByWeek(String begin, String end) {
		
		Query query = this.entityManager	
				.createNativeQuery(" SELECT  SUM(CASE WHEN visitcount>0 THEN 1 ELSE 0 END) returnVisit,SUM(CASE WHEN visitcount>0 THEN 0 ELSE 1 END) newVisit,T.daytime,T.time\n" + 
						"FROM( \n" + 
						"select SUM(CASE WHEN B.FaceID is null then 0 else 1 end) AS visitcount,A.FaceID,Datepart(HOUR,A.EnterTime) time,CAST( convert(date,A.EnterTime) AS varchar(20)) daytime from \n" + 
						"(SELECT * FROM VisitRecord \n" + 
						"WHERE  Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and EnterTime > ?1 and EnterTime < ?2 +' 23:59:59' \n" + 
						") A left join \n" + 
						"(SELECT * FROM VisitRecord \n" + 
						"WHERE  Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and EnterTime < ?1 \n" + 
						") B on A.FaceID = B.FaceID\r\n" + 
						"group by A.FaceID,Datepart(HOUR,A.EnterTime),CAST( convert(date,A.EnterTime) AS varchar(20)) \n" + 
						" ) T \n" + 
						" group by T.daytime,T.[time] order by daytime,time", ReturnFrequency.class);
//		Query query = this.entityManager	
//				.createNativeQuery("SELECT Datepart(HOUR,EnterTime) time,CAST( convert(date,EnterTime) AS varchar(20)) daytime, SUM(CASE WHEN Age <18 THEN 1 ELSE 0 END) returnVisit , SUM(CASE WHEN Age >= 18 THEN 1 ELSE 0 END)  newVisit FROM VisitRecord \n" + 
//						"WHERE  Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and EnterTime > ?1 and EnterTime < ?2 +' 23:59:59'  \n" + 
//						"group by Datepart(HOUR,EnterTime),CAST( convert(date,EnterTime) AS varchar(20)) ORDER BY daytime, time", ReturnFrequency.class);
//		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, begin);
        query.setParameter(2, end);
		List results = query.getResultList();
		return results;
	}
	
	
	public List<ConversionPipline> GetConversionPiplineByDate(String date) {
		Query query = this.entityManager
				.createNativeQuery("select Datepart(HOUR,EnterTime) time,CAST( convert(date,EnterTime) AS varchar(20)) daytime, SUM(Case when Spent is null or Spent =0 then 0 else 1 end) value from VisitRecord\n" + 
						"where Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and DateDiff(Day,EnterTime,?1) = 0 \n" + 
						"group by Datepart(HOUR,EnterTime),CAST( convert(date,EnterTime) AS varchar(20)) order by time\n" +
						"", ConversionPipline.class);
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, date);
		List results = query.getResultList();
		return results;
	}
	
	public List<ConversionPipline> GetConversionPiplineByWeek(String begin, String end) {
		if(end == null || end.length() ==0 )
		{
			return null;
		}
		Query query = this.entityManager	
				.createNativeQuery("select Datepart(HOUR,EnterTime) time,CAST( convert(date,EnterTime) AS varchar(20)) daytime,SUM(Case when Spent is null or Spent =0 then 0 else 1 end) value from VisitRecord\n" + 
						"where Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and EnterTime > ?1 and EnterTime < ?2 +' 23:59:59' \n" + 
						"group by Datepart(HOUR,EnterTime),CAST( convert(date,EnterTime) AS varchar(20)) order by daytime,time", ConversionPipline.class);
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, begin);
        query.setParameter(2, end);
		List results = query.getResultList();
		return results;
	}
	
	
	public List<Sentiment> GetSentimentByDate(String date) {
	
		Query query = this.entityManager	
				.createNativeQuery("select Datepart(HOUR,EnterTime) time,CAST( convert(date,EnterTime) AS varchar(20)) daytime,emotion,COUNT(Emotion) value from VisitRecord \n" + 
						"						where Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and DateDiff(Day,EnterTime,?1) = 0 and Emotion is not null \n" + 
						"						group by Datepart(HOUR,EnterTime),CAST( convert(date,EnterTime) AS varchar(20)),Emotion order by daytime,time \n" + 
						"", Sentiment.class);
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, date);
		List results = query.getResultList();
		return results;
	}
	
	public List<Sentiment> GetSentimentByWeek(String begin, String end) {
		if(end == null || end.length() ==0 )
		{
			return null;
		}
		Query query = this.entityManager	
				.createNativeQuery("select Datepart(HOUR,EnterTime) time,CAST( convert(date,EnterTime) AS varchar(20)) daytime,emotion,COUNT(Emotion) value from VisitRecord \n" + 
						"						where Datepart(HOUR,EnterTime)>=9 and EnterTime < GETDATE() and EnterTime > ?1 and EnterTime < ?2 +' 23:59:59'  and Emotion is not null \n" + 
						"						group by Datepart(HOUR,EnterTime),CAST( convert(date,EnterTime) AS varchar(20)),Emotion order by daytime,time \n" + 
						"", Sentiment.class);
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, begin);
        query.setParameter(2, end);
		List results = query.getResultList();
		return results;
	}
}
