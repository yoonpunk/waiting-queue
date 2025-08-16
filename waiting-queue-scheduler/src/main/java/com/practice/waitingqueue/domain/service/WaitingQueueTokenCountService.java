package com.practice.waitingqueue.domain.service;

import com.practice.waitingqueue.domain.dto.WaitingQueueTokenCountCriteria;
import com.practice.waitingqueue.domain.info.WaitingQueueTokenCountInfo;
import com.practice.waitingqueue.domain.info.WaitingQueueTokenCountInfoList;
import com.practice.waitingqueue.domain.repository.EntrySetRepository;
import com.practice.waitingqueue.domain.repository.WaitingQueueRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class WaitingQueueTokenCountService {

    private final WaitingQueueRepository waitingQueueRepository;
    private final EntrySetRepository entrySetRepository;

    public WaitingQueueTokenCountInfoList countWaitingQueueToken(
        WaitingQueueTokenCountCriteria criteria
    ) {
        if (criteria.isSearchAll()) {
            final var queueingItemIdList = waitingQueueRepository.findAllQueueingItemIdList();
            return countWaitingQueueList(queueingItemIdList);
        } else {
            return countWaitingQueueList(criteria.getItemIdListToSearch());
        }
    }

    private WaitingQueueTokenCountInfoList countWaitingQueueList(
        Set<Long> itemIdListToSearch
    ) {
        if (CollectionUtils.isEmpty(itemIdListToSearch)) {
            return WaitingQueueTokenCountInfoList.empty();
        }

        final var countInfoList = itemIdListToSearch.stream()
            .map(
                itemId -> WaitingQueueTokenCountInfo.of(
                    itemId,
                    waitingQueueRepository.countWaitingQueueTokenByItemId(itemId),
                    entrySetRepository.countEntrySetTokenByItemId(itemId)
                )
            ).toList();

        return WaitingQueueTokenCountInfoList.of(countInfoList);
    }
}
