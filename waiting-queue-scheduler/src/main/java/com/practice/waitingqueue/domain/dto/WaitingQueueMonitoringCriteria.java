package com.practice.waitingqueue.domain.dto;

import java.util.Collections;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class WaitingQueueMonitoringCriteria {

    private final boolean searchAll;             // 전체 조회 여부
    private final Set<Long> itemIdListToSearch;  // 부분 조회 시, 조회할 itemId 리스트

    public static WaitingQueueMonitoringCriteria createForSearchAll() {
        return new WaitingQueueMonitoringCriteria(true, Collections.emptySet());
    }

    public static WaitingQueueMonitoringCriteria createForSearchPartial(Set<Long> itemIdListToSearch) {
        if (CollectionUtils.isEmpty(itemIdListToSearch)) {
            throw new IllegalArgumentException("부분 조회 시에는 itemId 리스트가 비어있을 수 없습니다.");
        }

        return new WaitingQueueMonitoringCriteria(false, itemIdListToSearch);
    }
}
