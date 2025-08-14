package com.practice.waitingqueue.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor(staticName = "of")
@ToString
public class WaitingItem {
    private final long itemId;                 // 상품 고유번호
    private final String itemName;             // 상품 이름
    private int remainCount;                   // 재고수량
    private boolean waitingQueueEnabled;       // 대기열 사용이 활성화 되었는 지 여부 (true: 활성화, false: 비활성화)

    public boolean cannotUseWaitingQueue() {
        return !this.waitingQueueEnabled || remainCount <= 0;
    }

    public void updateRemainCount(int remainCount) {
        this.remainCount = remainCount;
    }

    public void updateWaitingQueueEnabled(boolean needToUseWaitQueue) {
        this.waitingQueueEnabled = needToUseWaitQueue;
    }
}
