package com.practice.waitingqueue.domain.exception;

import com.practice.waitingqueue.common.exception.CommonException;
import com.practice.waitingqueue.common.exception.LogLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class WaitingQueueRegisterFailedException extends CommonException {

    private final static LogLevel DEFAULT_LOG_LEVEL = LogLevel.INFO;

    private final Long itemId;
    private final ErrorCode errorCode;

    public WaitingQueueRegisterFailedException(Long itemId, ErrorCode errorCode) {
        super(errorCode.message, DEFAULT_LOG_LEVEL);
        this.errorCode = errorCode;
        this.itemId = itemId;
    }

    public WaitingQueueRegisterFailedException(Long itemId, ErrorCode errorCode, Throwable cause) {
        super(errorCode.message, cause, DEFAULT_LOG_LEVEL);
        this.errorCode = errorCode;
        this.itemId = itemId;
    }

    public WaitingQueueRegisterFailedException(Long itemId, ErrorCode errorCode, LogLevel logLevel) {
        super(errorCode.message, logLevel);
        this.errorCode = errorCode;
        this.itemId = itemId;
    }

    public WaitingQueueRegisterFailedException(Long itemId, ErrorCode errorCode, Throwable cause, LogLevel logLevel) {
        super(errorCode.message, cause, logLevel);
        this.errorCode = errorCode;
        this.itemId = itemId;
    }

    @Getter
    @AllArgsConstructor
    public
    enum ErrorCode {
        ITEM_CANNOT_USE_WAITING_QUEUE("대기열 등록이 더이상 불가한 상품입니다.");

        private final String message;
    }
}
