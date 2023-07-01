package ru.clevertec.AuthServer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.clevertec.AuthServer.filter.BodyAuthFilter;
import ru.clevertec.AuthServer.service.UserService;

/**
 * Класс конфигурирующий доступ в приложении
 */
@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder encoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(encoder);
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/jwt", "/auth/user").permitAll()
                        .requestMatchers("/auth/journalist").hasRole("ADMIN")
                )
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(bodyAuthFilter(passwordEncoder()), BasicAuthenticationFilter.class)
                .exceptionHandling(handler -> handler.authenticationEntryPoint(new Http403ForbiddenEntryPoint()));

        return http.build();
    }

    @Bean
    public BodyAuthFilter bodyAuthFilter(PasswordEncoder passwordEncoder) {
        AuthenticationManager manager = new ProviderManager(daoAuthenticationProvider(passwordEncoder));
        return new BodyAuthFilter(manager, objectMapper);
    }
}
