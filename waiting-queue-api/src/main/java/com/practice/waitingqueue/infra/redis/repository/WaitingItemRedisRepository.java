package com.practice.waitingqueue.infra.redis.repository;

import com.practice.waitingqueue.domain.entity.WaitingItem;
import com.practice.waitingqueue.domain.repository.WaitingItemRepository;
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

        String key = String.valueOf(waitingItem.getItemId());
        redisTemplate.opsForValue().set(key, waitingItem);
        return waitingItem;
    }

    @Override
    public WaitingItem findByItemId(long itemId) {
        String key = String.valueOf(itemId);
        return redisTemplate.opsForValue().get(key);
    }
}
