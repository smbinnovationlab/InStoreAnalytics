package com.sap.innolabs.shopanalystic.imp;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sap.innolabs.shopanalystic.api.Camera;
import com.sap.innolabs.shopanalystic.model.CustomerInfor;
import com.sap.innolabs.shopanalystic.model.CustomerSummaryInfor;
import com.sap.innolabs.shopanalystic.model.VisitImages;
import com.sap.innolabs.shopanalystic.model.VisitInforSummary;
import com.sap.innolabs.shopanalystic.model.VisitRecord;
import com.sap.innolabs.shopanalystic.repository.IShopReportRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.PATCH;

@Component
@Path("/Camera")
@Api(value = "Camera")
@Produces("application/json;charset=UTF-8")
public class CameraImp implements Camera{

	
	@Autowired
	//@Qualifier("shopReportRepository_HANA")
	private IShopReportRepository rep;

	@Override
	@POST
	@Path("/VisitRecord")
	@ApiOperation(value = "Add new visit record", notes = "Add new visit record")
	public void AddVisitRecord(VisitRecord record) {
		rep.AddVisitRecord(record);
	}
	
	@Override
	@PATCH
	@Path("/VisitRecord")
	@ApiOperation(value = "Update visit record when customer leave shop", notes = "Update visit record when customer leave shop")
	public void UpdateVistRecord(VisitRecord record) {
		rep.UpdateVistRecord(record);
	}
	
	
	
	@Override
	@GET
	@Path("/CustomerInfor")
	@ApiOperation(value = "Get CustomerInfor by name", notes = "Get CustomerInfor by faceid")
	public CustomerInfor GetCustomerInfor(@ApiParam(value = "name", required = true)  
	@DefaultValue("Martin")  @QueryParam("name") String name)
	{
		return rep.GetCustomerInfor(name);
	}
	
	
	
	@Override
	@PATCH
	@Path("/CustomerInfor")
	@ApiOperation(value = "Update CustomerInfor", notes = "Update CustomerInfor")
	public void UpdateCustomerInfor(CustomerInfor record)
	{
		rep.UpdateCustomerInfor(record);
	}
	
	@Override
	@DELETE
	@Path("/CustomerInfor")
	@ApiOperation(value = "Clean CustomerInfor", notes = "Clean CustomerInfor")
	public void DeleteCustomerInfor(@ApiParam(value = "faceId", required = true)
	@DefaultValue("1")  @QueryParam("faceId") Integer faceId)
	{
		rep.DeleteCustomerInfor(faceId);
	}
	
	@Override
	@DELETE
	@Path("/CustomerInfor/all")
	@ApiOperation(value = "Clean all CustomerInfor", notes = "Clean CustomerInfor")
	public void DeleteAllCustomerInfor()
	{
		rep.DeleteAllCustomerInfor();
	}
	
	
	@POST
	@Path("/FocusCustomer/{faceID}")
	@ApiOperation(value = "Add new focus record", notes = "Add new focus record")
	public void FocusCustomer(@PathParam("faceID") String faceID, @ApiParam(required = false) VisitRecord record) {
		rep.FocusCustomer(faceID,record);
	}
	
	@GET
	@Path("/FocusCustomer")
	@Produces("text/plain")
	@ApiOperation(value = "Get current focus customer", notes = "Get current focus customer")
	public String GetFocusCustomer() {
		 return rep.GetCurrentFocusFace();
	}
	
	@Override
	@GET
	@Path("/CustomerSummaryInfor/{faceID}")
	@ApiOperation(value = "Get visist images till now", notes = "Get visist images till now")
	public CustomerSummaryInfor GetCustomerSummaryInforamtion(@PathParam("faceID") String faceID) 
	{
		return rep.GetCustomerDetailInforamtion(faceID);
	}
	
	
	
}
