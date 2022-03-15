package com.terralink.terralink_api.domain.auth.service;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthService implements ReactiveAuthenticationManager {
    private JWTService jwtService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        Claims tokenClaims = this.jwtService.getAllClaimsFromToken(token);

        return Mono.just(this.jwtService.validateTokenWithClaims(tokenClaims))
            .filter(valid -> valid)
            .switchIfEmpty(Mono.empty())
            .map(valid -> authentication);
    }


}
