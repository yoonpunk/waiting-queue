package com.practice.waitingqueue.presentation.api.controller;

import com.practice.waitingqueue.common.http.CommonResponse;
import com.practice.waitingqueue.domain.service.MoveTokenFromWaitingQueueToEntrySetService;
import com.practice.waitingqueue.presentation.api.dto.TokenCountToMoveResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Waiting Queue Internal Admin API", description = "대기열 관련 비공개 어드민 API")
public class WaitingQueueInternalAdminController {

    private final MoveTokenFromWaitingQueueToEntrySetService moveTokenFromWaitingQueueToEntrySetService;

    @Operation(summary = "대기열에서 입장열로 옮겨가는 토큰 수를 조회합니다.")
    @GetMapping("/internal/admin/v1/waiting-queue/token-count-to-move")
    public CommonResponse<TokenCountToMoveResponse> getTokenCountToMove(
        @RequestHeader("user-id") final long userId
    ) {
        final var changedTokenCountToMove = moveTokenFromWaitingQueueToEntrySetService.getTokenCountToMove();
        return CommonResponse.success(TokenCountToMoveResponse.of(changedTokenCountToMove));
    }

    @Operation(summary = "대기열에서 입장열로 옮겨가는 토큰 수를 변경합니다.")
    @PutMapping("/internal/admin/v1/waiting-queue/token-count-to-move")
    public CommonResponse<TokenCountToMoveResponse> changeTokenCountToMove(
        @RequestHeader("user-id") final long userId,
        @RequestParam @PositiveOrZero final int tokenCountToMove
    ) {
        moveTokenFromWaitingQueueToEntrySetService.setTokenCountToMove(tokenCountToMove);
        final var changedTokenCountToMove = moveTokenFromWaitingQueueToEntrySetService.getTokenCountToMove();
        return CommonResponse.success(TokenCountToMoveResponse.of(changedTokenCountToMove));
    }
}
