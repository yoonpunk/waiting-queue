package com.practice.waitingqueue.api.controller;

import com.practice.waitingqueue.api.dto.WaitingQueueRegisterResponse;
import com.practice.waitingqueue.common.http.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Waiting Queue Public API", description = "대기열 관련 공개 API")
public class WaitingQueueController {

    /**
     * 대기열 등록 API
     */
    @Operation(summary = "대기열 등록 API")
    @PostMapping("/api/v1/item/{itemId}/waiting-queue")
    public CommonResponse<WaitingQueueRegisterResponse> registerWaitingQueue(
        @PathVariable final long itemId
    ) {
        // todo : 대기열 등록 로직 구현
        final var response = WaitingQueueRegisterResponse.of(
            itemId,
            "550e8400-e29b-41d4-a716-446655440000",
            3,
            false
        );

        return CommonResponse.success(response);
    }

    /**
     * 대기열 등록 조회
     */
    @Operation(summary = "대기열 조회 API")
    @GetMapping("/api/v1/item/{itemId}/waiting-queue/{waitingQueueId}")
    public CommonResponse<WaitingQueueRegisterResponse> getWaitingQueue(
        @PathVariable final long itemId,
        @PathVariable final String waitingQueueId
    ) {
        // todo : 대기열 조회 로직 구현
        final var response = WaitingQueueRegisterResponse.of(
            itemId,
            "550e8400-e29b-41d4-a716-446655440000",
            3,
            false
        );

        return CommonResponse.success(response);
    }
}
