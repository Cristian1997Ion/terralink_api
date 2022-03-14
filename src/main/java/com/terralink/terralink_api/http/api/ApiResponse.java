package com.terralink.terralink_api.http.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResponse {
    protected Boolean success = true;
    protected String message = null;
}
