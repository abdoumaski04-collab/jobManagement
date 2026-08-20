package com.abdo.JobManagement.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.function.Function;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class jwtService {


    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private Long expirationMs;

    private SecretKey getSigingKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email,String role){
        Date now=new Date();
        Date expiry=new Date(now.getTime()+expirationMs);

        return Jwts.builder()
                .subject(email)
                .claim("role",role)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigingKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims,T> resolver){
        return resolver.apply(extractAllClaims(token));
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
    public boolean isTokenValid(String token, String email) {
        return extractEmail(token).equals(email) && !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
}
