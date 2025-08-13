package com.practice.waitingqueue.infra.redis.repository;

public class EntrySetKeyGenerator {

    private static final String ENTRY_SET_PREFIX = "entrySet:";

    public static String generate(long itemId) {
        return ENTRY_SET_PREFIX + itemId;
    }
}