package com.caferaters.service.dish;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.caferaters.model.Dish;
import com.caferaters.repository.dish.DishRepository;
import com.caferaters.repository.entity.CafeEntity;
import com.caferaters.repository.entity.DishEntity;

@Service
public class DishServiceImpl implements DishService {

	private static final Logger logger = LoggerFactory.getLogger(DishServiceImpl.class);

	@Autowired
	@Qualifier("dishRepository")
	private DishRepository dishRepository;

	@Autowired
	private DishAdapter dishAdapter;

	@Override
	public Dish createDish(final Dish dish) {
		final DishEntity dishEntity = dishAdapter.toEntity(dish, "id");
		
		final CafeEntity cafeEntity = new CafeEntity();
		cafeEntity.setId(dish.getCafeId());
		
		dishEntity.setCafe(cafeEntity);
		final Long id = dishRepository.save(dishEntity);
		
		dish.setId(id);
		
		return dish;
	}

	@Override
	public void deleteDish(final long id) {
		final DishEntity dishEntity = new DishEntity();
		dishEntity.setId(id);
		dishRepository.delete(dishEntity);
	}

	@Override
	public Dish updateDish(final Dish dish) {
		final DishEntity dishEntity = dishAdapter.toEntity(dish);
		final CafeEntity cafeEntity = new CafeEntity();
		cafeEntity.setId(dish.getCafe().getId());
		dishEntity.setCafe(cafeEntity);
		dishRepository.update(dishEntity);
		
		return dishAdapter.toModel(dishEntity);
	}

	@Override
	public Dish getDish(final long id) {
		final DishEntity dishEntity = dishRepository.findById(id);

		if (dishEntity == null) {
			return null;
		}
		
		final Dish dish = dishAdapter.toModel(dishEntity); 
		return dish;
	}

	@Override
	public Collection<Dish> getAllCafeDishes(final long cafeId) {
		final List<DishEntity> dishEntities = dishRepository.findByCafeId(cafeId);
		final Collection<Dish> dishes = dishAdapter.toModels(dishEntities);

		fillCafeId(dishes, cafeId);

		return dishes;
	}

	@Override
	public boolean isDishExists(final Dish dish) {
		final DishEntity dishEntity = dishAdapter.toEntity(dish);
		final List<DishEntity> dishes = dishRepository.findByExample(dishEntity);
		return !dishes.isEmpty();
	}

	@Override
	public Collection<Dish> getCafeMenu(final long cafeId) {
		final List<DishEntity> dishEntities = dishRepository.findByCafeIdAndCreatedDate(cafeId, new Date());
		final Collection<Dish> dishes = dishAdapter.toModels(dishEntities);

		fillCafeId(dishes, cafeId);

		return dishes;
	}

	private void fillCafeId(Collection<Dish> dishes, long cafeId) {
		for (Dish dish : dishes) {
			dish.setCafeId(cafeId);
		}
	}
}