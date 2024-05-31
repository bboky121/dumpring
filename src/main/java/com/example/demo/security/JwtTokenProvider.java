package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(this.secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String userName, String role) {
        ClaimsBuilder claimBuilder = Jwts.claims().subject(userName);
        claimBuilder.add("role", role);
        claimBuilder.build();
        Claims claims = claimBuilder.build();

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder().claims(claims).issuedAt(now).expiration(validity).signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public String getRole(String token) {
        return (String) Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload().get("role");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
