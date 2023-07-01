package ru.clevertec.finalproj.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import ru.clevertec.finalproj.client.JwtTokenClient;
import ru.clevertec.finalproj.filter.AuthenticateHeaderFilter;
import ru.clevertec.finalproj.util.AuthorizeUserForAction;
import ru.clevertec.finalproj.util.CheckUserByRequestId;
import ru.clevertec.finalproj.util.CheckUserInRequestBody;
import ru.clevertec.finalproj.util.ForbiddenEntryPoint;
import ru.clevertec.finalproj.util.JwtCustomDecoder;

/**
 * Класс конфигурирующий Spring Security
 */

@Configuration
@AllArgsConstructor
@EnableWebSecurity(debug = false)
public class SecurityConfig {

    @Autowired
    private CheckUserInRequestBody checkUserInRequestBody;

    @Autowired
    private CheckUserByRequestId checkUserByRequestId;

    @Autowired
    private JwtTokenClient jwtTokenClient;

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return converter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return new JwtCustomDecoder();
    }

    @Bean
    public AuthenticateHeaderFilter authenticateHeaderFilter() {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtDecoder());
        provider.setJwtAuthenticationConverter(jwtAuthenticationConverter());
        AuthenticationManager manager = new ProviderManager(provider);
        return new AuthenticateHeaderFilter(manager, jwtTokenClient);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/**").access(new AuthorizeUserForAction(checkUserByRequestId))
                        .requestMatchers("/api/comment", "api/news").access(new AuthorizeUserForAction(checkUserInRequestBody))
                        .anyRequest().permitAll()
                )
                .addFilterBefore(authenticateHeaderFilter(), BearerTokenAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handler -> handler.authenticationEntryPoint(new ForbiddenEntryPoint()))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder()).jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();
    }
}
