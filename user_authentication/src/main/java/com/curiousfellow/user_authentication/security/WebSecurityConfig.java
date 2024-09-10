package com.curiousfellow.user_authentication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.curiousfellow.user_authentication.security.jwt.JwtValidator;
import com.curiousfellow.user_authentication.services.CustomUserDetailsService;

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
                                .csrf(token -> token.disable())
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
}
