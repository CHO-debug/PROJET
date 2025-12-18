package com.example.api_gateway.filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Filtre global pour gérer le Correlation ID.
 * - Génère un ID unique si absent
 * - Propagation de l'ID aux services en aval
 * - Ajout dans la réponse HTTP pour traçabilité
 */
@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(CorrelationIdFilter.class);

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    public static final String CORRELATION_ID_MDC_KEY = "correlationId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Vérifie si un correlation ID existe
        String correlationId = request.getHeaders().getFirst(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
            logger.debug("Generated new correlation ID: {}", correlationId);
        } else {
            logger.debug("Using existing correlation ID: {}", correlationId);
        }

        // Ajoute dans MDC pour les logs
        MDC.put(CORRELATION_ID_MDC_KEY, correlationId);

        // Ajoute le header aux requêtes en aval
        ServerHttpRequest modifiedRequest = request.mutate()
                .header(CORRELATION_ID_HEADER, correlationId)
                .build();

        // Ajoute le header à la réponse
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add(CORRELATION_ID_HEADER, correlationId);

        return chain.filter(exchange.mutate().request(modifiedRequest).build())
                .doFinally(signalType -> MDC.remove(CORRELATION_ID_MDC_KEY));
    }

    @Override
    public int getOrder() {
        // S'exécute en tout premier pour que l'ID soit disponible pour tous les filtres
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
