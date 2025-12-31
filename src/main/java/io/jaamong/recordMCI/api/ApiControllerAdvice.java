package io.jaamong.recordMCI.api;

import io.jaamong.recordMCI.api.exception.CustomRuntimeException;
import io.jaamong.recordMCI.api.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiResponse<Object> bindException(BindException e) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                null
        );
    }

    @ExceptionHandler
    public ApiResponse<ErrorCode> customRuntimeException(CustomRuntimeException e) {
        return ApiResponse.of(
                e.getStatus(),
                e.getMessage(),
                null
        );
    }
}
