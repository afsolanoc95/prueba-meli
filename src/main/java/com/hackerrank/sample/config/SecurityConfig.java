package com.hackerrank.sample.config;

import com.hackerrank.sample.security.AuthEntryPointJwt;
import com.hackerrank.sample.security.AuthTokenFilter;
import com.hackerrank.sample.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter; // We inject the bean directly

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @org.springframework.beans.factory.annotation.Value("${app.cors.allowed-origins:*}")
    private String[] allowedOrigins;

    private static final String ROLE_SELLER = "SELLER";
    private static final String ROLE_BUYER = "BUYER";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())).csrf(csrf -> csrf.disable()) // NOSONAR:
                                                                                                            // CSRF is
                                                                                                            // disabled
                                                                                                            // as the
                                                                                                            // application
                                                                                                            // uses
                                                                                                            // stateless
                                                                                                            // JWT
                                                                                                            // authentication
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public Endpoints
                        .requestMatchers("/api/auth/**").permitAll().requestMatchers(HttpMethod.GET, "/api/products/**")
                        .permitAll().requestMatchers("/h2-console/**").permitAll() // H2 Console

                        // ... inside filterChain ...
                        // Swagger / OpenAPI
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Public endpoints
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                        // SELLER Rules
                        .requestMatchers(HttpMethod.POST, "/api/products").hasRole(ROLE_SELLER)
                        .requestMatchers(HttpMethod.PUT, "/api/products/{id}").hasRole(ROLE_SELLER)
                        .requestMatchers(HttpMethod.DELETE, "/api/products/{id}").hasRole(ROLE_SELLER)
                        .requestMatchers(HttpMethod.PUT, "/api/products/{id}/questions/{qid}").hasRole(ROLE_SELLER)

                        // BUYER Rules
                        .requestMatchers(HttpMethod.POST, "/api/products/{id}/reviews").hasRole(ROLE_BUYER)
                        .requestMatchers(HttpMethod.PUT, "/api/products/{id}/reviews/**").hasRole(ROLE_BUYER)
                        .requestMatchers(HttpMethod.DELETE, "/api/products/{id}/reviews/**").hasRole(ROLE_BUYER)
                        .requestMatchers(HttpMethod.POST, "/api/products/{id}/questions").hasRole(ROLE_BUYER)
                        .requestMatchers(HttpMethod.DELETE, "/api/products/{id}/questions/{qid}").hasRole(ROLE_BUYER)

                        // Any other request needs auth
                        .anyRequest().authenticated());

        // Fix H2 Console frame issue
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();

        // Use allowed origins from properties or default to *
        if (allowedOrigins.length == 1 && "*".equals(allowedOrigins[0])) {
            configuration.addAllowedOriginPattern("*");
        } else {
            configuration.setAllowedOrigins(java.util.Arrays.asList(allowedOrigins));
        }

        configuration.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(java.util.Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
