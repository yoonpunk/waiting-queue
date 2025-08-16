package com.practice.waitingqueue.domain.repository;

import com.practice.waitingqueue.domain.entity.WaitingQueue;
import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface WaitingQueueRepository {

    WaitingQueueToken save(long itemId, WaitingQueueToken waitingQueueToken, long score);

    Optional<WaitingQueue> findByItemIdAndWaitingQueueToken(
        long itemId,
        WaitingQueueToken waitingQueueToken
    );

    Set<Long> findAllQueueingItemIdList();

    List<WaitingQueueToken> findTopRankedWaitingQueueTokenListByItem(long itemId, int rank);

    Long countWaitingQueueTokenByItemId(long itemId);

    void deleteWaitingQueueTokenListByItemId(long itemId, List<WaitingQueueToken> waitingQueueTokenList);
}
