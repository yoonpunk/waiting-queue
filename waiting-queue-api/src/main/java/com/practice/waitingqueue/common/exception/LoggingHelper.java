package com.practice.waitingqueue.common.exception;

import org.slf4j.Logger;

public class LoggingHelper {

    public static void logWithLevel(Logger logger, LogLevel level, String message, Throwable t) {
        switch (level) {
            case INFO -> logger.info(message);
            case WARN -> logger.warn(message, t);
            case ERROR -> logger.error(message, t);
            default -> logger.error(message, t);
        }
    }
}
