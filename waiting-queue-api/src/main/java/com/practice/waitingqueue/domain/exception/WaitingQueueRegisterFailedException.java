package com.practice.waitingqueue.domain.exception;

import lombok.Getter;

@Getter
public class WaitingQueueRegisterFailedException extends RuntimeException {

    private final Long itemId;
    private final String waitingQueueToken;
    private final String errorCode = "WAITING_QUEUE_REGISTER_FAILED";
    private final String errorCodeMessage = "대기열 등록에 실패했습니다.";

    public WaitingQueueRegisterFailedException(Long itemId, String waitingQueueToken) {
        super(buildMessage(itemId, waitingQueueToken));
        this.itemId = itemId;
        this.waitingQueueToken = waitingQueueToken;
    }

    public WaitingQueueRegisterFailedException(Long itemId, String waitingQueueToken, Throwable cause) {
        super(buildMessage(itemId, waitingQueueToken), cause);
        this.itemId = itemId;
        this.waitingQueueToken = waitingQueueToken;
    }

    private static String buildMessage(Long itemId, String token) {
        return "WaitingQueue register failed. itemId=" + itemId + ", token=" + token;
    }
}
