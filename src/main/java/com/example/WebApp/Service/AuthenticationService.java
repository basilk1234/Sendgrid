package com.example.WebApp.Service;
import com.example.WebApp.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
@Slf4j
public class AuthenticationService {

    private static final String SECRET_KEY = "dshhsfhshdsdfgasdfasdadasdfagsfbhfgjfhjgdhfsfgbdafaf";

    public String generateToken(User user) {
        // Set the expiration time for the token
        Date expirationDate = new Date(System.currentTimeMillis() + 86400000); // 1 day

        // Generate the token using the user's ID, username, and expiration date
        String token = Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("id", user.getId().toString())
                .claim("email", user.getEmail())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        return token;
    }
}
