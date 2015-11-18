package com.caferaters.config;

import org.h2.server.web.WebServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages = { "com.caferaters.controller"})
public class WebMvcConfig extends WebMvcConfigurerAdapter {

//	@Override
//	public void addViewControllers(final ViewControllerRegistry registry) {
//		registry.addViewController("/admin").setViewName("admin");
//		registry.addViewController("/").setViewName("login");
//		registry.addViewController("/hello").setViewName("hello");
//		registry.addViewController("/login").setViewName("login");
//	}
//
//	@Bean
//	public InternalResourceViewResolver viewResolver() {
//		final InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//		resolver.setPrefix("/WEB-INF/jsp/");
//		resolver.setSuffix(".jsp");
//		return resolver;
//	}

    @Bean
    public ServletRegistrationBean h2servletRegistration() {
    	//https://dzone.com/articles/using-the-h2-database-console-in-spring-boot-with
        final ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }
}