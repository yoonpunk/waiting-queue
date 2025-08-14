package com.practice.waitingqueue.domain.repository;

import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import java.util.List;

public interface EntrySetRepository {

    WaitingQueueToken save(long itemId, WaitingQueueToken waitingQueueToken);

    List<WaitingQueueToken> saveAllByItemId(long itemId, List<WaitingQueueToken> waitingQueueTokenList);

    boolean containsToken(long itemId, WaitingQueueToken waitingQueueToken);
}
