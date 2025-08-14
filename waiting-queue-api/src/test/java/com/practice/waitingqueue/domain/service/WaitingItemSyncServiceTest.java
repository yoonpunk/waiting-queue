package com.practice.waitingqueue.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.practice.waitingqueue.domain.dto.WaitingItemSyncCommand;
import com.practice.waitingqueue.domain.entity.WaitingItem;
import com.practice.waitingqueue.testdouble.FakeWaitingItemRepository;
import org.junit.jupiter.api.Test;

class WaitingItemSyncServiceTest {

    @Test
    void 기존_대기열_상품_동기화_성공() {
        // given
        final var itemId = 100L;
        final var itemName = "Test Item";
        final var remainCount = 10;
        final var waitingQueueEnabled = true;
        final var existWaitingItem = WaitingItem.of(
            itemId,
            itemName,
            remainCount,
            waitingQueueEnabled
        );

        final var waitingItemRepository = new FakeWaitingItemRepository();
        waitingItemRepository.save(existWaitingItem);

        final var sut = new WaitingItemSyncService(waitingItemRepository);

        // when
        final var changedRemainCount = 5;
        final var changedWaitingQueueEnabled = false;
        final var command = WaitingItemSyncCommand.of(
            itemId,
            itemName,
            changedRemainCount,
            changedWaitingQueueEnabled
        );

        final var syncedWaitingItem = sut.sync(command);

        // then
        assertThat(syncedWaitingItem.getItemId()).isEqualTo(itemId);
        assertThat(syncedWaitingItem.getItemName()).isEqualTo(itemName);
        assertThat(syncedWaitingItem.getRemainCount()).isEqualTo(changedRemainCount);
        assertThat(syncedWaitingItem.isWaitingQueueEnabled()).isEqualTo(changedWaitingQueueEnabled);
    }

    @Test
    void 신규_대기열_상품의_동기화_성공() {
        // given
        final var itemId = 100L;
        final var itemName = "Test Item";
        final var remainCount = 10;
        final var waitingQueueEnabled = true;

        final var waitingItemRepository = new FakeWaitingItemRepository();
        final var sut = new WaitingItemSyncService(waitingItemRepository);

        // when
        final var command = WaitingItemSyncCommand.of(
            itemId,
            itemName,
            remainCount,
            waitingQueueEnabled
        );
        final var syncedWaitingItem = sut.sync(command);

        // then
        assertThat(syncedWaitingItem.getItemId()).isEqualTo(itemId);
        assertThat(syncedWaitingItem.getItemName()).isEqualTo(itemName);
        assertThat(syncedWaitingItem.getRemainCount()).isEqualTo(remainCount);
        assertThat(syncedWaitingItem.isWaitingQueueEnabled()).isEqualTo(waitingQueueEnabled);
    }
}
