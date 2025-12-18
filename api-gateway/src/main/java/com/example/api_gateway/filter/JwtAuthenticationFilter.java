package com.example.api_gateway.filter;

import com.example.api_gateway.dto.GatewayErrorResponse;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String JWT_COOKIE_NAME = "JWT";
    private static final String AUTH_PATH_PREFIX = "/auth";
    private static final String USER_ID_HEADER = "X-User-Id";

    private final SecretKey secretKey;

    public JwtAuthenticationFilter(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        // Skip JWT validation for auth endpoints
        if (path.startsWith(AUTH_PATH_PREFIX)) {
            return chain.filter(exchange);
        }

        // Lire le cookie JWT
        var cookies = request.getCookies().getFirst(JWT_COOKIE_NAME);
        if (cookies == null) {
            return onUnauthorized(exchange, "JWT cookie manquant");
        }

        String token = cookies.getValue();

        try {
            // Valider le JWT
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            String role = claims.get("role", String.class);

            if (userId == null || userId.isEmpty()) {
                return onUnauthorized(exchange, "Token invalide : userId manquant");
            }

            // Ici tu peux ajouter une vérification role si nécessaire
            if (!role.equals("EMPLOYE") && !role.equals("RH")) {
                return onUnauthorized(exchange, "Rôle non autorisé");
            }

            // Injection du header X-User-Id
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header(USER_ID_HEADER, userId)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (ExpiredJwtException e) {
            return onUnauthorized(exchange, "Token expiré");
        } catch (JwtException e) {
            return onUnauthorized(exchange, "Token invalide");
        } catch (Exception e) {
            return onUnauthorized(exchange, "Erreur de traitement du JWT");
        }
    }

    private Mono<Void> onUnauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = String.format(
                "{\"code\":\"AUTH_FAILED\",\"message\":\"%s\",\"timestamp\":\"%s\",\"path\":\"%s\"}",
                message,
                Instant.now(),
                exchange.getRequest().getPath().value()
        );

        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8))));
    }

    @Override
    public int getOrder() {
        // Après CorrelationIdFilter et RequestLoggingFilter
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}
