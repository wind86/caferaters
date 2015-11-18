package com.caferaters.repository.cafe;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.caferaters.repository.entity.CafeEntity;
import com.caferaters.repository.entity.VoteEntity;
import com.caferaters.model.CafeRating;
import com.caferaters.repository.AbstractCrudRepository;

@Repository("cafeRepository")
@Transactional
public class CafeRepositoryImpl extends AbstractCrudRepository<CafeEntity, Long> implements CafeRepository {

	@Override
	public CafeEntity findById(final Long id) {
		return (CafeEntity) getSession().get(CafeEntity.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CafeEntity> findAll() {
		return getSession().createCriteria(CafeEntity.class).list();
	}

	@Override
	@Transactional(readOnly = true)
	public CafeEntity findByName(final String name) {
		@SuppressWarnings("unchecked")
		final List<CafeEntity> cafes = getSession()
				.createCriteria(CafeEntity.class)
				.add(Restrictions.eq("name", name))
				.list();

		return cafes.isEmpty() ? null : cafes.get(0);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<CafeRating> calculateCafeRatings() {
		@SuppressWarnings("unchecked")
		final List<CafeRating> results = getSession().createCriteria(VoteEntity.class)
				.setProjection(Projections.projectionList()
						.add(Projections.groupProperty("cafe.id"), "cafeId")
						.add(Projections.rowCount(), "rating"))
				.addOrder(Order.desc("rating"))
				.setFetchSize(20)
				.setResultTransformer(Transformers.aliasToBean(CafeRating.class)).list();

		return results;
	}
}