package com.practice.waitingqueue.interfaces.dto;

import com.practice.waitingqueue.domain.info.WaitingQueueTokenCountInfo;
import com.practice.waitingqueue.domain.info.WaitingQueueTokenCountInfoList;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Schema(description = "현재 대기열 토큰 수 조회 응답 DTO")
public class WaitingQueueTokenCountListResponse {

    @Schema(description = "상품별 대기열 토큰 수 정보")
    private final List<WaitingQueueTokenCountResponse> currentWaitingQueueList;

    public static WaitingQueueTokenCountListResponse of(
        WaitingQueueTokenCountInfoList infoList
    ) {
        final var responseList = infoList.stream()
            .map(WaitingQueueTokenCountResponse::of)
            .toList();

        return new WaitingQueueTokenCountListResponse(responseList);
    }

    @Getter
    @AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
    @Schema(description = "특정 상품 대기열 토큰 수 조회 응답 DTO")
    public static class WaitingQueueTokenCountResponse {

        @Schema(description = "대기열 상품 고유번호", example = "12345")
        private final long itemId;

        @Schema(description = "대기중인 토근 수", example = "5")
        private final long waitingTokenCount;

        @Schema(description = "입장된 토큰 수", example = "5")
        private final long enteredTokenCount;

        public static WaitingQueueTokenCountResponse of(WaitingQueueTokenCountInfo info) {
            return new WaitingQueueTokenCountResponse(
                info.getItemId(),
                info.getWaitingQueueTokenCount(),
                info.getEntrySetTokenCount()
            );
        }
    }
}
