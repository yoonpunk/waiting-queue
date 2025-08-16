package com.practice.waitingqueue.presentation.dto;

import com.practice.waitingqueue.domain.dto.WaitingItemSyncCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Schema(description = "대기열 대사 상품 정보 생성/변경 요청 DTO")
public class WaitingItemCreateOrUpdateRequest {
    @Schema(description = "대기열 대상 상품 고유번호", example = "12345")
    private final long itemId;

    @Schema(description = "대기열 대상 상품 이름", example = "아이폰16 256G 블랙")
    private final String itemName;

    @Schema(description = "대기열 대상 상품 잔여 재고 수량", example = "10")
    private int remainCount;

    @Schema(description = "대기열 대상 상품의 대기열 사용 활성화 여부 (true: 활성화, false: 비활성화)", example = "12345")
    private boolean waitingQueueEnabled;

    public WaitingItemSyncCommand toWaitingItemSyncCommand(long itemId) {
        return WaitingItemSyncCommand.of(
            itemId,
            itemName,
            remainCount,
            waitingQueueEnabled
        );
    }
}
