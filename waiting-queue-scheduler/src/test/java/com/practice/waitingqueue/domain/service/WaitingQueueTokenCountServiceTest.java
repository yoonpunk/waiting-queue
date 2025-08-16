package com.practice.waitingqueue.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.practice.waitingqueue.domain.dto.WaitingQueueTokenCountCriteria;
import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import com.practice.waitingqueue.testdouble.FakeEntrySetRepository;
import com.practice.waitingqueue.testdouble.FakeWaitingQueueRepository;
import java.util.Set;
import org.junit.jupiter.api.Test;

class WaitingQueueTokenCountServiceTest {

    @Test
    void 전체_대기열의_모니터링_정보_조회가_성공() {
        // given
        final var waitingQueueRepository = new FakeWaitingQueueRepository();
        final var entrySetRepository = new FakeEntrySetRepository();
        final var itemOneId = 1L;
        final var itemTwoId = 2L;
        final var itemThreeId = 3L;

        // itemOneId: 3개, itemTwoId: 2개, itemThreeId: 1개
        waitingQueueRepository.save(itemOneId, WaitingQueueToken.generate(1L, itemOneId), System.currentTimeMillis());
        waitingQueueRepository.save(itemOneId, WaitingQueueToken.generate(2L, itemOneId), System.currentTimeMillis());
        waitingQueueRepository.save(itemTwoId, WaitingQueueToken.generate(3L, itemTwoId), System.currentTimeMillis());
        waitingQueueRepository.save(itemTwoId, WaitingQueueToken.generate(4L, itemTwoId), System.currentTimeMillis());
        waitingQueueRepository.save(itemTwoId, WaitingQueueToken.generate(5L, itemTwoId), System.currentTimeMillis());
        waitingQueueRepository.save(itemThreeId, WaitingQueueToken.generate(6L, itemThreeId), System.currentTimeMillis());

        // itemOneId: 2개, itemTwoId: 0개, itemThreeId: 3개
        entrySetRepository.save(itemOneId, WaitingQueueToken.generate(1L, itemOneId));
        entrySetRepository.save(itemOneId, WaitingQueueToken.generate(2L, itemOneId));
        entrySetRepository.save(itemThreeId, WaitingQueueToken.generate(4L, itemThreeId));
        entrySetRepository.save(itemThreeId, WaitingQueueToken.generate(5L, itemThreeId));
        entrySetRepository.save(itemThreeId, WaitingQueueToken.generate(6L, itemThreeId));

        final var sut = new WaitingQueueTokenCountService(waitingQueueRepository, entrySetRepository);

        // when
        final var criteria = WaitingQueueTokenCountCriteria.createForSearchAll();
        final var result = sut.countWaitingQueueToken(criteria);

        // then
        assertThat(result.getInfoList()).hasSize(3);

        assertThat(result.getInfoList())
            .extracting("itemId")
            .containsExactlyInAnyOrder(itemOneId, itemTwoId, itemThreeId);

        assertThat(result.getInfoList())
            .filteredOn(it -> it.getItemId() == itemOneId)
            .extracting("waitingQueueTokenCount")
            .containsExactly(2L);

        assertThat(result.getInfoList())
            .filteredOn(it -> it.getItemId() == itemTwoId)
            .extracting("waitingQueueTokenCount")
            .containsExactly(3L);

        assertThat(result.getInfoList())
            .filteredOn(it -> it.getItemId() == itemThreeId)
            .extracting("waitingQueueTokenCount")
            .containsExactly(1L);

        assertThat(result.getInfoList())
            .filteredOn(it -> it.getItemId() == itemOneId)
            .extracting("entrySetTokenCount")
            .containsExactly(2L);

        assertThat(result.getInfoList())
            .filteredOn(it -> it.getItemId() == itemTwoId)
            .extracting("entrySetTokenCount")
            .containsExactly(0L);

        assertThat(result.getInfoList())
            .filteredOn(it -> it.getItemId() == itemThreeId)
            .extracting("entrySetTokenCount")
            .containsExactly(3L);
    }

    @Test
    void 일부_상품의_대기열_모니터링_정보_조회가_성공() {
        // given
        final var waitingQueueRepository = new FakeWaitingQueueRepository();
        final var entrySetRepository = new FakeEntrySetRepository();
        final var itemOneId = 1L;
        final var itemTwoId = 2L;
        final var itemThreeId = 3L;

        // itemOneId: 3개, itemTwoId: 2개, itemThreeId: 1개
        waitingQueueRepository.save(itemOneId, WaitingQueueToken.generate(1L, itemOneId), System.currentTimeMillis());
        waitingQueueRepository.save(itemOneId, WaitingQueueToken.generate(2L, itemOneId), System.currentTimeMillis());
        waitingQueueRepository.save(itemTwoId, WaitingQueueToken.generate(3L, itemTwoId), System.currentTimeMillis());
        waitingQueueRepository.save(itemTwoId, WaitingQueueToken.generate(4L, itemTwoId), System.currentTimeMillis());
        waitingQueueRepository.save(itemTwoId, WaitingQueueToken.generate(5L, itemTwoId), System.currentTimeMillis());
        waitingQueueRepository.save(itemThreeId, WaitingQueueToken.generate(6L, itemThreeId), System.currentTimeMillis());

        // itemOneId: 2개, itemTwoId: 0개, itemThreeId: 3개
        entrySetRepository.save(itemOneId, WaitingQueueToken.generate(1L, itemOneId));
        entrySetRepository.save(itemOneId, WaitingQueueToken.generate(2L, itemOneId));
        entrySetRepository.save(itemThreeId, WaitingQueueToken.generate(4L, itemThreeId));
        entrySetRepository.save(itemThreeId, WaitingQueueToken.generate(5L, itemThreeId));
        entrySetRepository.save(itemThreeId, WaitingQueueToken.generate(6L, itemThreeId));

        final var sut = new WaitingQueueTokenCountService(waitingQueueRepository, entrySetRepository);

        // when
        final var criteria = WaitingQueueTokenCountCriteria.createForSearchPartial(Set.of(itemOneId, itemThreeId));
        final var result = sut.countWaitingQueueToken(criteria);

        // then
        assertThat(result.getInfoList()).hasSize(2);

        assertThat(result.getInfoList())
            .extracting("itemId")
            .containsExactlyInAnyOrder(itemOneId, itemThreeId);

        assertThat(result.getInfoList())
            .filteredOn(it -> it.getItemId() == itemOneId)
            .extracting("waitingQueueTokenCount")
            .containsExactly(2L);

        assertThat(result.getInfoList())
            .filteredOn(it -> it.getItemId() == itemThreeId)
            .extracting("waitingQueueTokenCount")
            .containsExactly(1L);

        assertThat(result.getInfoList())
            .filteredOn(it -> it.getItemId() == itemOneId)
            .extracting("entrySetTokenCount")
            .containsExactly(2L);

        assertThat(result.getInfoList())
            .filteredOn(it -> it.getItemId() == itemThreeId)
            .extracting("entrySetTokenCount")
            .containsExactly(3L);
    }
}
