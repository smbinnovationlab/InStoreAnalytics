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
				+ "	 CAST(FLOOR(SUM(\"Gender\") * 100 / COUNT(*)) AS varchar(5)) || '%' AS \"femalePercentage\", "
				+ "	 CAST(100 - FLOOR(SUM(\"Gender\") * 100 / COUNT(*)) AS varchar(5)) || '%' AS \"malePercentage\", "
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
				+ "SELECT CAST((\"Age\"/ 10) AS INT) * 10 AS \"age\", SUM(\"Gender\") AS \"maleNumber\", SUM(ABS(\"Gender\" - 1)) "
				+ "AS \"femaleNumber\" FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 AND \"EnterTime\" < UTCTOLOCAL (CURRENT_UTCTIMESTAMP, 'UTC+8' ) AND DAYS_BETWEEN(\"EnterTime\", "
				+ " ?1) = 0 GROUP BY CAST((\"Age\"/ 10) AS INT) * 10 ORDER BY \"age\";";
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
				+ "CAST(FLOOR(SUM(\"Gender\") * 100 / COUNT(*)) AS varchar(5)) || '%' AS \"femalePercentage\", "
				+ "CAST(100 - FLOOR(SUM(\"Gender\") * 100 / COUNT(*)) AS varchar(5)) || '%' AS \"malePercentage\", "
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
				+ "SELECT CAST((\"Age\"/ 10) AS INT) * 10 AS \"age\", SUM(\"Gender\") AS \"maleNumber\", SUM(ABS(\"Gender\" - 1)) AS \"femaleNumber\" "
				+ "FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 AND \"EnterTime\" < UTCTOLOCAL (CURRENT_UTCTIMESTAMP, 'UTC+8' ) AND \"EnterTime\" > ?1 AND \"EnterTime\" < ?2 || ' 23:59:59' "
				+ "GROUP BY CAST((\"Age\"/ 10) AS INT) * 10 ORDER BY \"age\";";
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
		
		query = this.entityManager.createStoredProcedureQuery("GENERATEMOCKPRODUCTIONINFOR");
		query.execute();
	}
	
	//To be corrected.

	public List<RevenueInfor> GetTimeRevenueByDate(String date) {
		Query query = this.entityManager
				.createNativeQuery("select HOUR(\"EnterTime\") \"time\",CAST( CAST(\"EnterTime\" AS date) AS varchar(20)) \"daytime\", SUM(Case when \"Spent\" is null then 0 else \"Spent\" end) \"value\" from \"VisitRecord\"\r\n" + 
						"where HOUR(\"EnterTime\")>=9 and  \"EnterTime\" < UTCTOLOCAL (CURRENT_UTCTIMESTAMP, 'UTC+8' ) and DAYS_BETWEEN(\"EnterTime\",?1) = 0 \r\n" + 
						"group by HOUR(\"EnterTime\"),CAST( CAST(\"EnterTime\" AS date) AS varchar(20)) order by \"time\"" +
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
				.createNativeQuery("select HOUR(\"EnterTime\") \"time\",CAST( CAST(\"EnterTime\" AS date) AS varchar(20)) \"daytime\", SUM(Case when \"Spent\" is null then 0 else \"Spent\" end) \"value\" from \"VisitRecord\"\r\n" + 
						"where HOUR(\"EnterTime\")>=9 and  \"EnterTime\" < UTCTOLOCAL (CURRENT_UTCTIMESTAMP, 'UTC+8' ) and \"EnterTime\" > ?1 AND \"EnterTime\" < ?2 || ' 23:59:59'  \r\n" + 
						"group by HOUR(\"EnterTime\"),CAST( CAST(\"EnterTime\" AS date) AS varchar(20)) order by \"time\"", RevenueInfor.class);
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, begin);
        query.setParameter(2, end);
		List results = query.getResultList();
		return results;
	}
	
