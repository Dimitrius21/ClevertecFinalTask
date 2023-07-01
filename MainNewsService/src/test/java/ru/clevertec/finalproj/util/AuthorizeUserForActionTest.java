package ru.clevertec.finalproj.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.List;
import java.util.function.Supplier;

class AuthorizeUserForActionTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void checkTest() {

        CheckUserInRequest checkUser = new CheckUserInRequestBody(objectMapper);
        AuthorizationManager<RequestAuthorizationContext> authorizationManager = new AuthorizeUserForAction(checkUser);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestAuthorizationContext requestAuthorizationContext = new RequestAuthorizationContext(request);

        AuthenticatedPrincipal principal = () -> "admin";
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        Supplier<Authentication> authentication = () -> new UsernamePasswordAuthenticationToken(principal, "", List.of(authority));

        AuthorizationDecision decision = authorizationManager.check(authentication, requestAuthorizationContext);

        Assertions.assertThat(decision.isGranted()).isTrue();
    }

    @Test
    void rejectNotCorrespondentRoleTest() {

        CheckUserInRequest checkUser = new CheckUserInRequestBody(objectMapper);
        AuthorizationManager<RequestAuthorizationContext> authorizationManager = new AuthorizeUserForAction(checkUser);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/news");
        RequestAuthorizationContext requestAuthorizationContext = new RequestAuthorizationContext(request);

        AuthenticatedPrincipal principal = () -> "Sema";
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_SUBSCRIBER");
        Supplier<Authentication> authentication = () -> new UsernamePasswordAuthenticationToken(principal, "", List.of(authority));

        AuthorizationDecision decision = authorizationManager.check(authentication, requestAuthorizationContext);

        Assertions.assertThat(decision.isGranted()).isFalse();
    }
}