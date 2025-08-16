package com.practice.waitingqueue.domain.exception;

import com.practice.waitingqueue.common.exception.CommonException;
import com.practice.waitingqueue.common.exception.LogLevel;
import com.practice.waitingqueue.domain.entity.WaitingQueueToken;
import lombok.Getter;

@Getter
public class WaitingQueueNotFoundException extends CommonException {

    private final static LogLevel DEFAULT_LOG_LEVEL = LogLevel.INFO;

    private final Long itemId;
    private final WaitingQueueToken waitingQueueToken;
    private final String errorCode = "WAITING_QUEUE_NOT_FOUND";
    private final String errorCodeMessage = "대기열 정보가 존재하지 않습니다.";

    public WaitingQueueNotFoundException(Long itemId, WaitingQueueToken waitingQueueToken) {
        super(buildMessage(itemId, waitingQueueToken), DEFAULT_LOG_LEVEL);
        this.itemId = itemId;
        this.waitingQueueToken = waitingQueueToken;
    }

    public WaitingQueueNotFoundException(Long itemId, WaitingQueueToken waitingQueueToken, Throwable cause) {
        super(buildMessage(itemId, waitingQueueToken), cause, DEFAULT_LOG_LEVEL);
        this.itemId = itemId;
        this.waitingQueueToken = waitingQueueToken;
    }

    private static String buildMessage(Long itemId, WaitingQueueToken token) {
        return "WaitingQueue not found. itemId=" + itemId + ", token=" + token.getValue();
    }
}
