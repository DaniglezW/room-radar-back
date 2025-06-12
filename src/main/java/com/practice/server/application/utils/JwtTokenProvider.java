package com.practice.server.application.utils;

import com.practice.server.application.model.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long tokenValidity;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret, @Value("${jwt.token-validity}") long tokenValidity) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.tokenValidity = tokenValidity;
    }

    public String generateToken(String email, Role role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
                .signWith(secretKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Role getRoleFromToken(String token) {
        String roleString = getClaimsFromToken(token).get("roles", String.class);
        return Role.valueOf(roleString);
    }

    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Token inválido: " + e.getMessage());
            return false;
        }
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
