package com.practice.waitingqueue.domain.service;

import com.practice.waitingqueue.domain.dto.WaitingQueueMonitoringCriteria;
import com.practice.waitingqueue.domain.info.WaitingQueueMonitoringInfo;
import com.practice.waitingqueue.domain.info.WaitingQueueMonitoringInfoList;
import com.practice.waitingqueue.domain.repository.WaitingQueueRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class WaitingQueueMonitoringService {

    private final WaitingQueueRepository waitingQueueRepository;

    public WaitingQueueMonitoringInfoList monitorWaitingQueueList(
        WaitingQueueMonitoringCriteria criteria
    ) {
        if (criteria.isSearchAll()) {
            final var queueingItemIdList = waitingQueueRepository.findAllQueueingItemIdList();
            return monitorWaitingQueueList(queueingItemIdList);
        } else {
            return monitorWaitingQueueList(criteria.getItemIdListToSearch());
        }
    }

    private WaitingQueueMonitoringInfoList monitorWaitingQueueList(
        Set<Long> itemIdListToSearch
    ) {
        if (CollectionUtils.isEmpty(itemIdListToSearch)) {
            return WaitingQueueMonitoringInfoList.empty();
        }

        final var waitingQueueMonitoringInfoList = itemIdListToSearch.stream()
            .map(
                itemId -> WaitingQueueMonitoringInfo.of(
                    itemId,
                    waitingQueueRepository.countWaitingQueueTokenByItemId(itemId)
                )
            ).toList();

        // 특정 아이템 ID 목록에 대한 대기열 모니터링 로직 구현
        return WaitingQueueMonitoringInfoList.of(waitingQueueMonitoringInfoList);
    }
}
