package com.sap.innolabs.shopanalystic.repository.imp;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

import org.hibernate.jpa.QueryHints;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
import com.sap.innolabs.shopanalystic.repository.IShopReportRepository;

@Repository
@ConditionalOnProperty(name = "db.type", havingValue = "HANA")
public class ShopReportRepository_HANA extends BaseShopReportRepository {

	public GeneralVistInfor GetGeneralInforByDate(String date) {

		String sqlstr = "" + "SELECT " + "	 COUNT(*) AS \"totalNumber\", "
				+ "	 'Day ' || CAST(MAX(\"EnterTime\") AS varchar) AS \"readLastTime\", "
				+ "	 CAST(FLOOR(SUM(\"Gender\") * 100 / COUNT(*)) AS varchar(5)) || '%' AS \"malePercentage\", "
				+ "	 CAST(100 - FLOOR(SUM(\"Gender\") * 100 / COUNT(*)) AS varchar(5)) || '%' AS \"femalePercentage\", "
				+ "	 IFNULL(SUM(CASE WHEN \"during\" = 0 " + "		THEN 1 " + "		ELSE 0 " + "		END), "
				+ "	 0) AS \"duration1Min\", " + "	 IFNULL(SUM(CASE WHEN \"during\" > 0 "
				+ "		AND \"during\" <= 5 " + "		THEN 1 " + "		ELSE 0 " + "		END), "
				+ "	 0) AS \"duration5Min\", " + "	 CAST(FLOOR(IFNULL(SUM(CASE WHEN \"Emotion\" = 0 "
				+ "			THEN 1 " + "			END), " + "	 0) * 100 / SUM(CASE WHEN \"Emotion\" IS NOT NULL "
				+ "		THEN 1 " + "		END)) AS varchar(5)) AS \"angry\", "
				+ "	 CAST(FLOOR(IFNULL(SUM(CASE WHEN \"Emotion\" = 1 " + "	"
				+ "			THEN 1 " + "			END), " + "	 0) * 100 / SUM(CASE WHEN \"Emotion\" IS NOT NULL "
				+ "		THEN 1 " + "		END)) AS varchar(5)) AS \"frown\", "
				+ "	 CAST(FLOOR(IFNULL(SUM(CASE WHEN \"Emotion\" = 2 " + "			THEN 1 " + "			END), "
				+ "	 0) * 100 / SUM(CASE WHEN \"Emotion\" IS NOT NULL " + "		THEN 1 " + "		ELSE 0 "
				+ "		END)) AS varchar(5)) AS \"happy\", "
				+ "	 CAST(100 - FLOOR(IFNULL(SUM(CASE WHEN \"Emotion\" = 0 " + "			THEN 1 "
				+ "			END), " + "	 0) * 100 / SUM(CASE WHEN \"Emotion\" IS NOT NULL " + "		THEN 1 "
				+ "		END)) - FLOOR(IFNULL(SUM(CASE WHEN \"Emotion\" = 1 " + "	 "
				+ "			THEN 1 " + "			END), " + "	 0) * 100 / SUM(CASE WHEN \"Emotion\" IS NOT NULL "
				+ "		THEN 1 " + "		END)) - FLOOR(IFNULL(SUM(CASE WHEN \"Emotion\" = 3 " + "			THEN 1 "
				+ "			END), " + "	 0) * 100 / SUM(CASE WHEN \"Emotion\" IS NOT NULL " + "		THEN 1 "
				+ "		END)) AS varchar(5)) AS \"neutral\", " + "	 IFNULL(SUM(CASE WHEN \"during\" > 5 "
				+ "		AND \"during\" <= 15 " + "		THEN 1 " + "		ELSE 0 " + "		END), "
				+ "	 0) AS \"duration15Min\", " + "	 IFNULL(SUM(CASE WHEN \"during\" > 15 " + "		THEN 1 "
				+ "		ELSE 0 " + "		END), " + "	 0) AS \"durationMore\" " + "FROM (SELECT "
				+ "	 SECONDS_BETWEEN(\"EnterTime\", " + "	 \"LeaveTime\") / 60 AS \"during\", " + "	 * "
				+ "	FROM \"VisitRecord\" " + "	WHERE HOUR(\"EnterTime\") >= 9 "
				+ "	AND \"EnterTime\" < UTCTOLOCAL (CURRENT_UTCTIMESTAMP, 'UTC+8' ) "
				+ "	AND DAYS_BETWEEN(\"EnterTime\", " + "	?1 " + "	) = 0) AS A ";
		Query query = this.entityManager.createNativeQuery(sqlstr, GeneralVistInfor.class);
		query.setHint(QueryHints.HINT_CACHEABLE, "false");

		query.setParameter(1, date);
		List<GeneralVistInfor> results = query.getResultList();
		if (results != null && results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	public List<TimeVisitInfor> GetTimeVisitInforByDate(String date) {
		String sqlstr = ""
				+ "SELECT HOUR(\"EnterTime\") AS \"time\", CAST(CAST(\"EnterTime\" AS date) AS varchar(20)) AS \"daytime\", COUNT(*) AS \"number\" "
				+ "FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 AND \"EnterTime\" < UTCTOLOCAL (CURRENT_UTCTIMESTAMP, 'UTC+8' ) AND DAYS_BETWEEN(\"EnterTime\", ?1) = 0 "
				+ "GROUP BY HOUR(\"EnterTime\"), CAST(CAST(\"EnterTime\" AS date) AS varchar(20)) ORDER BY \"time\";";
		Query query = this.entityManager.createNativeQuery(sqlstr, TimeVisitInfor.class);
		query.setHint(QueryHints.HINT_CACHEABLE, "false");
		query.setParameter(1, date);
		List results = query.getResultList();
		return results;
	}

	public List<AgeRangeInfor> GetAgeInforByDate(String date) {
		String sqlstr = ""
				+ "SELECT (\"Age\" / 10) * 10 AS \"age\", SUM(\"Gender\") AS \"maleNumber\", SUM(ABS(\"Gender\" - 1)) "
				+ "AS \"femaleNumber\" FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 AND \"EnterTime\" < UTCTOLOCAL (CURRENT_UTCTIMESTAMP, 'UTC+8' ) AND DAYS_BETWEEN(\"EnterTime\", "
				+ " ?1) = 0 GROUP BY \"Age\" / 10 ORDER BY \"Age\" / 10;";
		Query query = this.entityManager.createNativeQuery(sqlstr, AgeRangeInfor.class);
		query.setHint(QueryHints.HINT_CACHEABLE, "false");
		query.setParameter(1, date);
		List results = query.getResultList();
		return results;
	}

	public GeneralVistInfor GetGeneralInforByWeek(String begin, String end) {
		if (end == null || end.length() == 0) {
			return null;
		}
		String sqlstr = ""
				+ "SELECT COUNT(*) AS \"totalNumber\", 'Week ' || CAST(MAX(\"EnterTime\") AS varchar) AS \"readLastTime\", "
				+ "CAST(FLOOR(SUM(\"Gender\") * 100 / COUNT(*)) AS varchar(5)) || '%' AS \"malePercentage\", "
				+ "CAST(100 - FLOOR(SUM(\"Gender\") * 100 / COUNT(*)) AS varchar(5)) || '%' AS \"femalePercentage\", "
				+ "IFNULL(SUM(CASE WHEN \"during\" = 0 THEN 1 ELSE 0 END), 0) AS \"duration1Min\", "
				+ "IFNULL(SUM(CASE WHEN \"during\" > 0 AND \"during\" <= 5 THEN 1 ELSE 0 END), 0) AS \"duration5Min\", "
				+ "CAST(FLOOR(IFNULL(SUM(CASE WHEN \"Emotion\" = 0 THEN 1 END), 0) * 100 / SUM(CASE WHEN \"Emotion\" IS NOT NULL THEN 1 END)) AS varchar(5)) AS \"happy\", "
				+ "CAST(FLOOR(IFNULL(SUM(CASE WHEN \"Emotion\" = 1 OR \"Emotion\" = 2 THEN 1 END), 0) * 100 / SUM(CASE WHEN \"Emotion\" IS NOT NULL THEN 1 END)) AS varchar(5)) AS \"frown\", "
				+ "CAST(FLOOR(IFNULL(SUM(CASE WHEN \"Emotion\" = 3 THEN 1 END), 0) * 100 / SUM(CASE WHEN \"Emotion\" IS NOT NULL THEN 1 END)) AS varchar(5)) AS \"angry\", "
				+ "CAST(100 - FLOOR(IFNULL(SUM(CASE WHEN \"Emotion\" = 0 THEN 1 END), 0) * 100 / SUM(CASE WHEN \"Emotion\" IS NOT NULL THEN 1 END)) - FLOOR(IFNULL(SUM(CASE WHEN \"Emotion\" = 1 OR \"Emotion\" = 2 THEN 1 END), 0) * 100 / SUM(CASE WHEN \"Emotion\" IS NOT NULL THEN 1 END)) - FLOOR(IFNULL(SUM(CASE WHEN \"Emotion\" = 3 THEN 1 END), 0) * 100 / SUM(CASE WHEN \"Emotion\" IS NOT NULL THEN 1 END)) AS varchar(5)) AS \"neutral\", IFNULL(SUM(CASE WHEN \"during\" > 5 AND \"during\" <= 15 THEN 1 ELSE 0 END), 0) AS \"duration15Min\", "
				+ "IFNULL(SUM(CASE WHEN \"during\" > 15 THEN 1 ELSE 0 END), 0) AS \"durationMore\" FROM (SELECT SECONDS_BETWEEN(\"EnterTime\", \"LeaveTime\") / 60 AS \"during\", * "
				+ "FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 AND \"EnterTime\" < UTCTOLOCAL (CURRENT_UTCTIMESTAMP, 'UTC+8' ) AND \"EnterTime\" > ?1 AND \"EnterTime\" < ?2 || ' 23:59:59') AS A;";
		Query query = this.entityManager.createNativeQuery(sqlstr, GeneralVistInfor.class);

		query.setHint(QueryHints.HINT_CACHEABLE, "false");
		query.setParameter(1, begin);
		query.setParameter(2, end);
		List<GeneralVistInfor> results = query.getResultList();
		if (results != null && results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	public List<TimeVisitInfor> GetTimeVisitInforByWeek(String begin, String end) {
		if (end == null || end.length() == 0) {
			return null;
		}
		String sqlstr = ""
				+ "SELECT HOUR(\"EnterTime\") AS \"time\", CAST(CAST(\"EnterTime\" AS date) AS varchar(20)) AS \"daytime\", COUNT(*) AS \"number\" "
				+ "FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 AND \"EnterTime\" < UTCTOLOCAL (CURRENT_UTCTIMESTAMP, 'UTC+8' ) AND \"EnterTime\" > ?1 AND \"EnterTime\" < ?2 || ' 23:59:59' "
				+ "GROUP BY HOUR(\"EnterTime\"), CAST(CAST(\"EnterTime\" AS date) AS varchar(20)) ORDER BY \"time\",\"daytime\" ;";
		Query query = this.entityManager.createNativeQuery(sqlstr, TimeVisitInfor.class);

		query.setHint(QueryHints.HINT_CACHEABLE, "false");
		query.setParameter(1, begin);
		query.setParameter(2, end);
		List results = query.getResultList();
		return results;
	}

	public List<AgeRangeInfor> GetAgeInforByWeek(String begin, String end) {
		if (end == null || end.length() == 0) {
			return null;
		}
		String sqlstr = ""
				+ "SELECT (\"Age\" / 10) * 10 AS \"age\", SUM(\"Gender\") AS \"maleNumber\", SUM(ABS(\"Gender\" - 1)) AS \"femaleNumber\" "
				+ "FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 AND \"EnterTime\" < UTCTOLOCAL (CURRENT_UTCTIMESTAMP, 'UTC+8' ) AND \"EnterTime\" > ?1 AND \"EnterTime\" < ?2 || ' 23:59:59' "
				+ "GROUP BY \"Age\" / 10 ORDER BY \"Age\" / 10;";
		Query query = this.entityManager.createNativeQuery(sqlstr, AgeRangeInfor.class);
		query.setHint(QueryHints.HINT_CACHEABLE, "false");
		query.setParameter(1, begin);
		query.setParameter(2, end);
		List results = query.getResultList();
		return results;
	}

	public List<VisitImages> GetVistiImagesInRoom() {
		String sqlstr = ""
				+ "SELECT A.\"FaceID\" as \"faceId\", B.\"PicId\" as \"picId\", IFNULL(C.\"IsVip\", 0) AS \"isVip\", IFNULL(C.\"IsCelebrity\", 0) AS \"isCelebrity\", 1 AS \"visitCount\", "
				+ "C.\"Name\" as \"name\", 'VIP' AS \"vipType\", IFNULL(C.\"Currency\", 0) AS \"balance\", IFNULL(C.\"Currency\", 0) AS \"spent\", 6 AS \"visitCountIn90\" ,C.\"RecentPurchased\" as \"recentPurchased\", C.\"Recommand\" as \"recommand\" "
				+ "FROM (SELECT TOP 20 \"FaceID\", MAX(ID) AS \"ID\" FROM \"VisitRecord\" g WHERE HOUR(\"EnterTime\") >= 9 AND \"EnterTime\" < UTCTOLOCAL (CURRENT_UTCTIMESTAMP, 'UTC+8' ) "
				+ "AND DAYS_BETWEEN(\"EnterTime\", UTCTOLOCAL (CURRENT_UTCTIMESTAMP, 'UTC+8' )) = 0 GROUP BY \"FaceID\" ORDER BY ID DESC) AS A INNER JOIN \"VisitRecord\" B ON A.ID = B.ID "
				+ "AND B.\"LeaveTime\" IS NULL AND \"PicId\" IS NOT NULL LEFT OUTER JOIN \"CustomerInfor\" C ON B.\"FaceID\" = C.\"FaceID\";";
		Query query = this.entityManager.createNativeQuery(sqlstr, VisitImages.class);
		query.setHint(QueryHints.HINT_CACHEABLE, "false");
		List results = query.getResultList();
		return results;
	}

	public void GenerateMockData() {
		StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("GENEARATEMOCKDATA");
		query.registerStoredProcedureParameter("UntilCurrentTime", Integer.class, ParameterMode.IN);
		query.setParameter("UntilCurrentTime", 0);
		query.execute();
	}
	
	//To be corrected.

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
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
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
				.createNativeQuery("select Datepart(HOUR,EnterTime) time,CAST( convert(date,EnterTime) AS varchar(20)) daytime,Emotion,COUNT(Emotion) value from VisitRecord \n" + 
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
				.createNativeQuery("select Datepart(HOUR,EnterTime) time,CAST( convert(date,EnterTime) AS varchar(20)) daytime,Emotion,COUNT(Emotion) value from VisitRecord \n" + 
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
