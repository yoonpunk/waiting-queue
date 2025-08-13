package com.practice.waitingqueue.domain.repository;

import com.practice.waitingqueue.domain.entity.WaitingQueueToken;

public interface EntrySetRepository {

    WaitingQueueToken save(long itemId, WaitingQueueToken waitingQueueToken);

    boolean containsToken(long itemId, WaitingQueueToken waitingQueueToken);
}
