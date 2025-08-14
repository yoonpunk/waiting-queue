package com.practice.waitingqueue.domain.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import com.practice.waitingqueue.testdouble.FakeEntrySetRepository;
import com.practice.waitingqueue.testdouble.FakeWaitingQueueRepository;
import org.junit.jupiter.api.Test;

class MoveTokenFromWaitingQueueToEntrySetServiceTest {

    @Test
    void 설정된_갯수만큼_토큰이_대기열에서_입장셋으로_이동한다() {
        // given
        final var waitingQueueRepository = new FakeWaitingQueueRepository();
        final var entrySetRepository = new FakeEntrySetRepository();

        final var itemId1 = 100L;
        final var itemId2 = 200L;

        final var userId1 = 11L;
        final var item1Token1 = WaitingQueueToken.generate(userId1, itemId1);
        waitingQueueRepository.save(itemId1, item1Token1, System.currentTimeMillis());

        final var userId2 = 12L;
        final var item1Token2 = WaitingQueueToken.generate(userId2, itemId1);
        waitingQueueRepository.save(itemId1, item1Token2, System.currentTimeMillis());

        final var userId3 = 13L;
        final var item1Token3 = WaitingQueueToken.generate(userId3, itemId1);
        waitingQueueRepository.save(itemId1, item1Token3, System.currentTimeMillis());

        final var userId4 = 21L;
        final var item2Token1 = WaitingQueueToken.generate(userId4, itemId2);
        waitingQueueRepository.save(itemId2, item2Token1, System.currentTimeMillis());

        final var userId5 = 22L;
        final var item2Token2 = WaitingQueueToken.generate(userId5, itemId2);
        waitingQueueRepository.save(itemId2, item2Token2, System.currentTimeMillis());

        final var userId6 = 23L;
        final var item2Token3 = WaitingQueueToken.generate(userId6, itemId2);
        waitingQueueRepository.save(itemId2, item2Token3, System.currentTimeMillis());

        final var sut = new MoveTokenFromWaitingQueueToEntrySetService(waitingQueueRepository, entrySetRepository);

        // when
        sut.moveWaitingQueueTokens();

        // then
        // item1, item2 waiting queue에서 토큰이 모두 제거되고 entry set에 저장되어야 한다.
        assertThat(entrySetRepository.containsToken(itemId1, item1Token1)).isTrue();
        assertThat(entrySetRepository.containsToken(itemId1, item1Token2)).isTrue();
        assertThat(entrySetRepository.containsToken(itemId1, item1Token3)).isTrue();

        assertThat(entrySetRepository.containsToken(itemId2, item2Token1)).isTrue();
        assertThat(entrySetRepository.containsToken(itemId2, item2Token2)).isTrue();
        assertThat(entrySetRepository.containsToken(itemId2, item2Token3)).isTrue();

        assertThat(waitingQueueRepository.findByItemIdAndWaitingQueueToken(itemId1, item1Token1)).isEmpty();
        assertThat(waitingQueueRepository.findByItemIdAndWaitingQueueToken(itemId1, item1Token2)).isEmpty();
        assertThat(waitingQueueRepository.findByItemIdAndWaitingQueueToken(itemId1, item1Token3)).isEmpty();

        assertThat(waitingQueueRepository.findByItemIdAndWaitingQueueToken(itemId2, item1Token1)).isEmpty();
        assertThat(waitingQueueRepository.findByItemIdAndWaitingQueueToken(itemId2, item1Token2)).isEmpty();
        assertThat(waitingQueueRepository.findByItemIdAndWaitingQueueToken(itemId2, item1Token3)).isEmpty();
    }
}