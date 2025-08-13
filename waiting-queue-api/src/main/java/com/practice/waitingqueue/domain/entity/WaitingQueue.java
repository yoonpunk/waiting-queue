package com.practice.waitingqueue.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class WaitingQueue {
    private long itemId;                         // 상품 고유번호
    private WaitingQueueToken waitingQueueToken; // 대기열 토큰
    private int waitingQueueRank;                // 대기열 순위
}
