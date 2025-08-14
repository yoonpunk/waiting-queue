package com.practice.waitingqueue.domain.entity;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class WaitingQueueToken {
    private String value;

    /**
     * 대기열 토큰은 다음과 같은 형식으로 생성합니다.
     * {userId}-{itemId}-{UUID}
     * userId가 포함된 이유: 모종의 이유로 다른 유저에게 대기열 토큰이 탈취되어도 해당 토큰의 포함된 유저정보를 통해 본래 유저가 아닌 경우 사용이 불가하도록 막기 위함
     * itemId가 포함된 이유: 대기열 키만 보더라도 어떤 상품에 대한 것인지 알아볼 수 있게 하기 위함
     */
    public static WaitingQueueToken generate(long userId, long itemId) {
        final var token = String.format("%d-%d-%s", userId, itemId, UUID.randomUUID().toString());
        return new WaitingQueueToken(token);
    }

    public static WaitingQueueToken validateAndCreate(long userId, long itemId, String value) {
        validate(userId, itemId, value);
        return new WaitingQueueToken(value);
    }

    public static WaitingQueueToken createWithoutValidation(String value) {
        return new WaitingQueueToken(value);
    }

    /**
     * 대기열 토큰이 올바른 형태인지 검증합니다.
     * 대기열 토큰을 소유하는 유저와 토큰이 속한 대기열의 상품만 확인해도 충분하므로 이 두 가지만 검증합니다.
     */
    private static void validate(long userId, long itemId, String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("WaitingQueueToken is null or blank");
        }

        final var tokenPrefix = String.format("%d-%d-", userId, itemId);
        final var hasCorrectPrefix = value.startsWith(tokenPrefix);
    }
}
