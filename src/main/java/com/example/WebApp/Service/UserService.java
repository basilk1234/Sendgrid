package com.example.WebApp.Service;
import com.example.WebApp.exception.DuplicateEmailException;
import com.example.WebApp.dto.Token;
import com.example.WebApp.exception.InvalidDataException;
import com.example.WebApp.exception.InvalidLoginException;
import com.example.WebApp.exception.UserNotExsist;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import java.io.IOException;
import java.util.NoSuchElementException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.example.WebApp.model.User;
import com.example.WebApp.repository.UserRepository;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    private static final String SECRET_KEY = "dshhsfhshdsdfgasdfasdadasdfagsfbhfgjfhjgdhfsfgbdafaf";
//    private final BCryptPasswordEncoder passwordEncoder;
//    private final Key jwtSigningKey;

    public UserService(UserRepository userRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtSigningKey = jwtSigningKey;
    }

    public User register(User user) {
        log.info("Register:::");
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser;
        try {
             savedUser = userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateEmailException();
        }
        // Generate token
//        String jws = Jwts.builder()
//        .setSubject(savedUser.getId().toString())
//        .signWith(SignatureAlgorithm.HS256, jwtSigningKey.getEncoded())
//        .compact();
//
//
//        sendVerificationEmail(savedUser.getEmail(), jws);

        return savedUser;
    }

    public Token login(User user) {
        User loggedUser;
        try {
            loggedUser = userRepository.findByEmailAndPassword(user.getEmail(),user.getPassword()).get();
            log.info("User {}",loggedUser.getEmail());
            // Generate the token
            Token token = new Token(authenticationService.generateToken(loggedUser), loggedUser.getEmail(),loggedUser.getId());
            log.info("Token {}",token);
            return token;
        } catch (NoSuchElementException ex) {
            throw new InvalidLoginException();
        }

    }
private void sendVerificationEmail(String email, String token) {
        Email from = new Email("your-email@example.com"); // Replace with the email address
        String subject = "Please verify your email";
        Email to = new Email(email);
        Content content = new Content("text/plain", "Thank you for registering. To complete your registration, please click on the following link: http://localhost:8080/verifyEmail?token=" + token);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("YOUR_SENDGRID_API_KEY"); // Replace with your SendGrid API key
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public User getUser(String token) {
        // Parse and validate the token
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        log.info("ID {}", claims.get("id"));

        Long userId = Long.parseLong(claims.get("id").toString());
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new UserNotExsist();
        }
        return user;
    }

    public void updateUser(User user) {

        if(user.getId() == null ||  user.getEmail().isEmpty() || user.getPassword().isEmpty()){
            throw new InvalidDataException();
        }
        try {
            userRepository.updateEmailAndPasswordById(user.getEmail(), user.getPassword(), user.getId());
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateEmailException();
        }
    }

    public void deleteUser(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        log.info("ID {}", claims.get("id"));

        Long userId = Long.parseLong(claims.get("id").toString());
        userRepository.deleteById(userId);
    }
    // TODO: implement sendVerificationEmail() method
}

