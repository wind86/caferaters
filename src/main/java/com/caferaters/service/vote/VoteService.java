package com.caferaters.service.vote;

public interface VoteService {
	void voteForCafe(long cafeId, String ip) throws VoteException;
}
