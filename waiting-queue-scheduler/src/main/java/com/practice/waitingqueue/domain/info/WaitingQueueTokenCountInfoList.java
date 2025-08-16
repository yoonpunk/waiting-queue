package com.practice.waitingqueue.domain.info;

import java.util.List;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class WaitingQueueTokenCountInfoList {

    private final List<WaitingQueueTokenCountInfo> infoList;

    public static WaitingQueueTokenCountInfoList empty() {
        return new WaitingQueueTokenCountInfoList(List.of());
    }

    public Stream<WaitingQueueTokenCountInfo> stream() {
        return infoList.stream();
    }
}