public List<BounceRate> GetBounceRateByDate(String date) {
		
		Query query = this.entityManager	
				.createNativeQuery("select CAST( CAST(\"EnterTime\" AS date) AS varchar(20))  \"daytime\",SUM(Case when \"Spent\" is null then 0 else \"Spent\" end) \"bounce\",COUNT(*) \"total\" from \"VisitRecord\"\r\n" + 
						"where HOUR(\"EnterTime\")>=9 and  \"EnterTime\" < UTCTOLOCAL (CURRENT_UTCTIMESTAMP, 'UTC+8' ) and DAYS_BETWEEN(\"EnterTime\",?1) = 0 \r\n" + 
						"group by CAST( CAST(\"EnterTime\" AS date) AS varchar(20)) order by \"daytime\"", BounceRate.class);
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, date);
		List results = query.getResultList();
		return results;
	}
	
	public List<BounceRate> GetBounceRateByWeek(String begin,String end) {
		
		Query query = this.entityManager	
				.createNativeQuery("select CAST( CAST(\"EnterTime\" AS date) AS varchar(20))  daytime,SUM(Case when \"Spent\" is null then 0 else \"Spent\" end) bounce,COUNT(*) total from \"VisitRecord\"\r\n" + 
						"where HOUR(\"EnterTime\")>=9 and  \"EnterTime\" < UTCTOLOCAL (CURRENT_UTCTIMESTAMP, 'UTC+8' ) and  \"EnterTime\" > ?1 AND \"EnterTime\" < ?2 || ' 23:59:59' \r\n" + 
						"group by CAST( CAST(\"EnterTime\" AS date) AS varchar(20)) order by \"daytime\"", BounceRate.class);
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, begin);
        query.setParameter(2, end);
		List results = query.getResultList();
		return results;
	}
	
	
	public List<ReturnRate> GetReturnRateByWeek(String begin,String end) {
		
		Query query = this.entityManager	
				.createNativeQuery("SELECT SUM(CASE WHEN \"visitcount\" > 0 THEN 1 ELSE 0 END) AS \"returnVisit\", SUM(CASE WHEN \"visitcount\" > 0 THEN 0 ELSE 1 END) AS \"newVisit\"\r\n" + 
						" FROM (SELECT SUM(CASE WHEN B.\"FaceID\" IS NULL THEN 0 ELSE 1 END) AS \"visitcount\", A.\"FaceID\" \r\n" + 
						" FROM (SELECT * FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 AND \"EnterTime\" < UTCTOLOCAL(CURRENT_UTCTIMESTAMP, 'UTC+8') AND \"EnterTime\" > ?1 AND \"EnterTime\" < ?2  || ' 23:59:59')\r\n" + 
						" AS A \r\n" + 
						" LEFT OUTER JOIN (SELECT * FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 AND \"EnterTime\" < NOW() AND \"EnterTime\" < ?1) AS B \r\n" + 
						" ON A.\"FaceID\" = B.\"FaceID\" GROUP BY A.\"FaceID\") AS T;", ReturnRate.class);
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, begin);
        query.setParameter(2, end);
		List results = query.getResultList();
		return results;
	}
	
	public List<ReturnRate> GetReturnRateByDate(String date) {
		
		Query query = this.entityManager	
				.createNativeQuery("SELECT SUM(CASE WHEN \"visitcount\" > 0 THEN 1 ELSE 0 END) AS \"returnVisit\", SUM(CASE WHEN \"visitcount\" > 0 THEN 0 ELSE 1 END) AS \"newVisit\"\r\n" + 
						" FROM (SELECT SUM(CASE WHEN B.\"FaceID\" IS NULL THEN 0 ELSE 1 END) AS \"visitcount\", A.\"FaceID\" \r\n" + 
						" FROM (SELECT * FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 AND \"EnterTime\" < UTCTOLOCAL(CURRENT_UTCTIMESTAMP, 'UTC+8') AND  DAYS_BETWEEN(\"EnterTime\",?1) = 0 )\r\n" + 
						" AS A \r\n" + 
						" LEFT OUTER JOIN (SELECT * FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 AND \"EnterTime\" < NOW() AND \"EnterTime\" < ?1) AS B \r\n" + 
						" ON A.\"FaceID\" = B.\"FaceID\" GROUP BY A.\"FaceID\") AS T;", ReturnRate.class);
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, date);
		List results = query.getResultList();
		return results;
	}
	
	public List<ReturnFrequency> GetReturnFrequencyByDate(String date) {
		
		Query query = this.entityManager	
				.createNativeQuery("SELECT SUM(CASE WHEN \"visitcount\" > 0 THEN 1 ELSE 0 END) AS \"returnVisit\", SUM(CASE WHEN \"visitcount\" > 0 THEN 0 ELSE 1 END) \r\n" + 
						"AS \"newVisit\", T.\"daytime\", T.\"time\" FROM (SELECT SUM(CASE WHEN B.\"FaceID\" IS NULL THEN 0 ELSE 1 END) \r\n" + 
						"AS \"visitcount\", A.\"FaceID\", HOUR(A.\"EnterTime\") AS \"time\", CAST(CAST(A.\"EnterTime\" AS date) AS varchar(20)) AS \"daytime\" \r\n" + 
						"FROM (SELECT * FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 AND \"EnterTime\" < UTCTOLOCAL(CURRENT_UTCTIMESTAMP, 'UTC+8') AND\r\n" + 
						"  DAYS_BETWEEN(\"EnterTime\",?1) = 0)  AS A LEFT OUTER JOIN \r\n" + 
						" (SELECT * FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 AND \"EnterTime\" < UTCTOLOCAL(CURRENT_UTCTIMESTAMP, 'UTC+8') AND \"EnterTime\" < ?1) AS B ON A.\"FaceID\" = B.\"FaceID\"\r\n" + 
						" group by A.\"FaceID\",HOUR(A.\"EnterTime\"),CAST(CAST(A.\"EnterTime\" AS date) AS varchar(20)) \r\n" + 
						" ) AS T \r\n" + 
						" group by T.\"daytime\",T.\"time\" order by \"daytime\",\"time\";", ReturnFrequency.class);
		
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, date);
		List results = query.getResultList();
		return results;
	}
	
	
	public List<ReturnFrequency> GetReturnFrequencyByWeek(String begin, String end) {
		
		Query query = this.entityManager	
				.createNativeQuery(" SELECT SUM(CASE WHEN \"visitcount\" > 0 THEN 1 ELSE 0 END) AS \"returnVisit\", SUM(CASE WHEN \"visitcount\" > 0 THEN 0 ELSE 1 END) \r\n" + 
						"AS \"newVisit\", T.\"daytime\", T.\"time\" FROM (SELECT SUM(CASE WHEN B.\"FaceID\" IS NULL THEN 0 ELSE 1 END) \r\n" + 
						"AS \"visitcount\", A.\"FaceID\", HOUR(A.\"EnterTime\") AS \"time\", CAST(CAST(A.\"EnterTime\" AS date) AS varchar(20)) AS \"daytime\" \r\n" + 
						"FROM (SELECT * FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 AND \"EnterTime\" < UTCTOLOCAL(CURRENT_UTCTIMESTAMP, 'UTC+8') AND\r\n" + 
						"   \"EnterTime\" > ?1 AND \"EnterTime\" < ?2 || ' 23:59:59')  AS A LEFT OUTER JOIN \r\n" + 
						" (SELECT * FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 AND \"EnterTime\" < UTCTOLOCAL(CURRENT_UTCTIMESTAMP, 'UTC+8') AND \"EnterTime\" < ?1) AS B ON A.\"FaceID\" = B.\"FaceID\"\r\n" + 
						" group by A.\"FaceID\",HOUR(A.\"EnterTime\"),CAST(CAST(A.\"EnterTime\" AS date) AS varchar(20)) \r\n" + 
						" ) AS T \r\n" + 
						" group by T.\"daytime\",T.\"time\" order by \"daytime\",\"time\";", ReturnFrequency.class);
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, begin);
        query.setParameter(2, end);
		List results = query.getResultList();
		return results;
	}
	
	
	public List<ConversionPipline> GetConversionPiplineByDate(String date) {
		Query query = this.entityManager
				.createNativeQuery("SELECT HOUR(\"EnterTime\") AS \"time\", CAST(CAST(\"EnterTime\" AS date) AS varchar(20)) AS \"daytime\", \r\n" + 
						"SUM(CASE WHEN \"Spent\" IS NULL OR \"Spent\" = 0 THEN 0 ELSE 1 END) AS \"value\" FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 \r\n" + 
						"AND \"EnterTime\" < UTCTOLOCAL (CURRENT_UTCTIMESTAMP, 'UTC+8' )\r\n" + 
						"AND  DAYS_BETWEEN(\"EnterTime\",?1) = 0 \r\n" + 
						"GROUP BY HOUR(\"EnterTime\"), CAST(CAST(\"EnterTime\" AS date) AS varchar(20)) ORDER BY \"time\";", ConversionPipline.class);
		
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
				.createNativeQuery("SELECT HOUR(\"EnterTime\") AS \"time\", CAST(CAST(\"EnterTime\" AS date) AS varchar(20)) AS \"daytime\", \r\n" + 
						"SUM(CASE WHEN \"Spent\" IS NULL OR \"Spent\" = 0 THEN 0 ELSE 1 END) AS \"value\" FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 \r\n" + 
						"AND \"EnterTime\" < UTCTOLOCAL (CURRENT_UTCTIMESTAMP, 'UTC+8' )\r\n" + 
						"AND  \"EnterTime\" > ?1 AND \"EnterTime\" < ?2 || ' 23:59:59'\r\n" + 
						"GROUP BY HOUR(\"EnterTime\"), CAST(CAST(\"EnterTime\" AS date) AS varchar(20)) ORDER BY \"time\";\r\n" + 
						"", ConversionPipline.class);
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, begin);
        query.setParameter(2, end);
		List results = query.getResultList();
		return results;
	}
	
	
	public List<Sentiment> GetSentimentByDate(String date) {
	
		Query query = this.entityManager	
				.createNativeQuery("SELECT HOUR(\"EnterTime\") AS \"time\", CAST(CAST(\"EnterTime\" AS date) AS varchar(20)) AS \"daytime\", \"Emotion\" as \"emotion\",\r\n" + 
						" COUNT(\"Emotion\") AS \"value\" FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 AND \"EnterTime\" < UTCTOLOCAL (CURRENT_UTCTIMESTAMP, 'UTC+8' )\r\n" + 
						" AND  DAYS_BETWEEN(\"EnterTime\",?1) = 0  AND \"Emotion\" IS NOT NULL \r\n" + 
						" GROUP BY HOUR(\"EnterTime\"), CAST(CAST(\"EnterTime\" AS date) AS varchar(20)), \"Emotion\" ORDER BY \"daytime\", \"time\";", Sentiment.class);
		
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
				.createNativeQuery("SELECT HOUR(\"EnterTime\") AS \"time\", CAST(CAST(\"EnterTime\" AS date) AS varchar(20)) AS \"daytime\", \"Emotion\" as \"emotion\",\r\n" + 
						" COUNT(\"Emotion\") AS \"value\" FROM \"VisitRecord\" WHERE HOUR(\"EnterTime\") >= 9 AND \"EnterTime\" < UTCTOLOCAL (CURRENT_UTCTIMESTAMP, 'UTC+8' )\r\n" + 
						" AND   \"EnterTime\" > ?1 AND \"EnterTime\" < ?2 || ' 23:59:59'  AND \"Emotion\" IS NOT NULL \r\n" + 
						" GROUP BY HOUR(\"EnterTime\"), CAST(CAST(\"EnterTime\" AS date) AS varchar(20)), \"Emotion\" ORDER BY \"daytime\", \"time\";", Sentiment.class);
		
        query.setHint(QueryHints.HINT_CACHEABLE, "false");
        query.setParameter(1, begin);
        query.setParameter(2, end);
		List results = query.getResultList();
		return results;
	}
	
}
