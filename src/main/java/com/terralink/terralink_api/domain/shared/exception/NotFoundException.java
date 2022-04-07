package com.terralink.terralink_api.domain.shared.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class NotFoundException extends Exception {
    @Getter
    private String entityName;

    @Getter
    private String identifier;
}
