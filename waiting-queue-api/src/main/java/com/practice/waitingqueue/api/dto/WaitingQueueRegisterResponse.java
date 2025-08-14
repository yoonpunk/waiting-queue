package com.practice.waitingqueue.api.dto;

import com.practice.waitingqueue.domain.info.WaitingQueueInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
@Schema(description = "대기열 등록 요청 결과 DTO")
public class WaitingQueueRegisterResponse {

    @Schema(description = "대기열 등록요청 상품 고유번호", example = "12345")
    private final long itemId;

    @Schema(description = "대기열 등록 토큰(유저번호 + - + 상품번호 + - + UUID)", example = "12345-54321-550e8400-e29b-41d4-a716-446655440000")
    private final String waitingQueueToken;

    @Schema(description = "대기열 순번(0이면 즉시 주문 가능, 1이면 제일 빠른 순번)", example = "3")
    private final int waitingQueueRank;

    @Schema(description = "현재 주문 가능 여부 (대기열 종료 후 true)", example = "true")
    private final boolean canOrder;

    public static WaitingQueueRegisterResponse of(WaitingQueueInfo waitingQueueInfo) {
        return new WaitingQueueRegisterResponse(
            waitingQueueInfo.getItemId(),
            waitingQueueInfo.getWaitingQueueToken().getValue(),
            waitingQueueInfo.getWaitingQueueRank(),
            waitingQueueInfo.isEnteredEntrySet()
        );
    }
}
