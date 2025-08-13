package com.practice.waitingqueue.domain.repository;

public interface WaitingQueueRepository {

    void save(long itemId, String waitingQueueToken, long score);
}
