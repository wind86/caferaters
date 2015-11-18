package com.caferaters.service.dish;

import java.util.Collection;

import com.caferaters.model.Dish;

public interface DishService {

	Dish createDish(Dish dish);
	void deleteDish(long id);
	Dish updateDish(Dish dish);
	Dish getDish(long id);
	Collection<Dish> getAllCafeDishes(long cafeId);
	boolean isDishExists(Dish dish);
	Collection<Dish> getCafeMenu(long cafeId);
}