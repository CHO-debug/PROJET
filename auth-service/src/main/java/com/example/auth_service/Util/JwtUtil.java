package com.example.auth_service.Util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    // Using a sufficiently secure key for HS256
    private static final String SECRET = "mySecretKeyMySecretKeyMySecretKeyMySecretKey";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Token validity (e.g., 10 hours)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    public static String generateToken(String userId, String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId) // Setting userId as subject as well for easier retrieval
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }
}
