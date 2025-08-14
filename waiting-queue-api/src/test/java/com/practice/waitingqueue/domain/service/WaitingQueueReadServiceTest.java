package com.practice.waitingqueue.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import com.practice.waitingqueue.domain.exception.WaitingQueueNotFoundException;
import com.practice.waitingqueue.testdouble.FakeEntrySetRepository;
import com.practice.waitingqueue.testdouble.FakeWaitingQueueRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WaitingQueueReadServiceTest {

    @Test
    void 입장셋에_입장하지_않은_대기열_조회_성공() {
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

        final var entrySetRepository = new FakeEntrySetRepository();

        // when
        final var sut = new WaitingQueueReadService(waitingQueueRepository, entrySetRepository);
        final var result = sut.getWaitingQueueInfo(secondUserId, itemId, secondToken.getValue());

        // then
        assertThat(result.getItemId()).isEqualTo(itemId);
        assertThat(result.getWaitingQueueRank()).isEqualTo(2);
        assertThat(result.isEnteredEntrySet()).isFalse();
    }

    @Test
    void 입장셋에_입장한_대기열_조회_성공() {
        // given
        final var itemId = 100L;
        final var enteredUserId = 1L;
        final var enteredToken = WaitingQueueToken.generate(enteredUserId, itemId);

        final var waitingUserId = 2L;
        final var waitingToken = WaitingQueueToken.generate(waitingUserId, itemId);
        final var waitingScore = System.currentTimeMillis();

        final var waitingQueueRepository = new FakeWaitingQueueRepository();
        waitingQueueRepository.save(itemId, waitingToken, waitingScore);

        final var entrySetRepository = new FakeEntrySetRepository();
        entrySetRepository.save(itemId, enteredToken);

        // when
        final var sut = new WaitingQueueReadService(waitingQueueRepository, entrySetRepository);
        final var result = sut.getWaitingQueueInfo(enteredUserId, itemId, enteredToken.getValue());

        // then
        assertThat(result.getItemId()).isEqualTo(itemId);
        assertThat(result.getWaitingQueueRank()).isEqualTo(0);
        assertThat(result.isEnteredEntrySet()).isTrue();
    }

    @Test
    void 존재하지_않은_대기열_조회_시_실패() {
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

        final var entrySetRepository = new FakeEntrySetRepository();

        // when & then
        final var sut = new WaitingQueueReadService(waitingQueueRepository, entrySetRepository);

        Assertions.assertThrows(
            WaitingQueueNotFoundException.class,
            () -> sut.getWaitingQueueInfo(expectedUser, itemId, notRegisteredRawToken)
        );
    }
}
