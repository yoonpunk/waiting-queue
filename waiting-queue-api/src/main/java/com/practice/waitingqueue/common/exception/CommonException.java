package com.practice.waitingqueue.common.exception;

public class CommonException extends RuntimeException {

    private final LogLevel logLevel;

    public CommonException(String message) {
        super(message);
        this.logLevel = LogLevel.INFO;
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
        this.logLevel = LogLevel.INFO;
    }

    public CommonException(String message, LogLevel level) {
        super(message);
        this.logLevel = level;
    }

    public CommonException(String message, Throwable cause, LogLevel level) {
        super(message, cause);
        this.logLevel = LogLevel.INFO;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }
}
