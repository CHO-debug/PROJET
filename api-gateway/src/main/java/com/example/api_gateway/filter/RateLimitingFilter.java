package com.example.api_gateway.filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Filtre global pour limiter le nombre de requêtes par client (IP).
 * Limite simple en mémoire pour démonstration.
 */
@Component
public class RateLimitingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitingFilter.class);

    // Limite : 10 requêtes par minute par IP (exemple)
    private static final int MAX_REQUESTS_PER_MINUTE = 10;

    private final Map<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String clientIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();

        RequestCounter counter = requestCounts.computeIfAbsent(clientIp, k -> new RequestCounter());

        synchronized (counter) {
            long now = Instant.now().toEpochMilli();
            if (now - counter.timestamp > 60_000) { // reset toutes les 60 secondes
                counter.timestamp = now;
                counter.count.set(0);
            }

            if (counter.count.incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
                logger.warn("Rate limit exceeded for IP: {}", clientIp);
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // Placer après CorrelationIdFilter, avant JWT filter
        return Ordered.HIGHEST_PRECEDENCE + 20;
    }

    // Classe interne pour stocker le compteur et timestamp
    private static class RequestCounter {
        AtomicInteger count = new AtomicInteger(0);
        long timestamp = Instant.now().toEpochMilli();
    }
}
