package com.practice.waitingqueue.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.practice.waitingqueue.domain.dto.WaitingQueueMonitoringCriteria;
import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import com.practice.waitingqueue.testdouble.FakeWaitingQueueRepository;
import java.util.Set;
import org.junit.jupiter.api.Test;

class WaitingQueueMonitoringServiceTest {

    @Test
    void 전체_대기열의_모니터링_정보_조회가_성공() {
        // given
        final var waitingQueueRepository = new FakeWaitingQueueRepository();
        final var itemOneId = 1L;
        final var itemTwoId = 2L;
        final var itemThreeId = 3L;

        waitingQueueRepository.save(itemOneId, WaitingQueueToken.generate(1L, itemOneId), System.currentTimeMillis());
        waitingQueueRepository.save(itemOneId, WaitingQueueToken.generate(2L, itemOneId), System.currentTimeMillis());
        waitingQueueRepository.save(itemTwoId, WaitingQueueToken.generate(3L, itemTwoId), System.currentTimeMillis());
        waitingQueueRepository.save(itemTwoId, WaitingQueueToken.generate(4L, itemTwoId), System.currentTimeMillis());
        waitingQueueRepository.save(itemTwoId, WaitingQueueToken.generate(5L, itemTwoId), System.currentTimeMillis());
        waitingQueueRepository.save(itemThreeId, WaitingQueueToken.generate(6L, itemThreeId), System.currentTimeMillis());

        final var sut = new WaitingQueueMonitoringService(waitingQueueRepository);

        // when
        final var criteria = WaitingQueueMonitoringCriteria.createForSearchAll();
        final var result = sut.monitorWaitingQueueList(criteria);

        // then
        assertThat(result.getWaitingQueueMonitoringInfoList()).hasSize(3);

        assertThat(result.getWaitingQueueMonitoringInfoList())
            .extracting("itemId")
            .containsExactlyInAnyOrder(itemOneId, itemTwoId, itemThreeId);

        assertThat(result.getWaitingQueueMonitoringInfoList())
            .filteredOn(it -> it.getItemId() == itemOneId)
            .extracting("queueingTokenCount")
            .containsExactly(2L);

        assertThat(result.getWaitingQueueMonitoringInfoList())
            .filteredOn(it -> it.getItemId() == itemTwoId)
            .extracting("queueingTokenCount")
            .containsExactly(3L);

        assertThat(result.getWaitingQueueMonitoringInfoList())
            .filteredOn(it -> it.getItemId() == itemThreeId)
            .extracting("queueingTokenCount")
            .containsExactly(1L);
    }

    @Test
    void 일부_상품의_대기열_모니터링_정보_조회가_성공() {
        // given
        final var waitingQueueRepository = new FakeWaitingQueueRepository();
        final var itemOneId = 1L;
        final var itemTwoId = 2L;
        final var itemThreeId = 3L;

        waitingQueueRepository.save(itemOneId, WaitingQueueToken.generate(1L, itemOneId), System.currentTimeMillis());
        waitingQueueRepository.save(itemOneId, WaitingQueueToken.generate(2L, itemOneId), System.currentTimeMillis());
        waitingQueueRepository.save(itemTwoId, WaitingQueueToken.generate(3L, itemTwoId), System.currentTimeMillis());
        waitingQueueRepository.save(itemTwoId, WaitingQueueToken.generate(4L, itemTwoId), System.currentTimeMillis());
        waitingQueueRepository.save(itemTwoId, WaitingQueueToken.generate(5L, itemTwoId), System.currentTimeMillis());
        waitingQueueRepository.save(itemThreeId, WaitingQueueToken.generate(6L, itemThreeId), System.currentTimeMillis());

        final var sut = new WaitingQueueMonitoringService(waitingQueueRepository);

        // when
        final var criteria = WaitingQueueMonitoringCriteria.createForSearchPartial(Set.of(itemOneId, itemThreeId));
        final var result = sut.monitorWaitingQueueList(criteria);

        // then
        assertThat(result.getWaitingQueueMonitoringInfoList()).hasSize(2);

        assertThat(result.getWaitingQueueMonitoringInfoList())
            .extracting("itemId")
            .containsExactlyInAnyOrder(itemOneId, itemThreeId);

        assertThat(result.getWaitingQueueMonitoringInfoList())
            .filteredOn(it -> it.getItemId() == itemOneId)
            .extracting("queueingTokenCount")
            .containsExactly(2L);

        assertThat(result.getWaitingQueueMonitoringInfoList())
            .filteredOn(it -> it.getItemId() == itemThreeId)
            .extracting("queueingTokenCount")
            .containsExactly(1L);
    }
}
