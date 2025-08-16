package com.practice.waitingqueue.infra.redis.repository;

import com.practice.waitingqueue.domain.entity.WaitingItem;
import com.practice.waitingqueue.domain.repository.WaitingItemRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WaitingItemRedisRepository implements WaitingItemRepository {

    private final RedisTemplate<String, WaitingItem> redisTemplate;

    @Override
    public WaitingItem save(WaitingItem waitingItem) {
        if (waitingItem == null) {
            throw new IllegalArgumentException("waitingItem must not be null");
        }

        String key = WaitingItemKeyGenerator.generate(waitingItem.getItemId());
        redisTemplate.opsForValue().set(key, waitingItem);
        return waitingItem;
    }

    @Override
    public Optional<WaitingItem> findByItemId(long itemId) {
        String key = WaitingItemKeyGenerator.generate(itemId);
        final var waitingItem = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(waitingItem);
    }
}
