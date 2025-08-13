package com.practice.waitingqueue.api.controller;

import com.practice.waitingqueue.api.dto.WaitingQueueGetResponse;
import com.practice.waitingqueue.api.dto.WaitingQueueRegisterResponse;
import com.practice.waitingqueue.api.service.WaitingQueueReadApiService;
import com.practice.waitingqueue.api.service.WaitingQueueRegisterApiService;
import com.practice.waitingqueue.common.http.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Waiting Queue Public API", description = "대기열 관련 공개 API")
public class WaitingQueueController {

    private final WaitingQueueRegisterApiService waitingQueueRegisterApiService;
    private final WaitingQueueReadApiService waitingQueueReadApiService;

    /**
     * 대기열 등록 API
     */
    @Operation(summary = "대기열 등록 API")
    @PostMapping("/api/v1/item/{itemId}/waiting-queue")
    public CommonResponse<WaitingQueueRegisterResponse> registerWaitingQueue(
        @RequestHeader("user-id") final long userId,
        @PathVariable final long itemId
    ) {
        final var result = waitingQueueRegisterApiService.registerWaitingQueue(
            userId,
            itemId
        );
        return CommonResponse.success(result);
    }

    /**
     * 대기열 등록 조회
     */
    @Operation(summary = "대기열 조회 API")
    @GetMapping("/api/v1/item/{itemId}/waiting-queue/{waitingQueueToken}")
    public CommonResponse<WaitingQueueGetResponse> getWaitingQueue(
        @RequestHeader("user-id") final long userId,
        @PathVariable final long itemId,
        @PathVariable final String waitingQueueToken
    ) {
        final var result = waitingQueueReadApiService.getWaitingQueue(
            userId,
            itemId,
            waitingQueueToken
        );
        return CommonResponse.success(result);
    }
}
