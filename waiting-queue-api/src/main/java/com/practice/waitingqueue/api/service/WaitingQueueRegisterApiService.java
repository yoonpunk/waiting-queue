package com.practice.waitingqueue.api.service;

import com.practice.waitingqueue.api.dto.WaitingQueueRegisterResponse;
import com.practice.waitingqueue.domain.service.WaitingQueueRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueRegisterApiService {

    private final WaitingQueueRegisterService waitingQueueRegisterService;

    public WaitingQueueRegisterResponse registerWaitingQueue(long userId, long itemId) {
        // todo 대기열 등록할 상품 상태 검증로직 추가하기

        final var registeredQueue = waitingQueueRegisterService.registerWaitingQueue(userId, itemId);
        return WaitingQueueRegisterResponse.of(
            registeredQueue.getItemId(),
            registeredQueue.getWaitingQueueToken().getValue(),
            registeredQueue.getWaitingQueueRank(),
            false // todo 입장셋 만들고 true/false 적용하기
        );
    }
}
