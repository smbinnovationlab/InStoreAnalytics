package com.sap.innolabs.shopanalystic.main;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/swagger-ui/**").addResourceLocations("/WEB-INF/public/swagger-ui/dist/");
		//registry.addResourceHandler("/images/**").addResourceLocations("file:C:\\Development\\SourceCode\\FaceRegService\\Output\\Debug\\Thumb\\");
		registry.addResourceHandler("/client/**").addResourceLocations("/WEB-INF/public/static/");
	}
}