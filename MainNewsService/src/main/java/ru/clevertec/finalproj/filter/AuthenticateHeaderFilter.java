package ru.clevertec.finalproj.filter;

import feign.FeignException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.clevertec.finalproj.client.JwtTokenClient;
import ru.clevertec.finalproj.domain.dto.UserMainDto;
import ru.clevertec.finalproj.util.ForbiddenEntryPoint;

import java.io.IOException;

/**
 * Класс реализующий фильтр для получения аутентификации через данные в заголовке Authorization запроса с последующим
 * запросом на сервер аутентификации для подтверждения данных
 */
public class AuthenticateHeaderFilter extends OncePerRequestFilter {
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private final BasicAuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();

    private final AuthenticationEntryPoint authenticationEntryPoint = new ForbiddenEntryPoint();

    private final AuthenticationFailureHandler authenticationFailureHandler = new AuthenticationEntryPointFailureHandler(
            (request, response, exception) -> this.authenticationEntryPoint.commence(request, response, exception));

    private final SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    private final AuthenticationManager authenticationManager;

    private final JwtTokenClient jwtTokenClient;

    public AuthenticateHeaderFilter(AuthenticationManager authenticationManager, JwtTokenClient jwtTokenClient) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenClient = jwtTokenClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //Читаем и проверяем что заголовок Authorization содержит данные пользователя в формате для базовой аутентификации
            UsernamePasswordAuthenticationToken authRequest = this.authenticationConverter.convert(request);
            if (authRequest != null) {
                String username = authRequest.getName();
                String password = (String) authRequest.getCredentials();
                UserMainDto userData = new UserMainDto(username, password);
                //Запрашиваем подтверждение полученных данных на сервисе аутентификации
                String token = jwtTokenClient.getToken(userData);
                //Проводим аутентификацию пользователя по данным полученного токена
                BearerTokenAuthenticationToken authenticationRequest = new BearerTokenAuthenticationToken(token);
                Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);
                SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
                context.setAuthentication(authenticationResult);
                this.securityContextHolderStrategy.setContext(context);
                this.securityContextRepository.saveContext(context, request, response);
            }
            filterChain.doFilter(request, response);
        } catch (AuthenticationException ex) {
            this.securityContextHolderStrategy.clearContext();
            this.authenticationFailureHandler.onAuthenticationFailure(request, response, ex);
        } catch (FeignException ex) {
            this.securityContextHolderStrategy.clearContext();
            if (ex.status() == 401 || ex.status() == 400) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, new BadCredentialsException("Bad Credentials in request", ex));
            } else {
                authenticationFailureHandler.onAuthenticationFailure(request, response, new AuthenticationServiceException("Error with Authenticated Server", ex));
            }
        }
    }
}

