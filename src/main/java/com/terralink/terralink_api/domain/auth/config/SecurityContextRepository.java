package com.terralink.terralink_api.domain.auth.config;

import com.terralink.terralink_api.domain.auth.service.AuthService;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private AuthService authService;

    @Override
    public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange swe) {
        return Mono.justOrEmpty(swe.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
            .filter(authHeader -> authHeader.startsWith("Bearer "))
            .defaultIfEmpty("")
            .flatMap(authHeader -> {
                String authToken = authHeader.length() > 7 ? authHeader.substring(7): authHeader;
                Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
                return this
                    .authService
                    .authenticate(auth)
                    .map(SecurityContextImpl::new);
            });
    }
}