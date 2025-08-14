package com.practice.waitingqueue.infra.redis.repository;

import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import com.practice.waitingqueue.domain.repository.EntrySetRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EntrySetRedisRepository implements EntrySetRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public WaitingQueueToken save(long itemId, WaitingQueueToken waitingQueueToken) {
        final var entrySetKey = EntrySetKeyGenerator.generate(itemId);
        redisTemplate.opsForSet().add(entrySetKey, waitingQueueToken.getValue());

        return waitingQueueToken;
    }

    @Override
    public List<WaitingQueueToken> saveAllByItemId(
        long itemId,
        List<WaitingQueueToken> waitingQueueTokenList
    ) {
        final var entrySetKey = EntrySetKeyGenerator.generate(itemId);

        waitingQueueTokenList.forEach(
            waitingQueueToken -> redisTemplate.opsForSet().add(
                entrySetKey,
                waitingQueueToken.getValue()
            )
        );

        return waitingQueueTokenList;
    }

    @Override
    public boolean containsToken(long itemId, WaitingQueueToken waitingQueueToken) {
        final var entrySetKey = EntrySetKeyGenerator.generate(itemId);
        return redisTemplate.opsForSet().isMember(entrySetKey, waitingQueueToken.getValue());
    }
}
