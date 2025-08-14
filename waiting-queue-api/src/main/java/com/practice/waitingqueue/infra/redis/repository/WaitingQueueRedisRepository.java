package com.practice.waitingqueue.infra.redis.repository;

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

    /**
     * 현재 활성화 된 대기열의 아이템 ID를 저장하는 Redis 키입니다. 대기열이 최초 생성될 경우 이 키에 아이템 ID가 추가됩니다.
     * 레디스에서 제공하는 KEYS 명령어는 블로킹이며, SCAN 명령어는 논블로킹이지만 성능에 영향을 줄 수 있기에 활성화 된 대기열의 아이템 ID를 별도로 관리합니다.
     * 대기열->입장셋 토큰 이동 스케쥴러에 의해 대기열의 토큰이 모두 제거될 수 있지만 그렇더라도 해당 키에서 아이템 ID를 제거하지 않습니다.
     * 해당 대기열의 아이템 ID 수를 조회하여 0일 때 제거할 수 있지만, 그때 새로 토큰이 진입할 수 있어 동시성 제어에 어려울 수 있기 떄문입니다.
     * 따라서, 한 번 활성화 된 대기열의 아이템 ID는 계속해서 이 키에 계속 남아있도록 합니다.
     */
    private static final String ACTIVE_WAITING_QUEUES_KEY = "active-waiting-queues";

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public WaitingQueueToken save(long itemId, WaitingQueueToken waitingQueueToken, long score) {
        final var waitingQueueKey = WaitingQueueKeyGenerator.generate(itemId);

        redisTemplate.opsForSet().add(ACTIVE_WAITING_QUEUES_KEY, String.valueOf(itemId));
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
        final var activeQueueItemIdSet = redisTemplate.opsForSet().members(ACTIVE_WAITING_QUEUES_KEY);

        if (CollectionUtils.isEmpty(activeQueueItemIdSet)) {
            return Collections.emptySet();
        }

        return activeQueueItemIdSet.stream()
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
