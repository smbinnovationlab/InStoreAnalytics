package com.sap.innolabs.shopanalystic.api;


import java.io.InputStream;
import java.util.List;



import com.sap.innolabs.shopanalystic.model.CustomerInfor;
import com.sap.innolabs.shopanalystic.model.CustomerSummaryInfor;
import com.sap.innolabs.shopanalystic.model.ProductInfor;
import com.sap.innolabs.shopanalystic.model.VisitRecord;


public interface Camera {
	public void AddVisitRecord(VisitRecord record);

	public void UpdateVistRecord(VisitRecord record);

	public CustomerInfor GetCustomerInfor(String name);

	public void UpdateCustomerInfor(CustomerInfor record);

	public void DeleteCustomerInfor(Integer faceId);
	
	public void DeleteAllCustomerInfor();
	
	public void FocusCustomer(String faceID, VisitRecord record);
	
	public String GetFocusCustomer();
	
	public CustomerSummaryInfor GetCustomerSummaryInforamtion(String faceID);
	
	public String AddProductImage(InputStream fileInputStream);//
	
	public void AddProduct(ProductInfor product);
	
	public List<ProductInfor> GetRecommandProducts(int age,int gender,int emotion); 
	
	public List<ProductInfor> GetAllProducts();
	
	public List<CustomerInfor> GetAllCustomerInfors();
}
