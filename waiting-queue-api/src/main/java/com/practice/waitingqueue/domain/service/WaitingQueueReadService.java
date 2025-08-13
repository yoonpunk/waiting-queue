package com.practice.waitingqueue.domain.service;

import com.practice.waitingqueue.domain.entity.WaitingQueue;
import com.practice.waitingqueue.domain.exception.WaitingQueueNotFoundException;
import com.practice.waitingqueue.domain.repository.WaitingQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueReadService {

    private final WaitingQueueRepository waitingQueueRepository;

    public WaitingQueue getWaitingQueue(long itemId, String waitingQueueToken) {
        return waitingQueueRepository.findByItemIdAndWaitingQueueToken(itemId, waitingQueueToken)
            .orElseThrow(() -> new WaitingQueueNotFoundException(itemId, waitingQueueToken));
    }
}
