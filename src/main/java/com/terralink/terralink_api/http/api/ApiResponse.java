package com.terralink.terralink_api.http.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public final class ApiResponse<T> {
    protected Boolean success = true;
    protected String message = null;
    protected T payload = null;
}
