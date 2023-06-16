package com.example.WebApp.Service;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import java.io.IOException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.WebApp.model.User;
import com.example.WebApp.repository.UserRepository;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import java.security.Key;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Key jwtSigningKey;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, Key jwtSigningKey) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtSigningKey = jwtSigningKey;
    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        
        // Generate token
        String jws = Jwts.builder()
        .setSubject(savedUser.getId().toString())
        .signWith(SignatureAlgorithm.HS256, jwtSigningKey.getEncoded())
        .compact();

        
        sendVerificationEmail(savedUser.getEmail(), jws);

        return savedUser;
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
    // TODO: implement sendVerificationEmail() method
}

