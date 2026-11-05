package com.suma.carepoint.configs;

import com.suma.carepoint.security.UserDetailsServiceImpl;
import com.suma.carepoint.security.jwt.AuthCookieService;
import com.suma.carepoint.security.jwt.CarePointJwtAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.authorization.method.PreAuthorizeAuthorizationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import static com.suma.carepoint.models.constants.ApiConstant.Auth.*;

import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = false)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtDecoder jwtDecoder;
    private final CarePointJwtAuthenticationConverter jwtConverter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http, BearerTokenResolver cookieBearerTokenResolver) throws Exception {

        CookieCsrfTokenRepository csrfRepository =
                CookieCsrfTokenRepository.withHttpOnlyFalse();
        http
                .cors(cors -> {})
                .csrf(AbstractHttpConfigurer::disable)
//                .csrf(csrf -> csrf.csrfTokenRepository(csrfRepository))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/auth/csrf").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                BASE+LOGIN, BASE+REFRESH, BASE+LOGOUT, BASE+FORGOT_PASSWORD
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .bearerTokenResolver(cookieBearerTokenResolver)
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder).jwtAuthenticationConverter(jwtConverter))
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:4200")
        );

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        );

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    BearerTokenResolver cookieBearerTokenResolver(AuthCookieService authCookieService) {

        return request -> {
            String header = request.getHeader("Authorization");

            // Reject requests that attempt to supply two token sources.
            if (header != null) {
                if (header.startsWith("Bearer ")) {
                    throw new org.springframework.security.oauth2.server.resource.InvalidBearerTokenException("Bearer headers are not accepted");
                }
                throw new org.springframework.security.oauth2.server.resource.InvalidBearerTokenException("Unsupported authorization header");
            }
            if (request.getCookies() == null) {
                return null;
            }

            String token = null;

            for (var cookie : request.getCookies()) {
                if (authCookieService.accessCookieName().equals(cookie.getName())) {
                    if (token != null) {
                        throw new org.springframework.security.oauth2.server.resource.InvalidBearerTokenException("Duplicate access-token cookies");
                    }
                    token = cookie.getValue();
                }
            }

            return token;
        };
    }

    @Bean
    AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    static Advisor preAuthorizeAdvisor() {
        PreAuthorizeAuthorizationManager delegate = new PreAuthorizeAuthorizationManager();

        AuthorizationManager<MethodInvocation> manager = (authenticationSupplier, invocation) -> {
            Authentication authentication = authenticationSupplier.get();
            boolean isSuperAdmin = authentication != null
                    && authentication.isAuthenticated()
                    && authentication.getAuthorities().stream()
                    .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_SUPER_ADMIN"));

            if (isSuperAdmin) {
                return new AuthorizationDecision(true);
            }
            assert invocation != null;
            return delegate.authorize(authenticationSupplier, invocation);
        };
        return AuthorizationManagerBeforeMethodInterceptor.preAuthorize(manager);
    }
}