package com.practice.waitingqueue.testdouble;

import com.practice.waitingqueue.domain.entity.WaitingQueue;
import com.practice.waitingqueue.domain.repository.WaitingQueueRepository;
import com.practice.waitingqueue.infra.redis.repository.WaitingQueueKeyGenerator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;

public class FakeWaitingQueueRepository implements WaitingQueueRepository {

    private final Map<String, PriorityQueue<WaitingQueueWithScore>> waitingQueueStore = new HashMap<>();

    @Override
    public void save(long itemId, String waitingQueueToken, long score) {
        final var waitingQueueKey = WaitingQueueKeyGenerator.generate(itemId);
        waitingQueueStore.computeIfAbsent(
            waitingQueueKey,
            k -> new PriorityQueue<>(Comparator.comparingLong(entry -> entry.score))
        ).add(new WaitingQueueWithScore(itemId, waitingQueueToken, score));
    }

    @Override
    public Optional<WaitingQueue> findByItemIdAndWaitingQueueToken(
        long itemId,
        String waitingQueueToken
    ) {
        final var waitingQueueKey = WaitingQueueKeyGenerator.generate(itemId);
        final var waitingQueue = waitingQueueStore.get(waitingQueueKey);

        if (waitingQueue == null) {
            return Optional.empty();
        }

        var sortedList = waitingQueue.stream().toList();

        for (int i = 0; i < sortedList.size(); i++) {
            final var entry = sortedList.get(i);
            if (entry.waitingQueueToken.equals(waitingQueueToken)) {
                final var waitingQueueRank = i + 1;
                return Optional.of(
                    WaitingQueue.of(entry.itemId, entry.waitingQueueToken, waitingQueueRank)
                );
            }
        }

        return Optional.empty();
    }

    // 내부 클래스로 WaitingQueue의 정보와 정렬을 위한 score 함께 저장
    private static class WaitingQueueWithScore {
        long itemId;
        String waitingQueueToken;
        long score;

        public WaitingQueueWithScore(long itemId, String waitingQueueToken, long score) {
            this.itemId = itemId;
            this.waitingQueueToken = waitingQueueToken;
            this.score = score;
        }
    }
}
