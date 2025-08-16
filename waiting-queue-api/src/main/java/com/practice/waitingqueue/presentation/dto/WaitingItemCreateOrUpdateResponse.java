package com.practice.waitingqueue.presentation.dto;

import com.practice.waitingqueue.domain.entity.WaitingItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
@Schema(description = "대기열 대상 상품 정보 생성/변경 응답 DTO")
public class WaitingItemCreateOrUpdateResponse {

    @Schema(description = "대기열 대상 상품 고유번호", example = "12345")
    private final long itemId;

    @Schema(description = "대기열 대상 상품 이름", example = "아이폰16 256G 블랙")
    private final String itemName;

    @Schema(description = "대기열 대상 상품 잔여 재고 수량", example = "10")
    private int remainCount;

    @Schema(description = "대기열 대상 상품의 대기열 사용 활성화 여부 (true: 활성화, false: 비활성화)", example = "12345")
    private boolean waitingQueueEnabled;

    public static WaitingItemCreateOrUpdateResponse of(WaitingItem waitingItem) {
        return new WaitingItemCreateOrUpdateResponse(
            waitingItem.getItemId(),
            waitingItem.getItemName(),
            waitingItem.getRemainCount(),
            waitingItem.isWaitingQueueEnabled()
        );
    }
}
