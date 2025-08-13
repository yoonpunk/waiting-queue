package com.practice.waitingqueue.domain.service;

import com.practice.waitingqueue.domain.entity.WaitingQueue;
import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import com.practice.waitingqueue.domain.exception.WaitingQueueNotFoundException;
import com.practice.waitingqueue.domain.repository.WaitingQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueRegisterService {

    private final WaitingQueueRepository waitingQueueRepository;

    public WaitingQueue registerWaitingQueue(long userId, long itemId) {
        final var waitingQueueToken = WaitingQueueToken.generate(userId, itemId);
        final var score = System.currentTimeMillis();

        waitingQueueRepository.save(itemId, waitingQueueToken, score);
        return waitingQueueRepository.findByItemIdAndWaitingQueueToken(itemId, waitingQueueToken)
            .orElseThrow(() -> new WaitingQueueNotFoundException(itemId, waitingQueueToken));
    }
}
