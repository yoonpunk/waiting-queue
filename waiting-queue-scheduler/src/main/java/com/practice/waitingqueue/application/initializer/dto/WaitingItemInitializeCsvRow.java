package com.practice.waitingqueue.application.initializer.dto;

import com.opencsv.bean.CsvBindByName;
import com.practice.waitingqueue.domain.dto.WaitingItemSyncCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WaitingItemInitializeCsvRow {
    @CsvBindByName(column = "itemId")
    private long itemId;                 // 상품 고유번호
    @CsvBindByName(column = "itemName")
    private String itemName;             // 상품 이름
    @CsvBindByName(column = "remainCount")
    private int remainCount;             // 재고수량
    @CsvBindByName(column = "waitingQueueEnabled")
    private boolean waitingQueueEnabled; // 대기열 사용이 활성화 되었는 지 여부 (true: 활성화, false: 비활성화)

    public WaitingItemSyncCommand toCommand() {
        return WaitingItemSyncCommand.of(
            itemId,
            itemName,
            remainCount,
            waitingQueueEnabled
        );
    }
}
