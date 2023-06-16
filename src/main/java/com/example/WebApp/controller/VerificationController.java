package com.example.WebApp.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationController {
    @GetMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        
        return ResponseEntity.ok("Email verified successfully");
    }
}
