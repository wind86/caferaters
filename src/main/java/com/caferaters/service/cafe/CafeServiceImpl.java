package com.caferaters.service.cafe;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.caferaters.model.Cafe;
import com.caferaters.model.CafeRating;
import com.caferaters.repository.cafe.CafeRepository;
import com.caferaters.repository.entity.CafeEntity;

@Service
public class CafeServiceImpl implements CafeService {

	private static final Logger logger = LoggerFactory.getLogger(CafeServiceImpl.class);

	@Autowired
	@Qualifier("cafeRepository")
	private CafeRepository cafeRepository;

	@Autowired
	private CafeAdapter cafeAdapter;

	@Override
	public Cafe createCafe(final Cafe cafe) {
		if (cafe == null) {
			logger.warn("Unable to save not initilized cafe");
			return null;
		}
		
		CafeEntity cafeEntity = cafeAdapter.toEntity(cafe, "id");
		Long id = cafeRepository.save(cafeEntity);
		cafe.setId(id);

		return cafe;
	}

	@Override
	public void deleteCafe(final long id) {
		CafeEntity cafeEntity = new CafeEntity();
		cafeEntity.setId(id);
		cafeRepository.delete(cafeEntity);
	}

	@Override
	public Cafe updateCafe(final Cafe cafe) {
		cafeRepository.update(cafeAdapter.toEntity(cafe));
		return cafe;
	}

	@Override
	public Cafe getCafe(final long id) {
		CafeEntity cafeEntity = cafeRepository.findById(id);
		if (cafeEntity == null) {
			return null;
		}
		return cafeAdapter.toModel(cafeEntity);
	}

	@Override
	public Collection<Cafe> getAllCafes() {
		return cafeAdapter.toModels(cafeRepository.findAll());
	}

	@Override
	public boolean isCafeExists(Cafe cafe) {
		return cafeRepository.findByName(cafe.getName()) != null;
	}
	
	@Override
	public Collection<CafeRating> calculateCafeRating() {
		return cafeRepository.calculateCafeRatings();
	}
}
