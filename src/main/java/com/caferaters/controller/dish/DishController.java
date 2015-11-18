package com.caferaters.controller.dish;

import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.caferaters.model.Cafe;
import com.caferaters.model.Dish;
import com.caferaters.service.dish.DishService;

@RestController
@RequestMapping("/api/cafe/{cafeId}/dish")
public class DishController {

	private static final Logger logger = LoggerFactory.getLogger(DishController.class);

	@Autowired
	private DishService dishService;

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Dish>> getAllCafeDishes(@PathVariable("cafeId") long cafeId) {
		logger.info("Loading all dishes for cafe {}", cafeId);

        Collection<Dish> menu = dishService.getAllCafeDishes(cafeId);
        if (menu.isEmpty()) {
        	logger.warn("No dishes found for cafe {}", cafeId);
            return new ResponseEntity<Collection<Dish>>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Collection<Dish>>(menu, HttpStatus.OK);
	}

	@RequestMapping(value = "/{dishId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Dish> getDish(@PathVariable("cafeId") long cafeId, @PathVariable("dishId") long dishId) {
		logger.info("Fetching dish with id {} for cafe {}", dishId, cafeId);

		Dish dish = dishService.getDish(dishId);
		if (dish == null) {
			logger.warn("Dish with id {} for cafe {} not found", dishId, cafeId);
			return new ResponseEntity<Dish>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Dish>(dish, HttpStatus.OK);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createDish(@PathVariable("cafeId") long cafeId, @RequestBody Dish dish, UriComponentsBuilder ucBuilder) {
		logger.info("Creating dish {}", String.valueOf(dish));

		dish.setCafeId(cafeId);
		dish.setCreatedDate(new Date());

        if (dishService.isDishExists(dish)) {
        	logger.warn("Dish with name '{}' already exists", dish.getName());
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        Dish savedDish = dishService.createDish(dish);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/cafe/{cafeId}/dish/{dishId}").buildAndExpand(savedDish.getCafeId(), savedDish.getId()).toUri());

        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{dishId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> updateDish(@PathVariable("cafeId") long cafeId, @PathVariable("dishId") long dishId, @RequestBody Dish dish) {
    	logger.info("Updating dish with id {} for cafe {}", dishId, cafeId);
    	Dish existingDish = dishService.getDish(dishId);

        if (existingDish == null) {
        	logger.warn("Dish with id {} not found", dishId);
            return new ResponseEntity<Dish>(HttpStatus.NOT_FOUND);
        }

        existingDish.setName(dish.getName());
        existingDish.setPrice(dish.getPrice());
        
        Cafe cafe = new Cafe();
        cafe.setId(cafeId);
        existingDish.setCafe(cafe);

        Dish updatedDish = dishService.updateDish(existingDish);
        
        return new ResponseEntity<Dish>(updatedDish, HttpStatus.OK);
    }

    @RequestMapping(value = "/{dishId}", method = RequestMethod.DELETE)
    public ResponseEntity<Dish> deleteDish(@PathVariable("cafeId") Long cafeId, @PathVariable("dishId") Long dishId) {
    	logger.info("Deleting dish with id {} for cafe {}", dishId, cafeId);
    	Dish dish = dishService.getDish(dishId);

        if (dish == null) {
        	logger.warn("Dish with id {} not found", dishId);
            return new ResponseEntity<Dish>(HttpStatus.NOT_FOUND);
        }

        dishService.deleteDish(dishId);

        return new ResponseEntity<Dish>(HttpStatus.NO_CONTENT);
    }
}