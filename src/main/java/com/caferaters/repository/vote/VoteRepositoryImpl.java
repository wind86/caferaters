package com.caferaters.repository.vote;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.caferaters.repository.AbstractCrudRepository;
import com.caferaters.repository.entity.VoteEntity;

@Repository("voteRepository")
@Transactional
public class VoteRepositoryImpl
		extends AbstractCrudRepository<VoteEntity, Long>
		implements VoteRepository {

	@Override
	public void delete(VoteEntity entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Transactional(readOnly = true)
	public VoteEntity findById(final Long id) {
		return (VoteEntity) getSession().get(VoteEntity.class, id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<VoteEntity> findVotesPerformedTodayByIp(final String ip) {
		// today midnight
		final Calendar calendar = new GregorianCalendar();
		// reset hour, minutes, seconds and millis
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		final Timestamp midnightTimestamp = new Timestamp(calendar.getTime().getTime());

		@SuppressWarnings("unchecked")
		final List<VoteEntity> votes = getSession()
				.createCriteria(VoteEntity.class)
				.add(Restrictions.and(
						Restrictions.ge("createdTimepoint", midnightTimestamp),
						Restrictions.eq("ip", ip))
					)
				.list();

		return votes;
	}
}
