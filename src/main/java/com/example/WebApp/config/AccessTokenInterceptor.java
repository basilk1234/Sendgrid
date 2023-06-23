package com.example.WebApp.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
@Slf4j
public class AccessTokenInterceptor implements HandlerInterceptor {

    private static final String SECRET_KEY = "dshhsfhshdsdfgasdfasdadasdfagsfbhfgjfhjgdhfsfgbdafaf";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Retrieve the access token from the request headers or parameters
        String accessToken = retrieveAccessToken(request);
        log.info("AccessToken {}",accessToken);
        if (accessToken != null && validateToken(accessToken)) {
            // Token is valid, continue with the request
            return true;
        } else {
            // Token is invalid or not provided, handle unauthorized response
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    private String retrieveAccessToken(HttpServletRequest request) {
        return request.getHeader("token");
    }

    private boolean validateToken(String token) {
        try {
            // Parse and validate the token
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

            // Perform additional validation if needed

            return true;
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            // Token is invalid or malformed
            return false;
        }
    }
}
