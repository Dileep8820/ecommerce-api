package com.example.ecommerce.security;

import com.example.ecommerce.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationMs = 3600_000; // 1 hour

    // Generate token
    public String generateToken(String username, Role role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    // Validate token and get username
    public String getUsername(String token) {
        return parse(token).getBody().getSubject();
    }

    // Get role
    public String getRole(String token) {
        return parse(token).getBody().get("role", String.class);
    }

    private Jws<Claims> parse(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException e) {
            throw new SecurityException("Invalid JWT token");
        }
    }
}
