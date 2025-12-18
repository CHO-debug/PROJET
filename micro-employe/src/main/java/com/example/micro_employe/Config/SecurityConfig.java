package com.example.micro_employe.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // <-- Bean pour le cryptage
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ❌ Désactiver CSRF (utile pour POST/PUT via Postman ou Feign)
                .csrf(csrf -> csrf.disable())

                // ✅ Autoriser certains endpoints sans authentification
                .authorizeHttpRequests(auth -> auth
                        // Swagger UI et docs OpenAPI
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // Endpoint AuthService ou test local
                        .requestMatchers("/employes/**").permitAll()
                        // Toutes les autres requêtes nécessitent authentification
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
