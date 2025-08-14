package com.practice.waitingqueue.domain.service;

import com.practice.waitingqueue.domain.dto.WaitingItemSyncCommand;
import com.practice.waitingqueue.domain.entity.WaitingItem;
import com.practice.waitingqueue.domain.repository.WaitingItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingItemSyncService {

    private final WaitingItemRepository waitingItemRepository;

    public WaitingItem sync(WaitingItemSyncCommand command) {
        final var waitingItemOptional = waitingItemRepository.findByItemId(command.getItemId());

        if (waitingItemOptional.isEmpty()) {
            final var createdWaitingItem = waitingItemRepository.save(
                WaitingItem.of(
                    command.getItemId(),
                    command.getItemName(),
                    command.getRemainCount(),
                    command.isWaitingQueueEnabled()
                )
            );
            log.info("[WaitingItemSyncService] Created new waiting item: {}", createdWaitingItem.toString());

            return createdWaitingItem;
        }

        final var waitingItem = waitingItemOptional.get();
        waitingItem.updateRemainCount(command.getRemainCount());
        waitingItem.updateWaitingQueueEnabled(command.isWaitingQueueEnabled());

        final var updatedWaitingItem = waitingItemRepository.save(waitingItem);
        log.info("[WaitingItemSyncService] Created new waiting item: {}", updatedWaitingItem.toString());

        return updatedWaitingItem;
    }
}
