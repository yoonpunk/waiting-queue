package com.practice.waitingqueue.domain.info;

import com.practice.waitingqueue.domain.entity.WaitingQueue;
import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class WaitingQueueInfo {

    private final long itemId;                         // 상품 고유번호
    private final WaitingQueueToken waitingQueueToken; // 대기열 토큰
    private final int waitingQueueRank;                // 대기열 순위
    private final boolean enteredEntrySet;             // 입장셋 진입 여부

    public static WaitingQueueInfo createEntered(long itemId, WaitingQueueToken waitingQueueToken) {
        return new WaitingQueueInfo(
            itemId,
            waitingQueueToken,
            0,
            true
        );
    }

    public static WaitingQueueInfo createWaiting(WaitingQueue waitingQueue) {
        return new WaitingQueueInfo(
            waitingQueue.getItemId(),
            waitingQueue.getWaitingQueueToken(),
            waitingQueue.getWaitingQueueRank(),
            false
        );
    }
}
