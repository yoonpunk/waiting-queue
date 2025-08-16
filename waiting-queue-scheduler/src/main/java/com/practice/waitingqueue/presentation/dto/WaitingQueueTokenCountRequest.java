package com.practice.waitingqueue.presentation.dto;

import com.practice.waitingqueue.domain.dto.WaitingQueueTokenCountCriteria;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
@Schema(description = "현재 대기열 토큰 수 조회 요청 DTO")
public class WaitingQueueTokenCountRequest {

    @Schema(description = "현재 대기열 전체 조회 여부 (true: 전체, false: 부분", example = "5")
    private final boolean searchAll;

    @Schema(description = "부분 조회 시, 조회할 itemId 리스트(searchAll이 false일 경우 반드시 값이 존재해야함)", example = "[\"123\", \"456\", \"789\"]")
    private final Set<Long> itemIdListToSearch;

    public WaitingQueueTokenCountCriteria toWaitingQueueMonitoringCriteria() {
        if (searchAll) {
            return WaitingQueueTokenCountCriteria.createForSearchAll();
        }
        return WaitingQueueTokenCountCriteria.createForSearchPartial(itemIdListToSearch);
    }
}
