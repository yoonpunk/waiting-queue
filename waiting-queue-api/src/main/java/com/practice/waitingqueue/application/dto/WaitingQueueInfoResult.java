package com.practice.waitingqueue.application.dto;

import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import com.practice.waitingqueue.domain.info.WaitingQueueInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class WaitingQueueInfoResult {
    private final long itemId;                           // 상품 고유번호
    private final WaitingQueueToken waitingQueueToken;   // 대기열 등록 토큰
    private final int waitingQueueRank;                  // 대기열 순위
    private final boolean canOrder;                      // 현재 주문 가능 여부 (대기열 종료 후 true)

    public static WaitingQueueInfoResult of(WaitingQueueInfo waitingQueueInfo) {
        return new WaitingQueueInfoResult(
            waitingQueueInfo.getItemId(),
            waitingQueueInfo.getWaitingQueueToken(),
            waitingQueueInfo.getWaitingQueueRank(),
            waitingQueueInfo.isEnteredEntrySet()
        );
    }
}
