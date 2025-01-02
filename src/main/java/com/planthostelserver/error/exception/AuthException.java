package com.planthostelserver.error.exception;

import com.planthostelserver.error.type.ErrorResponse;
import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

    private final int status;
    private final String message;

    public AuthException(ErrorResponse errorResponse) {
        this.status = errorResponse.getHttpStatus().value();
        this.message = errorResponse.getMessage();
    }
}
