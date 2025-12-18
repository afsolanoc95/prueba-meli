package com.hackerrank.sample.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // Should be in application.properties, using a default for sample
    @Value("${app.jwtSecret:SecretKeyToGenJWTsMustBeLongEnoughToMeetSecurityRequirements1234567890}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs:3600000}") // 1 hour = 3600000 ms
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(getEncodedSecret()));
    }

    // For specific version of jjwt (0.11.x), keys should be byte arrays.
    // The provided secret in properties is a raw string.
    // Ideally it should be base64 encoded if using Decoders.BASE64.decode
    // Or just use Keys.hmacShaKeyFor(jwtSecret.getBytes())
    // Let's ensure robust handling.

    private String getEncodedSecret() {
        // Simple base64 encoding of the secret string for demo purposes
        // if user hasn't provided a base64 string.
        // But traditionally we expect the property to be base64.
        // Let's assume the default string provided in @Value is just raw text.
        // We will stick to using the bytes directly for simplicity or base64 encode it.
        // Better: use direct bytes if string is simple.
        // Keys.hmacShaKeyFor requires > 32 bytes (256 bits).
        return java.util.Base64.getEncoder().encodeToString(jwtSecret.getBytes());
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Date getExpirationDateFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getExpiration();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
