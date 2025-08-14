package com.practice.waitingqueue.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class WaitingItem {
    private final long itemId;                 // 상품 고유번호
    private final String itemName;             // 상품 이름
    private int remainCount;                   // 재고수량
    private boolean needToUseWaitQueue;        // 대기열 사용이 필요한 상품인 지 여부

    public boolean cannotUseWaitingQueue() {
        return !this.needToUseWaitQueue || remainCount <= 0;
    }
}
