package com.practice.waitingqueue.api.service;

import com.practice.waitingqueue.api.dto.WaitingQueueGetResponse;
import com.practice.waitingqueue.domain.service.WaitingQueueReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueReadApiService {

    private final WaitingQueueReadService waitingQueueReadService;

    public WaitingQueueGetResponse getWaitingQueue(long userId, long itemId, String waitingQueueToken) {
        final var registeredWaitingQueue = waitingQueueReadService.getWaitingQueue(
            userId,
            itemId,
            waitingQueueToken
        );

        return WaitingQueueGetResponse.of(
            registeredWaitingQueue.getItemId(),
            registeredWaitingQueue.getWaitingQueueToken().getValue(),
            registeredWaitingQueue.getWaitingQueueRank(),
            false // todo 입장셋 만들고 true/false 적용하기
        );
    }
}
