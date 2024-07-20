package com.bree.orion.security.token;

import com.bree.orion.security.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public void save(String idToken, String jwtToken, User user) {
        Token token = Token.builder()
                .token(idToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .user(user)
                .build();

        // Assuming you have the user information
        // token.setUser(user);

        tokenRepository.save(token);

        // Save the JWT token as well, if needed
        Token jwt = Token.builder()
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .user(user)
                .build();

        tokenRepository.save(jwt);
    }
}
