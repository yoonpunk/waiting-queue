package com.practice.waitingqueue.infra.redis.repository;

import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import com.practice.waitingqueue.domain.repository.EntrySetRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

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
        if (CollectionUtils.isEmpty(waitingQueueTokenList)) {
            return List.of();
        }

        final var entrySetKey = EntrySetKeyGenerator.generate(itemId);
        final var rawTokenList = waitingQueueTokenList.stream()
            .map(WaitingQueueToken::getValue)
            .toArray(String[]::new);

        redisTemplate.opsForSet().add(entrySetKey, rawTokenList);
        return waitingQueueTokenList;
    }

    @Override
    public Long countEntrySetTokenByItemId(long itemId) {
        final var entrySetKey = EntrySetKeyGenerator.generate(itemId);
        return redisTemplate.opsForSet().size(entrySetKey);
    }

    @Override
    public boolean containsToken(long itemId, WaitingQueueToken waitingQueueToken) {
        final var entrySetKey = EntrySetKeyGenerator.generate(itemId);
        return redisTemplate.opsForSet().isMember(entrySetKey, waitingQueueToken.getValue());
    }
}
