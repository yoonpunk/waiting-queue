package com.practice.waitingqueue.interfaces.consumer;

import com.practice.waitingqueue.domain.service.WaitingItemSyncService;
import com.practice.waitingqueue.interfaces.dto.WaitingItemChangedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * WaitingItemChangedConsumer는 상품 서비스에서 상품의 상태가 변경될 때 발행되는 상품 정보 메시지를 수신하는 클래스입니다.
 * 해당 예제 프로젝트에는 실제로 카프카 메시지를 발행하지 않으니 예시 코드로 동작만 작성해두었습니다.
 * 실제 프로젝트에서는 대기열 관련 상품의 제어가 필요하다면 카프카 설정 후 해당 컨슈머를 사용하여 카프카 메시지를 수신하여 사용하면 됩니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingItemChangedConsumer {

    private final WaitingItemSyncService waitingItemSyncService;

    /**
     * 카프카 메시지를 수신하여 대기열 관련 상품 정보를 동기화합니다. 대기열 대상 상품에 대한 메시지가 아니면 처리하지 않습니다.
     * itemId를 메시지 키로 사용하여 상품별 순차처리가 보장될 수 있도록 합니다.
     */
    // @KafkaListener(topics = "item-changed-event", groupId = "waiting-queue", containerFactory = "defaultKafkaListenerContainerFactory")
    public void consume(WaitingItemChangedMessage message) {
        log.info("[WaitingItemChangedConsumer] consume message: {}", message.toString());

        var isNotWaitingQueueTargetItem = !message.isWaitingQueueTargetItem();
        if (isNotWaitingQueueTargetItem) {
            return;
        }

        waitingItemSyncService.sync(message.toWaitingItemSyncCommand());
    }
}
