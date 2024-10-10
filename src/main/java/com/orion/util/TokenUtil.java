package com.orion.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class TokenUtil {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String OBJECT_ID = "objectId";
    private static final String TRANSACTION_ID = "transactionId";

    /**
     * Generates a JWT token containing rental ID and expiration time.
     *
     * @param objectId the rental ID to include in the token
     * @param expirationMinutes the token's validity duration in minutes
     * @return a JWT token as a String
     */
    public static String generateToken(Long objectId, int expirationMinutes,String transactionId) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(expirationMinutes * 60L);

        return Jwts.builder()
                .claim(OBJECT_ID, objectId)
                .claim(TRANSACTION_ID, transactionId)
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
    public static boolean validateToken(String token, Long objectId) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

            Date expirationDate = claims.getExpiration();

            if (expirationDate.before(new Date())) {
                return false;
            }

            Long compareObject = claims.get(OBJECT_ID, Long.class);

            return objectId.equals(compareObject);
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
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            System.err.println("Invalid JWT signature: " + e.getMessage());
            throw new RuntimeException("Invalid signature", e);
        } catch (ExpiredJwtException e) {
            System.err.println("JWT token is expired: " + e.getMessage());
            throw new RuntimeException("Token expired", e);
        } catch (Exception e) {
            System.err.println("Error parsing JWT token: " + e.getMessage());
            throw new RuntimeException("Error parsing token", e);
        }

        return claims.get(OBJECT_ID, Long.class);
    }

    public static String extractTransactionId(String token) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            System.err.println("Invalid JWT signature: " + e.getMessage());
            throw new RuntimeException("Invalid signature", e);
        } catch (ExpiredJwtException e) {
            System.err.println("JWT token is expired: " + e.getMessage());
            throw new RuntimeException("Token expired", e);
        } catch (Exception e) {
            System.err.println("Error parsing JWT token: " + e.getMessage());
            throw new RuntimeException("Error parsing token", e);
        }

        return claims.get(TRANSACTION_ID, String.class);
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
    public static String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

}
