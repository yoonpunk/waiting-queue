package com.practice.waitingqueue.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.practice.waitingqueue.domain.entity.WaitingItem;
import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import com.practice.waitingqueue.domain.exception.WaitingQueueRegisterFailedException;
import com.practice.waitingqueue.domain.validator.WaitingQueueRegisterValidator;
import com.practice.waitingqueue.testdouble.FakeWaitingItemRepository;
import com.practice.waitingqueue.testdouble.FakeWaitingQueueRepository;
import org.junit.jupiter.api.Test;

class WaitingQueueRegisterServiceTest {

    @Test
    void 대기열_등록_시_앞선_순서가_없는_경우_성공() {
        // given
        final var userId = 1L;
        final var itemId = 100L;
        final var waitingItem = WaitingItem.of(itemId, "testItem", 10, true);

        final var waitingQueueRepository = new FakeWaitingQueueRepository();
        final var waitingItemRepository = new FakeWaitingItemRepository();
        waitingItemRepository.save(waitingItem);

        final var waitingQueueRegisterValidator = new WaitingQueueRegisterValidator(waitingItemRepository);

        // when
        final var sut = new WaitingQueueRegisterService(waitingQueueRepository, waitingQueueRegisterValidator);
        final var result = sut.registerWaitingQueue(userId, itemId);

        // then
        final var registered = waitingQueueRepository.findByItemIdAndWaitingQueueToken(itemId, result);
        assertThat(registered).isPresent();
        assertThat(registered.get().getWaitingQueueToken()).isEqualTo(result);
        assertThat(registered.get().getWaitingQueueRank()).isEqualTo(1);
    }

    @Test
    void 대기열_등록_시_앞선_순서가_있는_경우_성공() {
        // given
        final var itemId = 100L;
        final var firstUserId = 1L;
        final var firstToken = WaitingQueueToken.generate(firstUserId, itemId);
        final var firstScore = System.currentTimeMillis();
        final var waitingItem = WaitingItem.of(itemId, "testItem", 10, true);

        final var expectedUserId = 2L;

        final var waitingQueueRepository = new FakeWaitingQueueRepository();
        waitingQueueRepository.save(itemId, firstToken, firstScore);

        final var waitingItemRepository = new FakeWaitingItemRepository();
        waitingItemRepository.save(waitingItem);

        final var waitingQueueRegisterValidator = new WaitingQueueRegisterValidator(waitingItemRepository);

        // when
        final var sut = new WaitingQueueRegisterService(waitingQueueRepository, waitingQueueRegisterValidator);
        final var result = sut.registerWaitingQueue(expectedUserId, itemId);

        // then
        final var registered = waitingQueueRepository.findByItemIdAndWaitingQueueToken(itemId, result);
        assertThat(registered).isPresent();
        assertThat(registered.get().getWaitingQueueToken()).isEqualTo(result);
        assertThat(registered.get().getWaitingQueueRank()).isEqualTo(2);
    }

    @Test
    void 대기열_등록_시_상품이_대기열을_이용할_수_없는_경우_예외가_발생한다() {
        // given
        final var itemId = 100L;
        final var userId = 1L;
        final var waitingItem = WaitingItem.of(itemId, "testItem", 0, false);

        final var waitingItemRepository = new FakeWaitingItemRepository();
        waitingItemRepository.save(waitingItem);

        final var waitingQueueRegisterValidator = new WaitingQueueRegisterValidator(waitingItemRepository);
        final var waitingQueueRepository = new FakeWaitingQueueRepository();

        // when & then
        final var sut = new WaitingQueueRegisterService(waitingQueueRepository, waitingQueueRegisterValidator);
        assertThrows(
            WaitingQueueRegisterFailedException.class,
            () -> sut.registerWaitingQueue(userId, itemId)
        );
    }
}
