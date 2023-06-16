package com.example.WebApp.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationController {
    @GetMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        // Parse the token and validate it
        // Mark the user as verified in the database
        // Return a success response or redirect to a confirmation page
        return ResponseEntity.ok("Email verified successfully");
    }
}
