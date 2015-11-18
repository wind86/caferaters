package com.caferaters.repository.cafe;

import java.util.List;

import com.caferaters.repository.entity.CafeEntity;
import com.caferaters.model.CafeRating;
import com.caferaters.repository.CrudRepository;

public interface CafeRepository extends CrudRepository<CafeEntity, Long> {
	List<CafeEntity> findAll();

	CafeEntity findByName(String name);
	
	List<CafeRating> calculateCafeRatings();
}