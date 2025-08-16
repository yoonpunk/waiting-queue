package com.practice.waitingqueue.common.exception;


import static com.practice.waitingqueue.common.exception.GlobalExceptionHandler.HandlerErrorCode.INVALID_PARAMETER;

import com.practice.waitingqueue.common.http.CommonResponse;
import com.practice.waitingqueue.domain.exception.WaitingQueueNotFoundException;
import com.practice.waitingqueue.domain.exception.WaitingQueueRegisterFailedException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 대기열 정보 없음 → 404
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {WaitingQueueNotFoundException.class})
    public CommonResponse<Void> handleWaitingQueueNotFound(WaitingQueueNotFoundException e) {
        final var message = String.format(
            "[WaitingQueueNotFoundException] itemId: %d, token: %s",
            e.getItemId(),
            e.getWaitingQueueToken().getValue()
        );

        LoggingHelper.logWithLevel(log, e.getLogLevel(), message, e);
        return CommonResponse.fail(e.getErrorCode(), e.getErrorCodeMessage());
    }

    /**
     * 대기열 등록 실패 → 409
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {WaitingQueueRegisterFailedException.class})
    public CommonResponse<Void> handleWaitingQueueRegisterFailed(WaitingQueueRegisterFailedException e) {
        final var message = String.format(
            "[WaitingQueueRegisterFailedException] token: %s",
            e.getItemId()
        );

        LoggingHelper.logWithLevel(log, e.getLogLevel(), message, e);
        return CommonResponse.fail(e.getErrorCode().name(), e.getErrorCode().getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public CommonResponse<Void> handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        final var firstValidation = e.getParameterValidationResults().stream().findFirst();
        if (firstValidation.isEmpty()) {
            log.warn("[HandlerMethodValidationException] 파라미터 검증결과가 존재하지 않습니다. 확인해주세요");
            return CommonResponse.fail(INVALID_PARAMETER.name(), INVALID_PARAMETER.errorMessage);
        }

        final var validation = firstValidation.get();
        String parameterName = validation.getMethodParameter().getParameterName();

        final var firstErrorResolve = validation.getResolvableErrors().stream().findFirst();
        if (firstErrorResolve.isEmpty()) {
            log.warn("[HandlerMethodValidationException] {} 파라미터의 검증 오류 메시지가 존재하지 않습니다. 확인해주세요", parameterName);
            return CommonResponse.fail(INVALID_PARAMETER.name(), INVALID_PARAMETER.errorMessage);
        }

        final var errorResolve = firstErrorResolve.get();
        final var errorMessage = errorResolve.getDefaultMessage();

        log.error("[HandlerMethodValidationException] {} 파라미터의 검증 오류가 발생했습니다. 오류 메시지: {}", parameterName, errorMessage);
        return CommonResponse.fail(INVALID_PARAMETER.name(), errorMessage);
    }

    private String extractFieldName(String propertyPath) {
        // propertyPath가 "methodName.parameterName" 형식이므로 마지막 부분을 필드명으로 사용
        return propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
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

    @AllArgsConstructor
    enum HandlerErrorCode {
        INVALID_PARAMETER("입력값이 유효하지 않습니다.")
        ;

        private final String errorMessage;
    }
}
