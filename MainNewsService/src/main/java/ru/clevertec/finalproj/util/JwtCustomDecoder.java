package ru.clevertec.finalproj.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.time.Instant;

/**
 * Класс с декодером полученного Jwt-Token
 */
public class JwtCustomDecoder implements JwtDecoder {
    @Value("${jwt.secret}")
    private String secret;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {

            io.jsonwebtoken.Jwt<? extends Header, Claims> tokenParsed = Jwts.parserBuilder().setSigningKey(secret).build().parse(token);
            Claims claimsBody = (Claims) tokenParsed.getBody();
            Header claimsHeader = tokenParsed.getHeader();
            Instant expiration = claimsBody.getExpiration().toInstant();
            Instant issuedAt = claimsBody.getIssuedAt().toInstant();

            Jwt jwt = new Jwt(token, issuedAt, expiration, claimsHeader, claimsBody);
            return jwt;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException ex) {
            throw new BadJwtException("Presented token isn't valid");
        }
    }
}
