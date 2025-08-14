package com.practice.waitingqueue.domain.service;

import com.practice.waitingqueue.domain.repository.EntrySetRepository;
import com.practice.waitingqueue.domain.repository.WaitingQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoveTokenFromWaitingQueueToEntrySetService {

    private static final int DEFAULT_TOKEN_COUNT_TO_MOVE = 5;

    private final WaitingQueueRepository waitingQueueRepository;
    private final EntrySetRepository entrySetRepository;

    /**
     * 대기열에서 입장셋으로 이동할 토큰 개수
     * 기본값은 5개이며, 설정을 통해 변경할 수 있습니다.
     * 0개일 때는 대기열에서 토큰을 이동하지 않습니다. (잠시 입장열에 진입을 막는 용도로 사용)
     */
    private int tokenCountToMove = DEFAULT_TOKEN_COUNT_TO_MOVE;

    public void setTokenCountToMove(int tokenCountToMove) {
        if (tokenCountToMove >= 0) {
            this.tokenCountToMove = tokenCountToMove;
        } else {
            throw new IllegalArgumentException("토큰 이동 개수는 0이상이어야 합니다.");
        }
    }

    public int getTokenCountToMove() {
        return tokenCountToMove;
    }

    public void moveWaitingQueueTokens() {
        log.info("[MoveTokenFromWaitingQueueToEntrySetService] start moving tokens from waiting queue to entry set. tokenCountToMove={}", tokenCountToMove);
        final var queueingItemIdList = waitingQueueRepository.findAllQueueingItemIdList();

        queueingItemIdList.forEach(
            itemId -> {
                final var waitingQueueList = waitingQueueRepository.findTopRankedWaitingQueueTokenListByItem(
                    itemId,
                    tokenCountToMove
                );

                if (CollectionUtils.isEmpty(waitingQueueList)) {
                    return;
                }

                /*
                 *  대기열에서 조회한 토큰을 입장셋이 먼저 저장하고, 대기열에서 삭제합니다.
                 *  대기열에서 먼저 삭제하고 입장셋에 저장할 경우, 그 찰나의 순간에 해당 토큰을 가진 고객이 대기열 정보 조회 시 정보조회 불가로 주문서 진입이 불가능할 수 있기 때문입니다.
                 *  또한, 모종의 이유로 입장셋에 저장한 토큰을 대기열에서 삭제하지 못한 채 스케쥴러 서버가 죽을 경우에도 해당 토큰이 누락되지 않도록 하기 위함합니다.
                 *  (잠시 대기열과 입장셋에 중복으로 존재하는 것은 문제 없음)
                 */
                entrySetRepository.saveAllByItemId(itemId, waitingQueueList);
                waitingQueueRepository.deleteWaitingQueueTokenListByItemId(itemId, waitingQueueList);

                log.info("[MoveTokenFromWaitingQueueToEntrySetService] enter entry set: itemId={} tokenCount={}", itemId, waitingQueueList.size());
            }
        );
    }
}
