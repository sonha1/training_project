package com.gtel.homework.utils;

import com.gtel.homework.dto.auth.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    @Value("${jwt.validity}")
    private Long tokenValidity;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.refreshSecret}")
    private String secretRF;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String getUsernameFromRfToken(String token) {
        return getClaimFromRfToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromRfToken(String token) {
        return getClaimFromRfToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public <T> T getClaimFromRfToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromRfToken(token);
        return claimsResolver.apply(claims);
    }

    private Key getSigningKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims getAllClaimsFromRfToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretRF))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    public String generateToken(UserPrincipal userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("idUser", userDetails.getUserId());
        claims.put("username", userDetails.getUsername());
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String generateRfToken(UserPrincipal userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateRfToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity * 1000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private String doGenerateRfToken(Map<String, Object> claims, String subject) {
        Key key = Keys.hmacShaKeyFor(secretRF.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity * 1000 * 15))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
