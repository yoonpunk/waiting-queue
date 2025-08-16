package com.practice.waitingqueue.presentation.api.controller;

import com.practice.waitingqueue.common.http.CommonResponse;
import com.practice.waitingqueue.domain.service.WaitingItemSyncService;
import com.practice.waitingqueue.presentation.api.dto.WaitingItemCreateOrUpdateRequest;
import com.practice.waitingqueue.presentation.api.dto.WaitingItemCreateOrUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Waiting Item Internal Admin API", description = "대기열 대상 상품 관련 비공개 어드민 API")
public class WaitingItemInternalAdminController {

    private final WaitingItemSyncService waitingItemSyncService;

    @Operation(summary = "대기열 대상 상품의 정보를 수기로 생성/변경합니다.")
    @PostMapping("/internal/admin/v1/waiting-item/{itemId}/manually")
    public CommonResponse<WaitingItemCreateOrUpdateResponse> createOrUpdateWaitingItemManually(
        @RequestHeader("user-id") final long userId,
        @PathVariable final long itemId,
        @RequestBody WaitingItemCreateOrUpdateRequest request
    ) {
        final var waitingItem = waitingItemSyncService.sync(request.toWaitingItemSyncCommand(itemId));
        return CommonResponse.success(WaitingItemCreateOrUpdateResponse.of(waitingItem));
    }
}
