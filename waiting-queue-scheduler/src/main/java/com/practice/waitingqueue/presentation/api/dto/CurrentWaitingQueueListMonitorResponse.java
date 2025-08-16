package com.practice.waitingqueue.presentation.api.dto;

import com.practice.waitingqueue.domain.info.WaitingQueueMonitoringInfo;
import com.practice.waitingqueue.domain.info.WaitingQueueMonitoringInfoList;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Schema(description = "현재 대기열의 토큰 수 모니터링 응답 DTO")
public class CurrentWaitingQueueListMonitorResponse {

    @Schema(description = "상품별 대기열 정보")
    private final List<CurrentWaitingQueueInfoResponse> currentWaitingQueueList;

    public static CurrentWaitingQueueListMonitorResponse of(
        WaitingQueueMonitoringInfoList infoList
    ) {
        final var currentWaitingQueueList = infoList.stream()
            .map(CurrentWaitingQueueInfoResponse::of)
            .toList();

        return new CurrentWaitingQueueListMonitorResponse(currentWaitingQueueList);
    }

    @AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
    @Schema(description = "특정 상품 대기열의 토큰 수 조회 응답 DTO")
    public static class CurrentWaitingQueueInfoResponse {

        @Schema(description = "대기열 상품 고유번호", example = "12345")
        private final long itemId;

        @Schema(description = "대기열에 있는 토큰 수", example = "5")
        private final long queueingTokenCount;

        public static CurrentWaitingQueueInfoResponse of(WaitingQueueMonitoringInfo info) {
            return new CurrentWaitingQueueInfoResponse(
                info.getItemId(),
                info.getQueueingTokenCount()
            );
        }
    }
}
