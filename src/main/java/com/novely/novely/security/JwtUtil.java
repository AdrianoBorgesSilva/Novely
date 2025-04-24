package com.novely.novely.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    private final String secret; 
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 2; // 2 horas

    private final SecretKey key;
    private final JwtParser parser;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        
        if (secret == null || secret.length() < 32) {
            throw new RuntimeException("Invalid JWT key! Please set 'jwt.secret' in application.properties to at least 32 characters.");
        }
        
        this.secret = secret;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.parser = Jwts.parser().verifyWith(key).build();
    }

    public String generateToken(CustomUserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, CustomUserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return parser.parseSignedClaims(token).getPayload();
    }
}
