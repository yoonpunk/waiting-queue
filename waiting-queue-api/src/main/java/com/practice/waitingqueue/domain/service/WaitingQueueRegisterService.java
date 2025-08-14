package com.practice.waitingqueue.domain.service;

import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import com.practice.waitingqueue.domain.repository.WaitingQueueRepository;
import com.practice.waitingqueue.domain.validator.WaitingQueueRegisterValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueRegisterService {

    private final WaitingQueueRepository waitingQueueRepository;
    private final WaitingQueueRegisterValidator waitingQueueRegisterValidator;

    public WaitingQueueToken registerWaitingQueue(long userId, long itemId) {
        waitingQueueRegisterValidator.validateItemIdCanUseWaitingQueue(itemId);

        final var waitingQueueToken = WaitingQueueToken.generate(userId, itemId);
        final var score = System.currentTimeMillis();

        return waitingQueueRepository.save(itemId, waitingQueueToken, score);
    }
}
