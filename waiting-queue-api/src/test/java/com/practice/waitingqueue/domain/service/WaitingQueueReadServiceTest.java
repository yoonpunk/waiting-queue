package com.practice.waitingqueue.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import com.practice.waitingqueue.domain.exception.WaitingQueueNotFoundException;
import com.practice.waitingqueue.testdouble.FakeWaitingQueueRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WaitingQueueReadServiceTest {

    @Test
    void 대기열_조회_성공() {
        // given
        final var itemId = 100L;
        final var firstUserId = 1L;
        final var firstToken = WaitingQueueToken.generate(firstUserId, itemId);
        final var firstScore = System.currentTimeMillis();

        final var secondUserId = 2L;
        final var secondToken = WaitingQueueToken.generate(secondUserId, itemId);
        final var secondScore = System.currentTimeMillis();

        final var waitingQueueRepository = new FakeWaitingQueueRepository();
        waitingQueueRepository.save(itemId, firstToken, firstScore);
        waitingQueueRepository.save(itemId, secondToken, secondScore);

        // when
        final var sut = new WaitingQueueReadService(waitingQueueRepository);
        final var result = sut.getWaitingQueue(secondUserId, itemId, secondToken.getValue());

        // then
        assertThat(result.getItemId()).isEqualTo(itemId);
        assertThat(result.getWaitingQueueRank()).isEqualTo(2);
    }

    @Test
    void 대기열_조회_실패_존재하지_않음() {
        // given
        final var itemId = 100L;
        final var firstUserId = 1L;
        final var firstToken = WaitingQueueToken.generate(firstUserId, itemId);
        final var firstScore = System.currentTimeMillis();

        final var secondUserId = 2L;
        final var secondToken = WaitingQueueToken.generate(secondUserId, itemId);
        final var secondScore = System.currentTimeMillis();

        final var expectedUser = 3L;
        final var notRegisteredRawToken = WaitingQueueToken.generate(expectedUser, itemId).getValue();

        final var waitingQueueRepository = new FakeWaitingQueueRepository();
        waitingQueueRepository.save(itemId, firstToken, firstScore);
        waitingQueueRepository.save(itemId, secondToken, secondScore);

        // when & then
        final var sut = new WaitingQueueReadService(waitingQueueRepository);

        Assertions.assertThrows(
            WaitingQueueNotFoundException.class,
            () -> sut.getWaitingQueue(expectedUser, itemId, notRegisteredRawToken)
        );
    }
}