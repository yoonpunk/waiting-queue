package com.practice.waitingqueue.domain.repository;

import com.practice.waitingqueue.domain.entity.WaitingQueue;
import java.util.Optional;

public interface WaitingQueueRepository {

    void save(long itemId, String waitingQueueToken, long score);

    Optional<WaitingQueue> findByItemIdAndWaitingQueueToken(
        long itemId,
        String waitingQueueToken
    );
}
