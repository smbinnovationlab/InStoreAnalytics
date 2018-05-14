package com.sap.innolabs.shopanalystic.imp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sap.innolabs.shopanalystic.api.ShopReport;
import com.sap.innolabs.shopanalystic.model.AgeRangeInfor;
import com.sap.innolabs.shopanalystic.model.BounceRate;
import com.sap.innolabs.shopanalystic.model.ConversionPipline;
import com.sap.innolabs.shopanalystic.model.GeneralVistInfor;
import com.sap.innolabs.shopanalystic.model.ReturnFrequency;
import com.sap.innolabs.shopanalystic.model.ReturnRate;
import com.sap.innolabs.shopanalystic.model.RevenueInfor;
import com.sap.innolabs.shopanalystic.model.Sentiment;
import com.sap.innolabs.shopanalystic.model.TimeVisitInfor;
import com.sap.innolabs.shopanalystic.model.VisitImages;
import com.sap.innolabs.shopanalystic.model.VisitInforSummary;
import com.sap.innolabs.shopanalystic.repository.IShopReportRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Component
@Path("/ShopReport")
@Api(value = "ShopReport")
@Produces("application/json;charset=UTF-8")
public class ShopReportImp implements ShopReport {
	@Autowired
	private IShopReportRepository rep;

	@Override
	@GET
	@Path("/day")
	@ApiOperation(value = "Get visist data by date", notes = "Get visist data by date")
	public VisitInforSummary GetVisitInforByDay(
			@ApiParam(value = "Date", required = true) @DefaultValue("2017-08-28") @QueryParam("date") String date) {

		GeneralVistInfor general = rep.GetGeneralInforByDate(date);
		List<TimeVisitInfor> timevisit = rep.GetTimeVisitInforByDate(date);
		List<AgeRangeInfor> agevisit = rep.GetAgeInforByDate(date);
		List<BounceRate> bounceRate = rep.GetBounceRateByDate(date);
		List<ConversionPipline> conversionPipline = rep.GetConversionPiplineByDate(date);
		List<ReturnFrequency> returnFrequency = rep.GetReturnFrequencyByDate(date);
		List<ReturnRate> returnRate = rep.GetReturnRateByDate(date);
		List<RevenueInfor> revenueInfor = rep.GetTimeRevenueByDate(date);
		List<Sentiment> sentiment = rep.GetSentimentByDate(date);
		return new VisitInforSummary(general, timevisit, agevisit, bounceRate, conversionPipline, returnFrequency,
				returnRate, revenueInfor, sentiment);
	}

	@Override
	@GET
	@Path("/range")
	@ApiOperation(value = "Get visist data by time range", notes = "Get visist data by time range")
	public VisitInforSummary GetVisitInforByWeek(
			@ApiParam(value = "BeginTime", required = true) @DefaultValue("2017-08-23") @QueryParam("begin") String begin,
			@ApiParam(value = "EndTime", required = true) @DefaultValue("2017-08-28") @QueryParam("end") String end) {

		GeneralVistInfor general = rep.GetGeneralInforByWeek(begin, end);
		List<TimeVisitInfor> timevisit = rep.GetTimeVisitInforByWeek(begin, end);
		List<AgeRangeInfor> agevisit = rep.GetAgeInforByWeek(begin, end);
		List<BounceRate> bounceRate = rep.GetBounceRateByWeek(begin, end);
		List<ConversionPipline> conversionPipline = rep.GetConversionPiplineByWeek(begin, end);
		List<ReturnFrequency> returnFrequency = rep.GetReturnFrequencyByWeek(begin, end);
		List<ReturnRate> returnRate = rep.GetReturnRateByWeek(begin, end);
		List<RevenueInfor> revenueInfor = rep.GetTimeRevenueByWeek(begin, end);
		List<Sentiment> sentiment = rep.GetSentimentByWeek(begin, end);
		// return new
		// VisitInforSummary(general,timevisit,agevisit,null,null,null,null,null,null);
		return new VisitInforSummary(general, timevisit, agevisit, bounceRate, conversionPipline, returnFrequency,
				returnRate, revenueInfor, sentiment);

	}

	@Override
	@GET
	@Path("/mock")
	@ApiOperation(value = "Generate fake data for test purpose", notes = "Generate fake data for test purpose")
	public void GenerateFakeData() {

		rep.GenerateMockData();

	}

	@Override
	@GET
	@Path("/customer/images/room")
	@ApiOperation(value = "Get visist images till now", notes = "Get visist images till now")
	public List<VisitImages> GetVistiImagesInRoom() {

		List<VisitImages> images = rep.GetVistiImagesInRoom();

		return images;
	}

}
