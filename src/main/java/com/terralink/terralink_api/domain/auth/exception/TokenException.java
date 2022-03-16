package com.terralink.terralink_api.domain.auth.exception;

public abstract class TokenException extends Exception {
    protected TokenException() {
        super("Unkown token error!");
    }

    protected TokenException(String message) {
        super(message);
    }
}
