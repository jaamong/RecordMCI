package io.jaamong.recordMCI.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomRuntimeException extends RuntimeException {

    private HttpStatus status;
    protected ErrorCode errorCode;

    public CustomRuntimeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CustomRuntimeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
