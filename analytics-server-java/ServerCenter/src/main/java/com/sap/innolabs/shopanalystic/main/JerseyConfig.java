package com.sap.innolabs.shopanalystic.main;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;

@Configuration
@ApplicationPath("/app")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig(@Context ServletContext servletContext) {
        //register(ShopReportImp.class);
    	 register(CORSResponseFilter.class);
    	 register(MultiPartFeature.class);

    	 register(com.sap.innolabs.shopanalystic.imp.CameraImp.class);
    	 register(com.sap.innolabs.shopanalystic.imp.ShopReportImp.class);
        //this.packages("com.sap.innolabs.shopanalystic.imp");
        //System.out.println("ResourceConfig - JerseyConfig");
        configureSwagger(servletContext.getContextPath());
    } 
    
    private void configureSwagger(String path) {
    	
    	register(ApiListingResource.class);
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("1.0.2");
		beanConfig.setSchemes(new String[] { "http" });
//		beanConfig.setHost("localhost:8080");
//		beanConfig.setBasePath("/app");
		beanConfig.setBasePath(path+"/app");
		beanConfig.setTitle("Shop Analystic Report");
		beanConfig.getSwagger().addConsumes(MediaType.APPLICATION_JSON);
		beanConfig.getSwagger().addProduces(MediaType.APPLICATION_JSON);
		beanConfig.setContact("Martin Du");
		beanConfig.setResourcePackage("com.sap.innolabs.shopanalystic.imp");
		beanConfig.setPrettyPrint(false);
		beanConfig.setScan();
	}
}