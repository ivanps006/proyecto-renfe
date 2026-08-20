package com.proyectorenfe.config;

import io.jsonwebtoken.Jwt;
import java.util.List;

import org.springframework.security.config.Customizer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/usuarios/login", "/usuarios/registro").permitAll()
                        .requestMatchers(HttpMethod.GET, "/viajes/*/asientos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/viajes/buscar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/rutas").permitAll()
                        .anyRequest().authenticated()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter){
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracion = new CorsConfiguration();

        configuracion.setAllowedOrigins(List.of("http://localhost:5173"));
        configuracion.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );
        configuracion.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource origen = new UrlBasedCorsConfigurationSource();
        origen.registerCorsConfiguration("/**", configuracion);

        return origen;
    }
}