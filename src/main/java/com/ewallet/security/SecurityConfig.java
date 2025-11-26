package com.ewallet.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .cors() // enable CORS
            .and()
            .authorizeHttpRequests()
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/wallet/**").permitAll()
            .requestMatchers("/api/qr/**").permitAll()
            .anyRequest().authenticated();

        return http.build();
    }

    // CORS configuration
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("https://5b84c315b2104403a4b6583d3336034c-1574df9c7b3e434baed0e0aa8.fly.dev")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
