package com.caferaters.repository.vote;

import java.util.List;

import com.caferaters.repository.CrudRepository;
import com.caferaters.repository.entity.VoteEntity;

public interface VoteRepository extends CrudRepository<VoteEntity, Long> {

	List<VoteEntity> findVotesPerformedTodayByIp(String ip);
}