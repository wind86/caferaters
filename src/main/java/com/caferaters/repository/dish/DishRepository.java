package com.caferaters.repository.dish;

import java.util.Date;
import java.util.List;

import com.caferaters.repository.entity.DishEntity;
import com.caferaters.repository.CrudRepository;

public interface DishRepository extends CrudRepository<DishEntity, Long> {
	DishEntity findByNameAndCreatedDate(String dishName, Date createdDate);

	List<DishEntity> findByExample(DishEntity dishEntity);

	List<DishEntity> findByCafeId(long cafeId);

	List<DishEntity> findByCafeIdAndCreatedDate(long cafeId, Date createdDate);
}