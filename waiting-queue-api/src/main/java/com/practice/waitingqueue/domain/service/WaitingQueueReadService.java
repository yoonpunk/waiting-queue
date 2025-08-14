package com.practice.waitingqueue.domain.service;

import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import com.practice.waitingqueue.domain.exception.WaitingQueueNotFoundException;
import com.practice.waitingqueue.domain.info.WaitingQueueInfo;
import com.practice.waitingqueue.domain.repository.EntrySetRepository;
import com.practice.waitingqueue.domain.repository.WaitingQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueReadService {

    private final WaitingQueueRepository waitingQueueRepository;
    private final EntrySetRepository entrySetRepository;

    public WaitingQueueInfo getWaitingQueueInfo(long userId, long itemId, String waitingQueueToken) {
        final var validatedToken = WaitingQueueToken.validateAndCreate(
            userId,
            itemId,
            waitingQueueToken
        );

        final var enteredEntrySet = entrySetRepository.containsToken(itemId, validatedToken);

        if (enteredEntrySet) {
            return WaitingQueueInfo.createEntered(itemId, validatedToken);
        } else {
            final var waitingQueue = waitingQueueRepository.findByItemIdAndWaitingQueueToken(
                itemId,
                validatedToken
            ).orElseThrow(() -> new WaitingQueueNotFoundException(itemId, validatedToken));

            return WaitingQueueInfo.createWaiting(waitingQueue);
        }
    }
}
