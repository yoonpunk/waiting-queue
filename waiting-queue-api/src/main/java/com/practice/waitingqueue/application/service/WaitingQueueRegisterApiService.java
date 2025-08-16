package com.practice.waitingqueue.application.service;

import com.practice.waitingqueue.application.dto.WaitingQueueInfoResult;
import com.practice.waitingqueue.domain.service.WaitingQueueReadService;
import com.practice.waitingqueue.domain.service.WaitingQueueRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueRegisterApiService {

    private final WaitingQueueRegisterService waitingQueueRegisterService;
    private final WaitingQueueReadService waitingQueueReadService;

    public WaitingQueueInfoResult registerWaitingQueue(long userId, long itemId) {
        final var waitingQueueToken = waitingQueueRegisterService.registerWaitingQueue(
            userId,
            itemId
        );

        final var registeredQueue = waitingQueueReadService.getWaitingQueueInfo(
            userId,
            itemId,
            waitingQueueToken.getValue()
        );

        return WaitingQueueInfoResult.of(registeredQueue);
    }
}
