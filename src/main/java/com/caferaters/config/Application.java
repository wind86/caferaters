package com.caferaters.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	/*
	Available urls:
		{[/api/cafe/],methods=[POST],consumes=[application/json]}
		{[/api/cafe/{cafeId}],methods=[PUT],consumes=[application/json]}
		{[/api/cafe/{cafeId}],methods=[DELETE]}
		{[/api/cafe/],methods=[GET],produces=[application/json]}
		{[/api/cafe/{cafeId}],methods=[GET],produces=[application/json]}
		{[/api/cafe/{cafeId}/dish/],methods=[GET],produces=[application/json]}
		{[/api/cafe/{cafeId}/dish/{dishId}],methods=[GET],produces=[application/json]}
		{[/api/cafe/{cafeId}/dish/],methods=[POST],consumes=[application/json]}
		{[/api/cafe/{cafeId}/dish/{dishId}],methods=[PUT],consumes=[application/json]}
		{[/api/cafe/{cafeId}/dish/{dishId}],methods=[DELETE]}
		
		{[/api/cafe/{cafeId}/menu],methods=[GET],produces=[application/json]}
		{[/api/cafe/{cafeId}/vote],methods=[POST]}
		{[/api/cafe/rating],methods=[GET]}	
		
	H2 console available via localhost:8080/console
	*/
	
    public static void main(String[] args) throws Throwable {
        SpringApplication.run(Application.class, args);
    }
}
