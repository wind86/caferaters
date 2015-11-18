package com.caferaters.service.dish;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.caferaters.adapter.AbstractModelEntityAdapter;
import com.caferaters.model.Dish;
import com.caferaters.repository.entity.DishEntity;

@Component
public class DishAdapter extends AbstractModelEntityAdapter<Dish, DishEntity> {

	@Override
	public Dish toModel(DishEntity dishEntity) {
		Dish dish = new Dish();
		BeanUtils.copyProperties(dishEntity, dish);
		return dish;
	}

	@Override
	public DishEntity toEntity(Dish dish) {
		DishEntity dishEntity = new DishEntity();
		BeanUtils.copyProperties(dish, dishEntity);
		return dishEntity;
	}

	public DishEntity toEntity(Dish dish, String... ignoreProperties) {
		DishEntity dishEntity = new DishEntity();
		BeanUtils.copyProperties(dish, dishEntity, ignoreProperties);
		return dishEntity;
	}
}
