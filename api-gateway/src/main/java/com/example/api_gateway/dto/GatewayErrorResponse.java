package com.example.api_gateway.dto;
import java.time.Instant;

/**
 * DTO standard pour les erreurs renvoyées par le Gateway.
 * Permet de fournir un message clair, un code, le chemin et la date de l'erreur.
 */
public record GatewayErrorResponse(
        String code,
        String message,
        Instant timestamp,
        String path
) {

    /**
     * Factory method pratique pour créer une erreur rapidement
     *
     * @param code    Code d'erreur (ex: AUTH_FAILED, NOT_FOUND)
     * @param message Message d'erreur clair
     * @param path    Endpoint qui a généré l'erreur
     * @return GatewayErrorResponse instance
     */
    public static GatewayErrorResponse of(String code, String message, String path) {
        return new GatewayErrorResponse(code, message, Instant.now(), path);
    }
}

