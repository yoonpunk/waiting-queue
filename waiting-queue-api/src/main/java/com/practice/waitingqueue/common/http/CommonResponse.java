package com.practice.waitingqueue.common.http;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonResponse<T> {

    private final State state;          // 응답 상태
    private final String errorCode;     // 에러코드
    private final String errorMessage;  // 에러메시지
    private final T data;               // 응답 데이터

    public static <T> CommonResponse<T> success(final T data) {
        return new CommonResponse<T>(
            State.SUCCESS,
            null,
            null,
            data
        );
    }

    public static CommonResponse<Void> success() {
        return new CommonResponse<Void>(
            State.SUCCESS,
            null,
            null,
            null
        );
    }

    public static <T> CommonResponse<T> fail(final String errorCode, final String errorMessage) {
        return new CommonResponse<T>(
            State.FAIL,
            errorCode,
            errorMessage,
            null
        );
    }

    public static <T> CommonResponse<T> fail(final String errorCode) {
        return new CommonResponse<T>(
            State.FAIL,
            errorCode,
            null,
            null
        );
    }

    private enum State {
        SUCCESS,
        FAIL
    }
}
