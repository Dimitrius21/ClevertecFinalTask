package ru.clevertec.AuthServer.utils;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для создания JwtToken
 */
@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration lifetime;

    private final String fieldForGrantedAuthorities = "scope";

    /**
     * Метод создающий JwtToken по данным пользователя
     *
     * @param user - данные пользователя
     * @return - сформированный JwtToken
     */
    public String jwtTokenCreator(UserDetails user) {

        Map<String, Object> claims = new HashMap<>();

        Date now = new Date();
        Date experationDate = new Date(now.getTime() + lifetime.getSeconds() * 1000 * 1000);
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        claims.put(fieldForGrantedAuthorities, roles);

        JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setSubject(user.getUsername())
                .setExpiration(experationDate)
                .signWith(SignatureAlgorithm.HS256, secret);
        String token = jwtBuilder.compact();
        return token;
    }
}
