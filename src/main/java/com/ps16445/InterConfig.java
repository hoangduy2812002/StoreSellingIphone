package com.ps16445;

import com.ps16445.interceptor.SecurityInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;




@Configuration
public class InterConfig implements WebMvcConfigurer{

	
	@Autowired
	SecurityInterceptor auth;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(auth)
			.addPathPatterns("/admin/**")
			.excludePathPatterns("/sties/login");
		
	}
}