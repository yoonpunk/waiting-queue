package com.practice.waitingqueue.presentation.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
@Schema(description = "대기열에서 입장셋으로 옮겨가는 토큰 수 결과 DTO")
public class TokenCountToMoveResponse {
    @Schema(description = "대기열에서 입장셋으로 옮겨가는 토큰 수", example = "5")
    private final int tokenCountToMove;
}
