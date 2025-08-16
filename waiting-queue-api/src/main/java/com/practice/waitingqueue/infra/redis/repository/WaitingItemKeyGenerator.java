package com.practice.waitingqueue.infra.redis.repository;

public class WaitingItemKeyGenerator {

    private static final String WAITING_ITEM_PREFIX = "waitingItem:";

    public static String generate(long itemId) {
        return WAITING_ITEM_PREFIX + itemId;
    }
}
