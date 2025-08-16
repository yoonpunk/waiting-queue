package com.practice.waitingqueue.domain.info;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class WaitingQueueTokenCountInfo {

    private final long itemId;                           // 상품 고유번호
    private final long waitingQueueTokenCount;           // 대기열에 있는 토큰 수
    private final long entrySetTokenCount;               // 입장셋에 있는 토큰 수
}
