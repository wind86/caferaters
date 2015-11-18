package com.caferaters.controller.util;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;

import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import com.jayway.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

public class TestUtils {
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
			MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	public static MockMvcRequestSpecification givenWithAuthorization() {		
		return given().auth().principalWithCredentials("user","password"); // temporary hardcoded. just for demo
	}
	
	public static MockMvcResponse sendCreateRequest(String url, Object body) {
		return givenWithAuthorization().
				body(body).
				contentType(MediaType.APPLICATION_JSON_VALUE).
				header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).
				post(url);
	}
	
	public static MockMvcResponse sendDeleteRequest(String url) {
		return givenWithAuthorization().delete(url);			
	}
	
	public static MockMvcResponse sendUpdateRequest(String url, Object body) {
		return givenWithAuthorization().
				body(body).
				contentType(MediaType.APPLICATION_JSON_VALUE).
				header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).
				put(url);
	}

	public static Long extractIdFromUrl(String url) {
		String[] path = StringUtils.split(url, "/");
		return Long.valueOf(path[path.length - 1]);		
	}
}
