package com.terralink.terralink_api.domain.auth.exception;

public class BadTokenException extends TokenException {
    public BadTokenException() {
        super("Invalid token");
    }
}
