package com.terralink.terralink_api.http.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terralink.terralink_api.domain.auth.exception.TokenException;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable exception) {
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        if (exception instanceof TokenException) {
           return this.handleTokenException(exchange, (TokenException) exception);
        }

        return this.handleUnkownException(exchange, exception);

    }

    private Mono<Void> handleTokenException(ServerWebExchange exchange, TokenException exception) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().writeWith(
            Mono
                .just(ApiResponse.create(false, exception.getMessage(), null))
                .map(this::getJsonBytesFromData)
                .map(exchange.getResponse().bufferFactory()::wrap)
        );   
    }

    private Mono<Void> handleUnkownException(ServerWebExchange exchange, Throwable exception) {
        GlobalExceptionHandler.log.error("error:", exception);
    
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return exchange.getResponse().writeWith(
            Mono
                .just(ApiResponse.create(false, "Internal server error!", null))
                .map(this::getJsonBytesFromData)
                .map(exchange.getResponse().bufferFactory()::wrap)
        );
    }

    private byte[] getJsonBytesFromData(Object data) {
        try {
            return this.objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            GlobalExceptionHandler.log.error("jsonParserError:", e);
            return new byte[] {};
        }
    }
    
}
