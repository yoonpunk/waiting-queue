package com.practice.waitingqueue.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import com.practice.waitingqueue.testdouble.FakeWaitingQueueRepository;
import org.junit.jupiter.api.Test;

class WaitingQueueRegisterServiceTest {

    @Test
    void 대기열_등록_시_앞선_순서가_없는_경우_성공() {
        // given
        long userId = 1L;
        long itemId = 100L;

        final var waitingQueueRepository = new FakeWaitingQueueRepository();

        // when
        final var sut = new WaitingQueueRegisterService(waitingQueueRepository);
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

        final var expectedUserId = 2L;

        final var waitingQueueRepository = new FakeWaitingQueueRepository();
        waitingQueueRepository.save(itemId, firstToken, firstScore);

        // when
        final var sut = new WaitingQueueRegisterService(waitingQueueRepository);
        final var result = sut.registerWaitingQueue(expectedUserId, itemId);

        // then
        final var registered = waitingQueueRepository.findByItemIdAndWaitingQueueToken(itemId, result);
        assertThat(registered).isPresent();
        assertThat(registered.get().getWaitingQueueToken()).isEqualTo(result);
        assertThat(registered.get().getWaitingQueueRank()).isEqualTo(2);
    }
}
