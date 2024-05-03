package com.walletapi.exception;

public class BadRequestNotFoundException extends RuntimeException {

    private final int errorCode;
    private final String message;

    public BadRequestNotFoundException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
