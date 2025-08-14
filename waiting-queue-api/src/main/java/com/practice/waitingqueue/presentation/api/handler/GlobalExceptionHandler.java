package com.practice.waitingqueue.presentation.api.handler;

import com.practice.waitingqueue.common.http.CommonResponse;
import com.practice.waitingqueue.domain.exception.WaitingQueueNotFoundException;
import com.practice.waitingqueue.domain.exception.WaitingQueueRegisterFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 대기열 정보 없음 → 404
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {WaitingQueueNotFoundException.class})
    public CommonResponse<Void> handleWaitingQueueNotFound(WaitingQueueNotFoundException e) {
        log.info(String.format("[WaitingQueueNotFoundException] %s", e.getMessage()));
        return CommonResponse.fail(e.getErrorCode(), e.getErrorCodeMessage());
    }

    /**
     * 대기열 등록 실패 → 409
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {WaitingQueueRegisterFailedException.class})
    public CommonResponse<Void> handleWaitingQueueRegisterFailed(WaitingQueueRegisterFailedException e) {
        log.warn(String.format("[WaitingQueueRegisterFailedException] %s", e.getMessage()));
        return CommonResponse.fail(e.getErrorCode().name(), e.getErrorCode().getMessage());
    }

    /**
     * 그 외 예상치 못한 예외 → 500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CommonResponse<Void> handleUnknownException(Exception e) {
        log.error(String.format("[UnknownException] %s", e.getMessage()));
        return CommonResponse.fail("UNKNOWN", "알 수 없는 오류가 발생했습니다.");
    }
}
