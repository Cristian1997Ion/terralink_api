package com.terralink.terralink_api.domain.auth.service;

import com.terralink.terralink_api.domain.auth.exception.BadTokenException;
import com.terralink.terralink_api.domain.auth.exception.ExpiredTokenException;
import com.terralink.terralink_api.domain.auth.exception.MissingTokenException;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthService implements ReactiveAuthenticationManager {
    private JWTService jwtService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono
            .just(authentication.getCredentials().toString())
            .filter(token -> token != null && token.length() > 0)
            .switchIfEmpty(Mono.error(new MissingTokenException()))
            .map(token -> this.jwtService.getAllClaimsFromToken(token))
            .onErrorResume(ExpiredJwtException.class, error -> Mono.error(new ExpiredTokenException()))
            .onErrorResume(JwtException.class, error -> Mono.error(new BadTokenException()))
            .map(claims ->  (Authentication) new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                null
            ));
    }


}
