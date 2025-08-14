package com.practice.waitingqueue.infra.redis.repository;

import static com.practice.waitingqueue.infra.redis.repository.WaitingQueueKeyGenerator.WAITING_QUEUE_KEY_PREFIX;

import com.practice.waitingqueue.domain.entity.WaitingQueue;
import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import com.practice.waitingqueue.domain.repository.WaitingQueueRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
@RequiredArgsConstructor
public class WaitingQueueRedisRepository implements WaitingQueueRepository {

    public static String WAITING_QUEUE_KEY_FIND_REGEX = WAITING_QUEUE_KEY_PREFIX + "*";

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public WaitingQueueToken save(long itemId, WaitingQueueToken waitingQueueToken, long score) {
        final var waitingQueueKey = WaitingQueueKeyGenerator.generate(itemId);
        redisTemplate.opsForZSet().add(waitingQueueKey, waitingQueueToken.getValue(), score);
        return waitingQueueToken;
    }

    @Override
    public Optional<WaitingQueue> findByItemIdAndWaitingQueueToken(
        long itemId,
        WaitingQueueToken waitingQueueToken
    ) {
        final var waitingQueueKey = WaitingQueueKeyGenerator.generate(itemId);
        final var rank = redisTemplate.opsForZSet().rank(waitingQueueKey, waitingQueueToken.getValue());

        if (rank == null) {
            return Optional.empty();
        } else {
            return Optional.of(
                WaitingQueue.of(itemId, waitingQueueToken, rank.intValue())
            );
        }
    }

    public Set<Long> findAllQueueingItemIdList() {
        final var keys = redisTemplate.keys(WAITING_QUEUE_KEY_FIND_REGEX);

        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }

        return keys.stream()
            .map(key -> key.replace(WAITING_QUEUE_KEY_PREFIX, ""))
            .map(Long::parseLong)
            .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public List<WaitingQueueToken> findTopRankedWaitingQueueTokenListByItem(long itemId, int rank) {
        final var waitingQueueKey = WaitingQueueKeyGenerator.generate(itemId);
        final var start = 0;
        final var end = rank - 1;

        final var userIdSet = redisTemplate.opsForZSet().range(waitingQueueKey, start, end);

        if (CollectionUtils.isEmpty(userIdSet)) {
            return Collections.emptyList();
        }

        return userIdSet.stream()
            .map(WaitingQueueToken::createWithoutValidation)
            .toList();
    }

    public void deleteWaitingQueueTokenListByItemId(
        long itemId,
        List<WaitingQueueToken> waitingQueueTokenList
    ) {
        if (CollectionUtils.isEmpty(waitingQueueTokenList)) {
            return;
        }

        final var waitingQueueKey = WaitingQueueKeyGenerator.generate(itemId);
        final var rawTokenSet = waitingQueueTokenList.stream()
            .map(WaitingQueueToken::getValue)
            .collect(Collectors.toUnmodifiableSet());

        redisTemplate.opsForZSet().remove(waitingQueueKey, rawTokenSet.toArray());
    }
}
