package com.example.WebApp.controller;
import com.example.WebApp.Service.AuthenticationService;
import com.example.WebApp.Service.UserService;
import com.example.WebApp.dto.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.WebApp.model.User;

@RestController
@Slf4j
@RequestMapping("/api/login")
public class LoginController {

    private final UserService userService;
    private final AuthenticationService authenticationService;


    public LoginController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<Token> loginUser(@RequestBody User user) {
        log.info("Login Request received");
        Token token =  userService.login(user);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
