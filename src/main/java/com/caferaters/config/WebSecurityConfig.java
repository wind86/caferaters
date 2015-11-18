package com.caferaters.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebMvcSecurity
@PropertySource(value = { "classpath:application.properties" })
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment environment;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http://g00glen00b.be/securing-your-rest-api-with-spring-security/
	    http
	      .csrf().disable()
	      .headers().frameOptions().disable()
	      .authorizeRequests()
	        .antMatchers(HttpMethod.POST, "/api/**").authenticated()
	        .antMatchers(HttpMethod.PUT, "/api/**").authenticated()
	        .antMatchers(HttpMethod.DELETE, "/api/**").authenticated()
	        // allow user to vote for cafe
	        .antMatchers(HttpMethod.POST, "/api/cafe/*/vote").permitAll()
	        .anyRequest().permitAll()
	        // allow h2 related console. only for development purpose
	        .antMatchers("/console/**").permitAll() // h2 related console
	        .and()
	      .httpBasic().and()
	      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.inMemoryAuthentication()
		.withUser(environment.getProperty("security.user.name"))
		.password(environment.getProperty("security.user.password"))
		.roles("ADMIN");
	}
}
