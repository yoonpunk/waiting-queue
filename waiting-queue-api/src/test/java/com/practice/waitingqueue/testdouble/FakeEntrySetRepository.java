package com.practice.waitingqueue.testdouble;

import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import com.practice.waitingqueue.domain.repository.EntrySetRepository;
import com.practice.waitingqueue.infra.redis.repository.EntrySetKeyGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeEntrySetRepository implements EntrySetRepository {

    private final Map<String, List<String>> waitingQueueStore = new HashMap<>();

    @Override
    public WaitingQueueToken save(long itemId, WaitingQueueToken waitingQueueToken) {
        final var entrySetKey = EntrySetKeyGenerator.generate(itemId);

        waitingQueueStore.computeIfAbsent(entrySetKey, k -> new ArrayList<>())
            .add(waitingQueueToken.getValue());

        return waitingQueueToken;
    }

    @Override
    public boolean containsToken(long itemId, WaitingQueueToken waitingQueueToken) {
        final var entrySetKey = EntrySetKeyGenerator.generate(itemId);

        final var tokens = waitingQueueStore.get(entrySetKey);

        return tokens != null && tokens.contains(waitingQueueToken.getValue());
    }
}
