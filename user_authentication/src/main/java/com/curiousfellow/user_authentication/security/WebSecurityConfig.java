package com.curiousfellow.user_authentication.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.curiousfellow.user_authentication.security.jwt.JwtValidator;
import com.curiousfellow.user_authentication.services.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class WebSecurityConfig {

        private final CustomUserDetailsService customUserDetailsService;
        private final PasswordEncoder passwordEncoder;
        private final JwtValidator jwtValidator;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.configurationSource(corsConfigSource()))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(
                                                jwtValidator, UsernamePasswordAuthenticationFilter.class)
                                .authorizeHttpRequests(
                                                requests -> requests.requestMatchers("/api/v1/auth/**").permitAll()
                                                                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                                                                .anyRequest().authenticated())
                                .userDetailsService(customUserDetailsService);

                return http.build();
        }

        @Bean
        public AuthenticationManager authManager(HttpSecurity http) throws Exception {
                AuthenticationManagerBuilder authMB = http.getSharedObject(AuthenticationManagerBuilder.class);
                authMB.userDetailsService(customUserDetailsService)
                                .passwordEncoder(passwordEncoder.bCryptPasswordEncoder());

                return authMB.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigSource() {
                return new CorsConfigurationSource() {

                        @Override
                        public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration config = new CorsConfiguration();
                                config.setAllowedOrigins(List.of("http://localhost:3000"));
                                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                                config.setAllowCredentials(true);
                                config.setAllowedHeaders(List.of("*"));
                                config.setMaxAge(3600L);
                                return config;
                        }

                };
        }
}
