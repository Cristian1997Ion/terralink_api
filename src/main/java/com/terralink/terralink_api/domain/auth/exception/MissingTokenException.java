package com.terralink.terralink_api.domain.auth.exception;

public final class MissingTokenException extends TokenException {
    public MissingTokenException() {
        super("Missing token!");
    }
}
