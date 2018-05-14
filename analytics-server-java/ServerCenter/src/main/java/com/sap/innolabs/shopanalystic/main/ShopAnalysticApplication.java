package com.sap.innolabs.shopanalystic.main;

import java.util.TimeZone;

import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.google.common.base.Predicates;



//@ComponentScan(basePackages = {"com.sap.innolabs.shopanalystic.main",
//		"com.sap.innolabs.shopanalystic.repository" ,
//		"com.sap.innolabs.shopanalystic.imp"})
@SpringBootApplication(scanBasePackages=  {"com.sap.innolabs.shopanalystic.main",
		"com.sap.innolabs.shopanalystic.repository" ,
		"com.sap.innolabs.shopanalystic.imp","com.sap.innolabs.shopanalystic.backend"})
@EntityScan("com.sap.innolabs.shopanalystic.model")
@ServletComponentScan
public class ShopAnalysticApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		System.out.println(TimeZone.getDefault().getDisplayName());
		SpringApplication.run(ShopAnalysticApplication.class, args);
	}

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		System.out.println("SpringBootServletInitializer - configure");
		
        return application.sources(ShopAnalysticApplication.class);
    }

}
