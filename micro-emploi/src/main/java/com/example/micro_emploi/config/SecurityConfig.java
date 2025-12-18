package com.example.micro_emploi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // désactive CSRF
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // autorise toutes les requêtes
                .formLogin(form -> form.disable()) // désactive la page de login
                .httpBasic(httpBasic -> httpBasic.disable()); // désactive l'authentification HTTP Basic

        return http.build();
    }
}