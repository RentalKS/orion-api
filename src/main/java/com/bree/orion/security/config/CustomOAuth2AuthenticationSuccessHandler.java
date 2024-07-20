package com.bree.orion.security.config;

import com.bree.orion.security.token.TokenService;
import com.bree.orion.security.user.Role;
import com.bree.orion.security.user.User;
import com.bree.orion.security.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String email;
        String firstName = null;
        String lastName = null;
        if (authentication.getPrincipal() instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            email = oidcUser.getEmail();
            firstName = oidcUser.getGivenName();
            lastName = oidcUser.getFamilyName();
            String idToken = oidcUser.getIdToken().getTokenValue();
            handleUserAndGenerateToken(email, firstName, lastName, response,idToken);
        } else if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
            email = oauth2User.getAttribute("email");
            firstName = oauth2User.getAttribute("given_name");
            lastName = oauth2User.getAttribute("family_name");
            handleUserAndGenerateToken(email, firstName, lastName, response,null);
        }

        // Clear the authentication attributes
        clearAuthenticationAttributes(request);
    }

    private void handleUserAndGenerateToken(String email,String firstName, String lastName, HttpServletResponse response,String idToken) throws IOException {
        // Find or create the user in your database
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user;
        if (optionalUser.isEmpty()) {
            user = new User();
            user.setEmail(email);
            user.setRole(Role.USER);
            user.setFirstname(firstName);
            user.setLastname(lastName);
            userRepository.save(user);
        } else {
            user = optionalUser.get();
        }

        // Load UserDetails for JWT generation
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Generate JWT token
        String jwtToken = jwtService.generateToken(userDetails);

        // Save the token
        tokenService.save(idToken,jwtToken, user);

        // Include the JWT in the response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(Map.of("token", jwtToken)));
        response.sendRedirect("http://localhost:3000/oauth2/redirect?token=" + jwtToken);

    }
}