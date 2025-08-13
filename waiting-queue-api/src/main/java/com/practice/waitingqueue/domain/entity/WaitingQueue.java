package com.practice.waitingqueue.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class WaitingQueue {
    private long itemId;              // 상품 고유번호
    private String waitingQueueToken; // 대기열 고유번호
    private int waitingQueueRank;     // 대기열 순위
}
