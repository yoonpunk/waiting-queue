package com.practice.waitingqueue.domain.entity;

public class WaitingQueueTokenGenerator {

    /**
     * 대기열 토큰은 다음과 같은 형식으로 생성합니다.
     * {userId}-{itemId}-{UUID}
     * userId가 포함된 이유: 모종의 이유로 다른 유저에게 대기열 토큰이 탈취되어도 해당 토큰의 포함된 유저정보를 통해 본래 유저가 아닌 경우 사용이 불가하도록 막기 위함
     * itemId가 포함된 이유: 대기열 키만 보더라도 어떤 상품에 대한 것인지 알아볼 수 있게 하기 위함
     */
    public static String generate(long userId, long itemId) {
        return String.format("%d-%d-%s", userId, itemId, java.util.UUID.randomUUID().toString());
    }
}
