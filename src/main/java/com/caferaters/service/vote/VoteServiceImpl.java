package com.caferaters.service.vote;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.caferaters.repository.cafe.CafeRepository;
import com.caferaters.repository.entity.CafeEntity;
import com.caferaters.repository.entity.VoteEntity;
import com.caferaters.repository.vote.VoteRepository;

@Service
public class VoteServiceImpl implements VoteService {

	@Autowired
	@Qualifier("voteRepository")
	private VoteRepository voteRepository;

	@Autowired
	@Qualifier("cafeRepository")
	private CafeRepository cafeRepository;

	@Override
	public void voteForCafe(final long cafeId, final String ip) throws VoteException {
		final CafeEntity cafeEntity = cafeRepository.findById(cafeId);

		if (cafeEntity == null) {
			throw new VoteException(String.format("Unknown cafe with id %d", cafeId));
		}

		final List<VoteEntity> performedVotes = voteRepository.findVotesPerformedTodayByIp(ip);
		if (performedVotes.isEmpty()) {
			voteRepository.save(createVoteEntity(cafeEntity, ip));
			return;
		}

		if (isVoteTimeLimitExceeded()) {
			throw new VoteException("Time range for voting exceeded");
		}

		final VoteEntity existingVoteEntity = performedVotes.get(0);

		existingVoteEntity.setCafe(cafeEntity);
		existingVoteEntity.setUpdatedTimepoint(getCurrentTimestamp());

		voteRepository.update(existingVoteEntity);
	}

	private boolean isVoteTimeLimitExceeded() {
		final Calendar calendar = new GregorianCalendar();
		// reset hour, minutes, seconds and millis
		calendar.set(Calendar.HOUR_OF_DAY, 11);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		final Calendar currentCalendar = new GregorianCalendar();
		currentCalendar.setTime(new Date());

		return currentCalendar.after(calendar);
	}

	private VoteEntity createVoteEntity(CafeEntity cafeEntity, String ip) {
		final VoteEntity newVoteEntity = new VoteEntity();

		newVoteEntity.setCafe(cafeEntity);
		newVoteEntity.setIp(ip);
		newVoteEntity.setCreatedTimepoint(getCurrentTimestamp());

		return newVoteEntity;
	}

	private Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}
}
