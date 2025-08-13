package com.practice.waitingqueue.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.practice.waitingqueue.domain.entity.WaitingQueueTokenGenerator;
import com.practice.waitingqueue.domain.exception.WaitingQueueNotFoundException;
import com.practice.waitingqueue.testdouble.FakeWaitingQueueRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WaitingQueueReadServiceTest {

    @Test
    void 대기열_조회_성공() {
        // given
        long itemId = 100L;
        long firstUserId = 1L;
        String firstToken = WaitingQueueTokenGenerator.generate(firstUserId, itemId);
        long firstScore = System.currentTimeMillis();

        long secondUserId = 2L;
        String secondToken = WaitingQueueTokenGenerator.generate(secondUserId, itemId);
        long secondScore = System.currentTimeMillis();

        final var waitingQueueRepository = new FakeWaitingQueueRepository();
        waitingQueueRepository.save(itemId, firstToken, firstScore);
        waitingQueueRepository.save(itemId, secondToken, secondScore);

        // when
        final var sut = new WaitingQueueReadService(waitingQueueRepository);
        final var result = sut.getWaitingQueue(itemId, secondToken);

        // then
        assertThat(result.getItemId()).isEqualTo(itemId);
        assertThat(result.getWaitingQueueRank()).isEqualTo(2);
    }

    @Test
    void 대기열_조회_실패_존재하지_않음() {
        // given
        long itemId = 100L;
        long firstUserId = 1L;
        String firstToken = WaitingQueueTokenGenerator.generate(firstUserId, itemId);
        long firstScore = System.currentTimeMillis();

        long secondUserId = 2L;
        String secondToken = WaitingQueueTokenGenerator.generate(secondUserId, itemId);
        long secondScore = System.currentTimeMillis();

        long expectedUser = 3L;
        String notRegisteredToken = WaitingQueueTokenGenerator.generate(expectedUser, itemId);

        final var waitingQueueRepository = new FakeWaitingQueueRepository();
        waitingQueueRepository.save(itemId, firstToken, firstScore);
        waitingQueueRepository.save(itemId, secondToken, secondScore);

        // when & then
        final var sut = new WaitingQueueReadService(waitingQueueRepository);

        Assertions.assertThrows(
            WaitingQueueNotFoundException.class,
            () -> sut.getWaitingQueue(itemId, notRegisteredToken)
        );
    }
}