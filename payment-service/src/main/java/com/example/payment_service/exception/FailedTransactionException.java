package com.example.payment_service.exception;

import lombok.Getter;

@Getter
public class FailedTransactionException extends Exception {
    private final ErrorBody errorBody;

    public FailedTransactionException(String message) {
        super(message);
        this.errorBody = new ErrorBody(message, "FailedTransaction");
    }
}
