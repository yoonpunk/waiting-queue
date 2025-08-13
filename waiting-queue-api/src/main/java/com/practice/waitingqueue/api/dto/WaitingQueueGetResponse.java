package com.practice.waitingqueue.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
@Schema(description = "대기열 조회 결과 DTO")
public class WaitingQueueGetResponse {

    @Schema(description = "대기열 등록요청 상품 고유번호", example = "12345")
    private final Long itemId;

    @Schema(description = "대기열 등록 고유번호", example = "550e8400-e29b-41d4-a716-446655440000")
    private final String waitingQueueId;

    @Schema(description = "입장까지 남은 순번 수 (0이면 즉시 주문 가능, 1이면 제일 빠른 순번)", example = "3")
    private final int waitingQueuePosition;

    @Schema(description = "현재 주문 가능 여부 (대기열 종료 후 true)", example = "true")
    private final boolean canOrder;
}
