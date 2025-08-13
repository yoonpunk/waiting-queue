package com.practice.waitingqueue.infra.redis.repository;

import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import com.practice.waitingqueue.domain.repository.EntrySetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EntrySetRedisRepository implements EntrySetRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public WaitingQueueToken save(long itemId, WaitingQueueToken waitingQueueToken) {
        final var entrySetKey = EntrySetKeyGenerator.generate(itemId);
        redisTemplate.opsForSet().add(entrySetKey, waitingQueueToken.getValue());

        return waitingQueueToken;
    }

    public boolean containsToken(long itemId, WaitingQueueToken waitingQueueToken) {
        final var entrySetKey = EntrySetKeyGenerator.generate(itemId);
        return redisTemplate.opsForSet().isMember(entrySetKey, waitingQueueToken.getValue());
    }
}
