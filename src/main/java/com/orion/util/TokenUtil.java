package com.orion.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TokenUtil {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Use a strong secret key

    /**
     * Generates a JWT token containing rental ID and expiration time.
     *
     * @param rentalId the rental ID to include in the token
     * @param expirationMinutes the token's validity duration in minutes
     * @return a JWT token as a String
     */
    public static String generateToken(Long rentalId, int expirationMinutes) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(expirationMinutes * 60);

        return Jwts.builder()
                .claim("rentalId", rentalId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Validates a JWT token and checks its expiration.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extracts the rental ID from a valid JWT token.
     *
     * @param token the JWT token
     * @return the rental ID if present, null otherwise
     */
    public static Long extractRentalId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("rentalId", Long.class);
    }

    /**
     * Generates the expiration time for a token.
     *
     * @param minutes the validity duration in minutes
     * @return the LocalDateTime of the expiration
     */
    public static LocalDateTime getTokenExpirationTime(int minutes) {
        return LocalDateTime.now().plusMinutes(minutes);
    }
}
