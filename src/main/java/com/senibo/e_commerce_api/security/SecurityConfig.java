package com.senibo.e_commerce_api.security;

import com.senibo.e_commerce_api.config.OAuth2LoginSuccessHandler;
import com.senibo.e_commerce_api.security.jwt.AuthTokenFilter;
import com.senibo.e_commerce_api.security.jwt.SecurityErrorHandler;
import com.senibo.e_commerce_api.service.impl.CustomOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Central configuration for application security.
 * <p>
 * This class sets up the main security filter chain, which includes CORS, CSRF,
 * session management, request authorization, exception handling, and custom JWT/OAuth2 filters.
 * It also enables method-level security with {@code @EnableMethodSecurity}.
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthTokenFilter jwtFilter;
    private final SecurityErrorHandler securityErrorHandler;
    private final CustomOauth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    /**
     * Defines the password encoder bean for the application.
     *
     * @return A {@link BCryptPasswordEncoder} instance.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes the AuthenticationManager as a bean from the authentication configuration.
     *
     * @param cfg The authentication configuration.
     * @return The {@link AuthenticationManager}.
     * @throws Exception if an error occurs while getting the authentication manager.
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    /**
     * Configures the primary security filter chain for the application.
     *
     * @param http The {@link HttpSecurity} to configure.
     * @return The configured {@link SecurityFilterChain}.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configure Cross-Origin Resource Sharing (CORS)
        http.cors(cors -> cors.configurationSource(corsConfigurationSource(null)));

        // Disable Cross-Site Request Forgery (CSRF) as we are using token-based auth
        http.csrf(AbstractHttpConfigurer::disable);

        // Set session management to stateless, as we are not using HTTP sessions
        http.sessionManagement(sm ->
                                       sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Configure authorization rules for different endpoints
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/v1/auth/**", "/login/**").permitAll()
                .requestMatchers("/oauth2/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger*/**", "/webjars/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                // --- ADD THIS LINE ---
                .requestMatchers("/api/v1/users/me").authenticated()
                .requestMatchers("/api/v1/webhooks/**").permitAll()
                .anyRequest().authenticated());

        // Configure OAuth2 login flow
        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService)
                )
                .successHandler(oAuth2LoginSuccessHandler)
        );

        // Configure custom exception handling for authentication and access denied errors
        http.exceptionHandling(e -> e
                .authenticationEntryPoint(securityErrorHandler)
                .accessDeniedHandler(securityErrorHandler));

        // Add the custom JWT filter before the standard username/password authentication filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Defines the CORS configuration for the application.
     *
     * @param ngrokUrl The dynamic Ngrok URL for development, injected from properties.
     * @return A {@link CorsConfigurationSource} with the defined policies.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource(
            @Value("${spring.app.ngrok-url}") String ngrokUrl
    ) {
        CorsConfiguration configuration = new CorsConfiguration();

        // Specify allowed origins explicitly for security
        configuration.setAllowedOrigins(List.of(
                "http://localhost:4403",
                "http://localhost:3000",
                "https://6r9jltnq-3000.uks1.devtunnels.ms",
                ngrokUrl
        ));

        configuration.setAllowedMethods(List.of("GET",
                                                "POST",
                                                "PUT",
                                                "DELETE",
                                                "OPTIONS",
                                                "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type", "Accept"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
