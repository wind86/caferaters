package com.caferaters.controller.cafe;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
//import static org.easymock.EasyMock.expect;
//import static org.easymock.EasyMock.expectLastCall;
//import static org.easymock.EasyMock.replay;
//import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.caferaters.config.Application;
import com.caferaters.controller.util.TestUtils;
import com.caferaters.model.Cafe;
import com.caferaters.model.Dish;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CafeControllerTest {

	private static final String CAFE_URL = "/api/cafe/";
	
    @Autowired
    private WebApplicationContext context;
        
    // http://www.hascode.com/2011/10/testing-restful-web-services-made-easy-using-the-rest-assured-framework/
    // https://github.com/jayway/rest-assured/blob/master/examples/rest-assured-itest-java/src/test/java/com/jayway/restassured/itest/java/JSONPostITest.java
    @Before
    public void setUp() {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context).build());
//        MockitoAnnotations.initMocks(this);
    }
    
	@Test
	public void testCreateCafe() {				
//		expect(cafeService.isCafeExists(cafe)).andReturn(false);
//		expectLastCall().times(1);
//		
//		replay(cafeService);
		
		sendCreateCafeRequest(createCafe("CafeName")).then().
		statusCode(HttpServletResponse.SC_CREATED);
		
//		verify(cafeService);
	}
	
	@Test
	public void testCreateDuplicatedCafe() {
		Cafe cafe = createCafe("CafeName2"); 
		// create and store first cafe
		sendCreateCafeRequest(cafe);
		
		// create duplicated cafe
		sendCreateCafeRequest(cafe).then().		
		statusCode(HttpServletResponse.SC_CONFLICT);
	}

	@Test
	public void testDeleteCafe() {
		Cafe cafe = createCafe("CafeName3");
		
		MockMvcResponse response = sendCreateCafeRequest(cafe);
		response.then().statusCode(HttpServletResponse.SC_CREATED);
		
		Long cafeId = extractId(response, HttpHeaders.LOCATION);
		assertNotNull(cafeId);
		
		response = sendDeleteCafeRequest(cafeId);
		response.then().statusCode(HttpServletResponse.SC_NO_CONTENT);
	}
	
	@Test
	public void testDeleteNonExistantCafe() {
		sendDeleteCafeRequest(100L).then().statusCode(HttpServletResponse.SC_NOT_FOUND);
	}
	
	@Test
	public void testGetAllCafesFound() {
		MockMvcResponse response = given().get(CAFE_URL);
		
		response.then().statusCode(HttpServletResponse.SC_OK);		
		assertFalse(response.as(List.class).isEmpty());	
	}
	
	@Test
	public void testBlankGetAllCafes() {
		MockMvcResponse response = given().get(CAFE_URL);		
		response.then().statusCode(HttpServletResponse.SC_NO_CONTENT);		
	}
	
	@Test
	public void testUpdateCafe() {
		Cafe cafe = createCafe("CafeName4");
		
		MockMvcResponse response = sendCreateCafeRequest(cafe);
		response.then().statusCode(HttpServletResponse.SC_CREATED);
		
		Long cafeId = extractId(response, HttpHeaders.LOCATION);
		assertNotNull(cafeId);
		
		cafe.setId(cafeId);
		cafe.setName(cafe.getName() + "Updated");
		
		response = sendUpdateCafeRequest(cafe);
		response.then().statusCode(HttpServletResponse.SC_OK);
	}
	
	@Test
	public void testUpdateNotFoundCafe() {
		Cafe cafe = createCafe("CafeName5");
		cafe.setId(100L);
		
		sendUpdateCafeRequest(cafe).then().statusCode(HttpServletResponse.SC_NOT_FOUND);
	}
	
	@Test
	public void testGetCafeById() {
		Cafe cafe = createCafe("CafeName6");
		
		MockMvcResponse response = sendCreateCafeRequest(cafe);
		response.then().statusCode(HttpServletResponse.SC_CREATED);
		
		Long cafeId = extractId(response, HttpHeaders.LOCATION);
		assertNotNull(cafeId);
		
		MockMvcResponse response2 = given().get(formatCafeUrlWithId(cafeId));
		response2.then().statusCode(HttpServletResponse.SC_OK);
		
		Cafe foundCafe = response2.as(Cafe.class);
		
		assertNotNull(foundCafe);
		assertEquals(cafe.getName(), foundCafe.getName());
	}
	
	@Test
	public void testGetNotExistantCafeById() {
		Cafe cafe = createCafe("CafeName7");
		cafe.setId(100L);
		
		MockMvcResponse response = given().get(formatCafeUrlWithId(cafe.getId()));
		response.then().statusCode(HttpServletResponse.SC_NOT_FOUND);
	}
	
	@Test
	public void testGetBlankCafeMenu() {
		Cafe cafe = createCafe("CafeName8");
		
		MockMvcResponse response = sendCreateCafeRequest(cafe);
		response.then().statusCode(HttpServletResponse.SC_CREATED);
		
		Long cafeId = extractId(response, HttpHeaders.LOCATION);
		assertNotNull(cafeId);
		
		String baseUrl = formatCafeUrlWithId(cafeId);
		MockMvcResponse response2 = given().get(String.format("%s/menu", baseUrl));
		
		response2.then().statusCode(HttpServletResponse.SC_NO_CONTENT);		
	}
	
	@Test
	public void testGetCafeMenu() {
		Cafe cafe = createCafe("CafeName9");

		MockMvcResponse response = sendCreateCafeRequest(cafe);
		response.then().statusCode(HttpServletResponse.SC_CREATED);
		
		Long cafeId = extractId(response, HttpHeaders.LOCATION);
		assertNotNull(cafeId);
		
		Dish dish1 = createDish(cafeId, "dish1", BigDecimal.ONE);
		Dish dish2 = createDish(cafeId, "dish2", BigDecimal.TEN);
		List<Dish> dishes = Arrays.asList(dish1, dish2); 
		
		for (Dish dish : dishes) {
			MockMvcResponse resp = sendCreateDishRequest(dish);
			resp.then().statusCode(HttpServletResponse.SC_CREATED);
		}
		
		String baseUrl = formatCafeUrlWithId(cafeId);
		MockMvcResponse response2 = given().get(String.format("%s/menu", baseUrl));
		
		response2.then().statusCode(HttpServletResponse.SC_OK);
		
		ArrayList<?> menu = response2.as(ArrayList.class);
		
		assertFalse(menu.isEmpty());
		assertEquals(dishes.size(), menu.size());
	}
	
	@Test
	public void testVoteForCafe() {
		Cafe cafe = createCafe("CafeName10");

		MockMvcResponse response = sendCreateCafeRequest(cafe);
		response.then().statusCode(HttpServletResponse.SC_CREATED);
		
		Long cafeId = extractId(response, HttpHeaders.LOCATION);
		assertNotNull(cafeId);

		String baseUrl = formatCafeUrlWithId(cafeId);
		MockMvcResponse response2 = given().post(String.format("%s/vote", baseUrl));
		
		response2.then().statusCode(HttpServletResponse.SC_CREATED);
	}
	
	@Test
	public void testDeleteDish() {
		Cafe cafe = createCafe("CafeName11");

		MockMvcResponse response = sendCreateCafeRequest(cafe);
		response.then().statusCode(HttpServletResponse.SC_CREATED);
		
		Long cafeId = extractId(response, HttpHeaders.LOCATION);
		assertNotNull(cafeId);
		
		Dish dish = createDish(cafeId, "dish123", BigDecimal.ONE);

		MockMvcResponse resp = sendCreateDishRequest(dish);
		resp.then().statusCode(HttpServletResponse.SC_CREATED);
		
		Long dishId = extractId(resp, HttpHeaders.LOCATION);
		assertNotNull(dishId);
		
		dish.setId(dishId);
		
		MockMvcResponse response2 = sendDeleteDishRequest(dish);
		response2.then().statusCode(HttpServletResponse.SC_NO_CONTENT);
	}
	
	@Test
	public void testUpdateDish() {
		Cafe cafe = createCafe("CafeName12");

		MockMvcResponse response = sendCreateCafeRequest(cafe);
		response.then().statusCode(HttpServletResponse.SC_CREATED);
		
		Long cafeId = extractId(response, HttpHeaders.LOCATION);
		assertNotNull(cafeId);
		
		Dish dish = createDish(cafeId, "dish234", BigDecimal.ONE);

		MockMvcResponse resp = sendCreateDishRequest(dish);
		resp.then().statusCode(HttpServletResponse.SC_CREATED);
		
		Long dishId = extractId(resp, HttpHeaders.LOCATION);
		assertNotNull(dishId);

		dish.setId(dishId);
		dish.setPrice(BigDecimal.TEN);
		
		MockMvcResponse response2 = sendUpdateDishRequest(dish);
		response2.then().statusCode(HttpServletResponse.SC_OK);
	}
	
	private MockMvcResponse sendCreateDishRequest(Dish dish) {
		return TestUtils.sendCreateRequest(formatCafeDishUrlWithIds(dish.getCafeId(), dish.getId()), dish);
	}
	
	private MockMvcResponse sendDeleteDishRequest(Dish dish) {
		String url = formatCafeDishUrlWithIds(dish.getCafeId(), dish.getId());
		System.out.println("Deleting dish url:" + url);
		return TestUtils.sendDeleteRequest(url);
	}
	
	private MockMvcResponse sendUpdateDishRequest(Dish dish) {
		String url = formatCafeDishUrlWithIds(dish.getCafeId(), dish.getId());
		System.out.println("Updating dish url:" + url);
		return TestUtils.sendUpdateRequest(formatCafeDishUrlWithIds(dish.getCafeId(), dish.getId()), dish);
	}
	
	private MockMvcResponse sendCreateCafeRequest(Cafe cafe) {
		return TestUtils.sendCreateRequest(CAFE_URL, cafe);
	}

	private MockMvcResponse sendDeleteCafeRequest(Long id) {
		return TestUtils.sendDeleteRequest(formatCafeUrlWithId(id));
	}
	
	private MockMvcResponse sendUpdateCafeRequest(Cafe cafe) {
		return TestUtils.sendUpdateRequest(formatCafeUrlWithId(cafe.getId()), cafe);
	}
	
	private String formatCafeUrlWithId(Long id) {
		return String.format("%s%d", CAFE_URL, id);
	}
	
	private String formatCafeDishUrlWithIds(Long cafeId, Long dishId) {
		String baseUrl = formatCafeUrlWithId(cafeId);
		
		if (dishId == null) {
			return String.format("%s/dish/", baseUrl);
		}
		return String.format("%s/dish/%d", baseUrl, dishId);
	}
	
	private Long extractId(MockMvcResponse response, String headerName) {
		String location =  response.getHeader(headerName);		
		return TestUtils.extractIdFromUrl(location);
	}
	
	private Cafe createCafe(String cafeName) {
		Cafe cafe = new Cafe();
		cafe.setName(cafeName);
		return cafe;
	}
	
	private Dish createDish(Long cafeId, String dishName, BigDecimal price) {
		Cafe cafe = new Cafe();
		cafe.setId(cafeId);
		
		Dish dish = new Dish();
		dish.setCafe(cafe);
		dish.setCafeId(cafeId);
		dish.setName(dishName);
		dish.setPrice(price);
		dish.setCreatedDate(new Date());
		
		return dish;
	}
}
