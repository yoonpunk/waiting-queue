package com.practice.waitingqueue.application.service;

import com.practice.waitingqueue.application.dto.WaitingQueueInfoResult;
import com.practice.waitingqueue.domain.service.WaitingQueueReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueReadApiService {

    private final WaitingQueueReadService waitingQueueReadService;

    public WaitingQueueInfoResult getWaitingQueue(long userId, long itemId, String waitingQueueToken) {
        final var registeredWaitingQueue = waitingQueueReadService.getWaitingQueueInfo(
            userId,
            itemId,
            waitingQueueToken
        );

        return WaitingQueueInfoResult.of(registeredWaitingQueue);
    }
}
