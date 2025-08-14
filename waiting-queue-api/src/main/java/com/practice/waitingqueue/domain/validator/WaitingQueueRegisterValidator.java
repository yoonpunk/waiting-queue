package com.practice.waitingqueue.domain.validator;

import com.practice.waitingqueue.domain.exception.WaitingQueueRegisterFailedException;
import com.practice.waitingqueue.domain.exception.WaitingQueueRegisterFailedException.ErrorCode;
import com.practice.waitingqueue.domain.repository.WaitingItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingQueueRegisterValidator {

    private final WaitingItemRepository waitingItemRepository;

    public void validateItemIdCanUseWaitingQueue(long itemId) {
        final var waitingItem = waitingItemRepository.findByItemId(itemId)
            .orElseThrow(() -> new WaitingQueueRegisterFailedException(
                itemId, ErrorCode.ITEM_CANNOT_USE_WAITING_QUEUE
            ));


        if (waitingItem.cannotUseWaitingQueue()) {
            throw new WaitingQueueRegisterFailedException(itemId, ErrorCode.ITEM_CANNOT_USE_WAITING_QUEUE);
        }
    }
}
