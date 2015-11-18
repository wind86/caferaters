package com.caferaters.service.cafe;

import java.util.Collection;

import com.caferaters.model.Cafe;
import com.caferaters.model.CafeRating;

public interface CafeService {

	Cafe createCafe(Cafe cafe);
	void deleteCafe(long id);
	Cafe updateCafe(Cafe cafe);
	Cafe getCafe(long id);
	Collection<Cafe> getAllCafes();
	boolean isCafeExists(Cafe cafe);
	Collection<CafeRating> calculateCafeRating();
}
