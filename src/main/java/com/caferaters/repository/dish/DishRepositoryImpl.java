package com.caferaters.repository.dish;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.caferaters.repository.AbstractCrudRepository;
import com.caferaters.repository.entity.DishEntity;

@Repository("dishRepository")
@Transactional
public class DishRepositoryImpl
		extends AbstractCrudRepository<DishEntity, Long>
		implements DishRepository {

	@Override
	@Transactional(readOnly = true)
	public DishEntity findById(final Long id) {
		return (DishEntity) getSession().get(DishEntity.class, id);
	}

	@Override
	@Transactional(readOnly = true)
	public DishEntity findByNameAndCreatedDate(final String dishName, final Date createdDate) {
		@SuppressWarnings("unchecked")
		final List<DishEntity> dishes = getSession()
				.createCriteria(DishEntity.class)
				.add(Restrictions.and(
						Restrictions.ilike("name", "%" + dishName),
						Restrictions.eq("createdDate", createdDate))
					)
				.list();

		return dishes.isEmpty() ? null : dishes.get(0);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DishEntity> findByExample(final DishEntity dishEntity) {
		final Criteria criteria = getSession().createCriteria(DishEntity.class);
		final Example example = Example.create(dishEntity).excludeZeroes();
		@SuppressWarnings("unchecked")
		final List<DishEntity> dishes = criteria.add(example).list();
		return dishes;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DishEntity> findByCafeId(final long cafeId) {
		@SuppressWarnings("unchecked")
		final List<DishEntity> dishes = getSession()
				.createCriteria(DishEntity.class)
				.add(Restrictions.eq("cafe.id", cafeId))
				.list();

		return dishes;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DishEntity> findByCafeIdAndCreatedDate(final long cafeId, final Date createdDate) {
		@SuppressWarnings("unchecked")
		final List<DishEntity> dishes = getSession()
				.createCriteria(DishEntity.class)
				.add(Restrictions.and(
						Restrictions.eq("cafe.id", cafeId),
						Restrictions.eq("createdDate", createdDate))
					)
				.list();

		return dishes;
	}

}