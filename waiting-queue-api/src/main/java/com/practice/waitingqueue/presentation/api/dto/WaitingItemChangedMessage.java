package com.practice.waitingqueue.presentation.api.dto;

import com.practice.waitingqueue.domain.dto.WaitingItemSyncCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 상품 서비스에서 상품의 상태가 변경될 때, 전달되는 이벤트의 메시지입니다.
 * 실제로는 풍부한 상품의 정보가 전달되나 여기서는 대기열에 관련된 내용만 축약하여 사용합니다.
 */
@AllArgsConstructor
@ToString
@Getter
public class WaitingItemChangedMessage {
    private final long itemId;                 // 상품 고유번호
    private final String itemName;             // 상품 이름
    private int remainCount;                   // 재고수량
    private boolean waitingQueueEnabled;       // 대기열 사용이 활성화 되었는 지 여부 (true: 활성화, false: 비활성화)
    private boolean isWaitingQueueTargetItem;  // 대기열 대상 상품인 지 여부

    public WaitingItemSyncCommand toWaitingItemSyncCommand() {
        return WaitingItemSyncCommand.of(
            itemId,
            itemName,
            remainCount,
            waitingQueueEnabled
        );
    }
}
