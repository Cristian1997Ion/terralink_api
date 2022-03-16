package com.terralink.terralink_api.domain.auth.exception;

public class ExpiredTokenException extends TokenException {
    public ExpiredTokenException() {
        super("Expired token!");
    }
}
