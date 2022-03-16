package com.terralink.terralink_api.domain.auth.service;

import java.security.Key;
import java.util.Date;

import javax.annotation.PostConstruct;

import com.terralink.terralink_api.domain.user.entity.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTService {
    @Value("${jjwt.secret}")
    private String secret;

    @Value("${jjwt.expiration}")
    private String expirationTime;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(this.secret.getBytes());
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(token).getBody();
    }

    public String generateToken(User user) {
        final Long expirationTimeLong = Long.parseLong(this.expirationTime); //in second
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(this.key)
                .compact();
    }
}
