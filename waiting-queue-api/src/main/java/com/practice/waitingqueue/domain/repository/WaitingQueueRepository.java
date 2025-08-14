package com.practice.waitingqueue.domain.repository;

import com.practice.waitingqueue.domain.entity.WaitingQueue;
import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import java.util.Optional;

public interface WaitingQueueRepository {

    WaitingQueueToken save(long itemId, WaitingQueueToken waitingQueueToken, long score);

    Optional<WaitingQueue> findByItemIdAndWaitingQueueToken(
        long itemId,
        WaitingQueueToken waitingQueueToken
    );
}
