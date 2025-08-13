package com.practice.waitingqueue.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.practice.waitingqueue.domain.entity.WaitingQueueTokenGenerator;
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
        assertThat(result.getItemId()).isEqualTo(itemId);
        assertThat(result.getWaitingQueueRank()).isEqualTo(1);
    }

    @Test
    void 대기열_등록_시_앞선_순서가_있는_경우_성공() {
        // given
        long itemId = 100L;
        long firstUserId = 1L;
        String firstToken = WaitingQueueTokenGenerator.generate(firstUserId, itemId);
        long firstScore = System.currentTimeMillis();

        long expectedUserId = 2L;

        final var waitingQueueRepository = new FakeWaitingQueueRepository();
        waitingQueueRepository.save(itemId, firstToken, firstScore);

        // when
        final var sut = new WaitingQueueRegisterService(waitingQueueRepository);
        final var result = sut.registerWaitingQueue(expectedUserId, itemId);

        // then
        assertThat(result.getItemId()).isEqualTo(itemId);
        assertThat(result.getWaitingQueueRank()).isEqualTo(2);
    }
}
