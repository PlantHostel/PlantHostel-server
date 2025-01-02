package com.planthostelserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planthostelserver.auth.filter.JwtAuthenticationFilter;
import com.planthostelserver.auth.filter.JwtAuthorizationFilter;
import com.planthostelserver.auth.handler.JwtAuthenticationFailureHandler;
import com.planthostelserver.auth.handler.JwtAuthorizationFailureHandler;
import com.planthostelserver.auth.service.AuthService;
import com.planthostelserver.type.RoleType;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic(HttpBasicConfigurer::disable)
            .cors(config -> config.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(
                configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/v3/api-docs/**", "/swagger/index.html", "/swagger-ui/**")
                .permitAll()

                .requestMatchers("/", "/auth/**").permitAll()
                .requestMatchers("/admins/**").hasRole(RoleType.ADMIN.name())
                .anyRequest().authenticated()
            )
            .addFilterBefore(
                new JwtAuthenticationFilter(authenticationManager(), new ObjectMapper()),
                UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JwtAuthorizationFilter(),
                UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(
                handler -> {
                    handler.authenticationEntryPoint(new JwtAuthenticationFailureHandler());
                    handler.accessDeniedHandler(new JwtAuthorizationFailureHandler());
                }
            );


        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(true);
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:5173"));

            return config;
        };
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(authService);
        provider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(provider);
    }
}
