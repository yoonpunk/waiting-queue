package com.practice.waitingqueue.domain.info;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class WaitingQueueMonitoringInfo {

    private final long itemId;               // 대기열 상품 고유번호
    private final long queueingTokenCount;   // 대기열에 있는 토큰 수
}
