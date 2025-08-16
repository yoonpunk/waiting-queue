package com.practice.waitingqueue.domain.info;

import java.util.List;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class WaitingQueueMonitoringInfoList {

    private final List<WaitingQueueMonitoringInfo> waitingQueueMonitoringInfoList;

    public static WaitingQueueMonitoringInfoList empty() {
        return new WaitingQueueMonitoringInfoList(List.of());
    }

    public Stream<WaitingQueueMonitoringInfo> stream() {
        return waitingQueueMonitoringInfoList.stream();
    }
}
