package com.example.WebApp.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.WebApp.model.User;
import com.example.WebApp.Service.UserService;

import java.util.Date;
import java.util.logging.Logger;

@RestController
@Slf4j
@RequestMapping("/api/registration")
public class RegistrationController {

    private final UserService userService;

    
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        log.info("Request received");
        user.setCreationDate(new Date());
        user.setUpdateDate(new Date());
        User registeredUser = userService.register(user);
        log.info("Saved user and respond");
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }
}
