package com.practice.waitingqueue.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class WaitingItemSyncCommand {
    private final long itemId;                       // 상품 고유번호
    private final String itemName;                   // 상품 이름
    private final int remainCount;                   // 재고수량
    private final boolean waitingQueueEnabled;       // 대기열 사용이 활성화 되었는 지 여부 (true: 활성화, false: 비활성화)
}
