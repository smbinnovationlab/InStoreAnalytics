package com.sap.innolabs.shopanalystic.repository.imp;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.sap.innolabs.shopanalystic.model.CustomerInfor;
import com.sap.innolabs.shopanalystic.model.CustomerSummaryInfor;
import com.sap.innolabs.shopanalystic.model.VisitRecord;
import com.sap.innolabs.shopanalystic.repository.IShopReportRepository;

public abstract class BaseShopReportRepository implements IShopReportRepository {

	@PersistenceContext
	protected EntityManager entityManager;
	protected String currentFocusFace;
	
	
	@Transactional
	public void AddVisitRecord(VisitRecord record) {
		this.entityManager.persist(record);
	}

	@Transactional
	public void UpdateVistRecord(VisitRecord record) {
		VisitRecord oldrecord = this.entityManager.find(VisitRecord.class, record.getID());
		oldrecord.setLeaveTime(record.getLeaveTime());
		oldrecord.setEmotion(record.getEmotion());
		this.entityManager.merge(oldrecord);
	}

	@Transactional
	public CustomerInfor GetCustomerInfor(String name) {
		CustomerInfor cust = null;
		try {
			cust = (CustomerInfor) entityManager.createQuery("SELECT OBJECT(c) FROM CustomerInfor c WHERE c.Name=:Name")
					.setParameter("Name", name).getSingleResult();

		} catch (NoResultException exp) {

		}
		return cust;
	}

	@Transactional
	public void UpdateCustomerInfor(CustomerInfor record) {
		this.entityManager.merge(record);
	}

	@Transactional
	public void DeleteCustomerInfor(Integer faceId) {
		CustomerInfor record = this.entityManager.find(CustomerInfor.class, faceId);
		if (record != null) {
			this.entityManager.remove(record);
		}
	}
	
	@Transactional
	public void DeleteAllCustomerInfor()
	{
		Query query = entityManager.createNativeQuery("DELETE FROM \"CustomerInfor\"");
		query.executeUpdate();
	}
	
	@Transactional
	public void FocusCustomer(String faceID,VisitRecord record)
	{
		try {
			
			VisitRecord old =  entityManager.createQuery
					("SELECT c FROM "+VisitRecord.class.getName()+" c WHERE c.FaceID=:FaceID and c.EnterTime=:EnterTime",VisitRecord.class)
					.setParameter("FaceID", faceID)
					.setParameter("EnterTime", record.getEnterTime())
					.getSingleResult();
			old.setAge(record.getAge());
			old.setGender(record.getGender());
			old.setPicId(record.getPicId());
			old.setEmotion(record.getEmotion());
			this.entityManager.merge(old);
		} 
		catch (NoResultException exp) {
			this.entityManager.merge(record);
		}
		currentFocusFace = faceID;
	}
	
	public String GetCurrentFocusFace()
	{
		String temp = currentFocusFace;
		currentFocusFace =  null;
		return temp;
	}
	
	
	public CustomerSummaryInfor GetCustomerDetailInforamtion(String faceID)
	{
		CustomerSummaryInfor summary  = new CustomerSummaryInfor();
		List<VisitRecord> old =  entityManager.createQuery
				("SELECT c FROM VisitRecord c WHERE c.FaceID=:FaceID ORDER BY EnterTime DESC",VisitRecord.class)
				.setParameter("FaceID", faceID)
				.getResultList();
		summary.setVisitRecord(old);
		if(old.size()>0)
		{
			VisitRecord record = old.get(0);
			summary.setAge(record.getAge());
			summary.setRecentEmotion(record.getEmotion());
			summary.setGender(record.getGender());
			summary.setPicId(record.getPicId());
			
			DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
			
			LocalDateTime currentDate = LocalDateTime.now();
			LocalDateTime enterDate =  LocalDateTime.parse(record.getEnterTime(),formater);
			Duration duration = Duration.between(enterDate, currentDate);
		
			summary.setCurrentDuration((int)(duration.getSeconds()/60));
			if (old.size()>1)
			{
				if (old.get(1).getLeaveTime() == null)
				{
					summary.setLastDuration(5);
				}
				else
				{
					enterDate =  LocalDateTime.parse(old.get(1).getEnterTime(),formater);
					LocalDateTime leaveDate = LocalDateTime.parse(old.get(1).getLeaveTime(),formater);
					duration = Duration.between(enterDate, leaveDate);
					summary.setLastDuration((int)(duration.getSeconds()/60));
				}
			}
		}
		
		List<CustomerInfor> customers = entityManager.createQuery
				("SELECT c FROM CustomerInfor c WHERE c.FaceID=:FaceID",CustomerInfor.class)
				.setParameter("FaceID", faceID)
				.getResultList();
		
		if (customers.size()>0)
		{
			summary.setName(customers.get(0).getName());
			summary.setIsVip(1);
		}
		else
		{
			summary.setName("Anonymous");
			summary.setIsVip(0);
		}
		
		return summary;
	}
}
