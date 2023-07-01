package ru.clevertec.AuthServer.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.clevertec.AuthServer.domain.dto.UserMainDto;
import ru.clevertec.AuthServer.exception.BadRequestBodyException;
import ru.clevertec.AuthServer.utils.ErrorSender;

import java.io.IOException;
import java.util.Objects;

/**
 * Фильтр проводящий аутентификацию пользователя по данным теле запроса в виде json для UserMainDto (имя и пароль)
 */

public class BodyAuthFilter extends OncePerRequestFilter {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private final SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    private final AuthenticationManager authenticationManager;

    private final ObjectMapper objectMapper;

    public BodyAuthFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        try {
            if ("/auth/jwt".equals(path)) {
                String body = new String(request.getInputStream().readAllBytes());
                UserMainDto userData = objectMapper.readValue(body, UserMainDto.class);
                if (Objects.isNull(userData)) {
                    throw new BadRequestBodyException();
                }
                UsernamePasswordAuthenticationToken authenticationRequest = UsernamePasswordAuthenticationToken
                        .unauthenticated(userData.getUsername(), userData.getPassword());

                Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);
                SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
                context.setAuthentication(authenticationResult);
                securityContextHolderStrategy.setContext(context);
                securityContextRepository.saveContext(context, request, response);
            }
            filterChain.doFilter(request, response);
        } catch (AuthenticationException ex) {
            this.securityContextHolderStrategy.clearContext();
            ErrorSender.sendError(response, HttpStatus.UNAUTHORIZED, "Not authenticated, bad credentials");
        } catch (JsonProcessingException | BadRequestBodyException ex) {
            this.securityContextHolderStrategy.clearContext();
            ErrorSender.sendError(response, HttpStatus.BAD_REQUEST, "Incorrect body in the request");
        } catch (Exception ex) {
            this.securityContextHolderStrategy.clearContext();
            ErrorSender.sendError(response, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
}
