package com.example.api_gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Filtre global pour logger les requêtes et réponses.
 * Ne logue jamais d'informations sensibles (token, mot de passe, email).
 */
@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String START_TIME_ATTR = "requestStartTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String method = (request.getMethod() != null) ? request.getMethod().name() : "UNKNOWN";
        String path = request.getPath().value();
        String correlationId = MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);

        // Enregistre l'heure de début
        long startTime = System.currentTimeMillis();
        exchange.getAttributes().put(START_TIME_ATTR, startTime);

        logger.info("Request: {} {} [correlationId={}]", method, path, correlationId);

        return chain.filter(exchange)
                .doFinally(signalType -> {
                    Long start = exchange.getAttribute(START_TIME_ATTR);
                    long duration = (start != null) ? System.currentTimeMillis() - start : 0;
                    int status = (exchange.getResponse().getStatusCode() != null)
                            ? exchange.getResponse().getStatusCode().value()
                            : 0;

                    logger.info("Response: {} {} [status={}, duration={}ms, correlationId={}]",
                            method, path, status, duration, correlationId);
                });
    }

    @Override
    public int getOrder() {
        // Après CorrelationIdFilter mais avant JwtAuthenticationFilter
        return Ordered.HIGHEST_PRECEDENCE + 5;
    }
}
