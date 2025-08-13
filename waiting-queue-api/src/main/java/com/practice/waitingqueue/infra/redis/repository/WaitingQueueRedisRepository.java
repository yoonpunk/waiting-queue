package com.practice.waitingqueue.infra.redis.repository;

import com.practice.waitingqueue.domain.entity.WaitingQueue;
import com.practice.waitingqueue.domain.repository.WaitingQueueRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WaitingQueueRedisRepository implements WaitingQueueRepository {

     private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(long itemId, String waitingQueueToken, long score) {
        final var waitingQueueKey = WaitingQueueKeyGenerator.generate(itemId);
        redisTemplate.opsForZSet().add(waitingQueueKey, waitingQueueToken, score);
    }

    @Override
    public Optional<WaitingQueue> findByItemIdAndWaitingQueueToken(
        long itemId,
        String waitingQueueToken
    ) {
        final var waitingQueueKey = WaitingQueueKeyGenerator.generate(itemId);
        final var rank = redisTemplate.opsForZSet().rank(waitingQueueKey, waitingQueueToken);

        if (rank == null) {
            return Optional.empty();
        } else {
            return Optional.of(
                WaitingQueue.of(itemId, waitingQueueToken, rank.intValue())
            );
        }
    }
}
