package com.practice.waitingqueue.infra.redis.repository;

public class WaitingQueueKeyGenerator {

    private static final String WAITING_QUEUE_KEY_PREFIX = "waitingQueue:";

    public static String generate(long itemId) {
        return WAITING_QUEUE_KEY_PREFIX + itemId;
    }
}