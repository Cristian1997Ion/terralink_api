package com.terralink.terralink_api.http.auth.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginPayload {
    private String username;
    private String token;
}
