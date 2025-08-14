package com.practice.waitingqueue.testdouble;

import com.practice.waitingqueue.domain.entity.WaitingItem;
import com.practice.waitingqueue.domain.repository.WaitingItemRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeWaitingItemRepository implements WaitingItemRepository {

    private final Map<String, WaitingItem> waitingItemMap = new HashMap<>();

    @Override
    public WaitingItem save(WaitingItem waitingItem) {
        waitingItemMap.put(
            String.valueOf(waitingItem.getItemId()),
            waitingItem
        );
        return waitingItem;
    }

    @Override
    public Optional<WaitingItem> findByItemId(long itemId) {
        final var waitingItem = waitingItemMap.getOrDefault(String.valueOf(itemId), null);

        if (waitingItem == null) {
            return Optional.empty();
        } else {
            return Optional.of(waitingItem);
        }
    }
}
