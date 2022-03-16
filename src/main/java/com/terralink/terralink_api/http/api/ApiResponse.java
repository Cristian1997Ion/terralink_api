package com.terralink.terralink_api.http.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResponse {
    protected Boolean success = true;
    protected String message = null;
    protected Object payload = null;

    public static ApiResponse create(Boolean success, String message, Object payload) {
        return new ApiResponse(success, message, payload);
    }
}
