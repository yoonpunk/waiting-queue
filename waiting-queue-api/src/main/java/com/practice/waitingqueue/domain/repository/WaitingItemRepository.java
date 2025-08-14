package com.practice.waitingqueue.domain.repository;

import com.practice.waitingqueue.domain.entity.WaitingItem;
import java.util.Optional;

public interface WaitingItemRepository {

    WaitingItem save(WaitingItem waitingItem);

    Optional<WaitingItem> findByItemId(long itemId);
}
