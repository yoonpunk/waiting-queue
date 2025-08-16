package com.practice.waitingqueue.testdouble;

import com.practice.waitingqueue.domain.entity.WaitingQueue;
import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import com.practice.waitingqueue.domain.repository.WaitingQueueRepository;
import com.practice.waitingqueue.infra.redis.repository.WaitingQueueKeyGenerator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class FakeWaitingQueueRepository implements WaitingQueueRepository {

    private final Map<String, PriorityQueue<WaitingQueueWithScore>> waitingQueueStore = new HashMap<>();

    @Override
    public WaitingQueueToken save(long itemId, WaitingQueueToken waitingQueueToken, long score) {
        final var waitingQueueKey = WaitingQueueKeyGenerator.generate(itemId);

        waitingQueueStore.computeIfAbsent(
            waitingQueueKey,
            k -> new PriorityQueue<>(Comparator.comparingLong(entry -> entry.score))
        ).add(new WaitingQueueWithScore(itemId, waitingQueueToken, score));

        return waitingQueueToken;
    }

    @Override
    public Optional<WaitingQueue> findByItemIdAndWaitingQueueToken(
        long itemId,
        WaitingQueueToken waitingQueueToken
    ) {
        final var waitingQueueKey = WaitingQueueKeyGenerator.generate(itemId);
        final var waitingQueue = waitingQueueStore.get(waitingQueueKey);

        if (waitingQueue == null) {
            return Optional.empty();
        }

        var sortedList = waitingQueue.stream().toList();

        for (int i = 0; i < sortedList.size(); i++) {
            final var entry = sortedList.get(i);
            if (entry.waitingQueueToken.getValue().equals(waitingQueueToken.getValue())) {
                final var waitingQueueRank = i + 1;
                return Optional.of(
                    WaitingQueue.of(entry.itemId, entry.waitingQueueToken, waitingQueueRank)
                );
            }
        }

        return Optional.empty();
    }

    @Override
    public Set<Long> findAllQueueingItemIdList() {
        return waitingQueueStore.keySet().stream()
            .map(key -> key.replace(WaitingQueueKeyGenerator.WAITING_QUEUE_KEY_PREFIX, ""))
            .map(Long::parseLong)
            .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public List<WaitingQueueToken> findTopRankedWaitingQueueTokenListByItem(long itemId, int rank) {
        if (rank <= 0) {
            return List.of();
        }

        return waitingQueueStore.get(WaitingQueueKeyGenerator.generate(itemId)).stream()
            .limit(rank)
            .map(entry -> entry.waitingQueueToken)
            .toList();
    }

    @Override
    public Long countWaitingQueueTokenByItemId(long itemId) {
        final var waitingQueueKey = WaitingQueueKeyGenerator.generate(itemId);
        final var queue = waitingQueueStore.getOrDefault(waitingQueueKey, null);

        if (queue == null) {
            return 0L;
        } else {
            return (long) queue.size();
        }
    }

    @Override
    public void deleteWaitingQueueTokenListByItemId(
        long itemId,
        List<WaitingQueueToken> waitingQueueTokenList
    ) {
        if (CollectionUtils.isEmpty(waitingQueueTokenList)) {
            return;
        }

        final var waitingQueueKey = WaitingQueueKeyGenerator.generate(itemId);
        final var waitingQueue = waitingQueueStore.get(waitingQueueKey);

        if (CollectionUtils.isEmpty(waitingQueue)) {
            return;
        }

        waitingQueue.removeIf(entry ->
            waitingQueueTokenList.stream()
                .anyMatch(token -> token.getValue().equals(entry.waitingQueueToken.getValue()))
        );
    }

    // 내부 클래스로 WaitingQueue의 정보와 정렬을 위한 score 함께 저장
    private static class WaitingQueueWithScore {
        long itemId;
        WaitingQueueToken waitingQueueToken;
        long score;

        public WaitingQueueWithScore(long itemId, WaitingQueueToken waitingQueueToken, long score) {
            this.itemId = itemId;
            this.waitingQueueToken = waitingQueueToken;
            this.score = score;
        }
    }
}
