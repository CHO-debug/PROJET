package com.example.api_gateway.Config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * Configuration centrale du Gateway pour la sécurité JWT.
 * Fournit la clé secrète utilisée pour valider les JWT (cookie HttpOnly).
 */
@Configuration
public class GatewayConfig {

    /**
     * Clé secrète JWT (DOIT être la même que dans auth-service)
     */
    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Bean SecretKey utilisé par JwtAuthenticationFilter
     * pour vérifier la signature des JWT.
     */
    @Bean
    public SecretKey jwtSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
