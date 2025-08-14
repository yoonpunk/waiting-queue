package com.practice.waitingqueue.presentation.scheduler;

import com.practice.waitingqueue.domain.service.MoveTokenFromWaitingQueueToEntrySetService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class MoveTokenFromWaitingQueueToEntrySetScheduler {

    private final MoveTokenFromWaitingQueueToEntrySetService moveTokenFromWaitingQueueToEntrySetService;

    @Scheduled(fixedDelay = 2000) // 이전 스케쥴 종료 후 2초 뒤 실행, 중첩실행을 방지
    public void moveTokens() {
        StopWatch stopWatch = new StopWatch();

        try {
            log.info("[MoveTokenFromWaitingQueueToEntrySetScheduler] start");
            stopWatch.start();
            moveTokenFromWaitingQueueToEntrySetService.moveWaitingQueueTokens();
        } finally {
            stopWatch.stop();
            log.info("[MoveTokenFromWaitingQueueToEntrySetScheduler] end in {} ms", stopWatch.getTotalTimeMillis());
        }
    }
}
