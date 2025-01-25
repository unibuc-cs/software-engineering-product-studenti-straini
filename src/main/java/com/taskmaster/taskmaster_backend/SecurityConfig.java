package com.taskmaster.taskmaster_backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Dezactivam CSRF pentru testare
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Permitem accesul la toate endpoint-urile
                );

        return http.build();
    }
}
