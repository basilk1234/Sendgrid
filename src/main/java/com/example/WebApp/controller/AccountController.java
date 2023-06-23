
package com.example.WebApp.controller;
        import com.example.WebApp.Service.AuthenticationService;
        import com.example.WebApp.Service.UserService;
        import com.example.WebApp.config.AccessTokenRequired;
        import com.example.WebApp.dto.Token;
        import lombok.extern.slf4j.Slf4j;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;
        import com.example.WebApp.model.User;

@RestController
@Slf4j
@RequestMapping("/api/account")
public class AccountController {

    private final UserService userService;
    private final AuthenticationService authenticationService;


    public AccountController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    @AccessTokenRequired
    public ResponseEntity<User> getUser(@RequestHeader(required = true) String token) {
        User user =  userService.getUser(token);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    @AccessTokenRequired
    public ResponseEntity<User> updateUser(@RequestHeader(required = true) String token, @RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    @AccessTokenRequired
    public ResponseEntity<User> deleteUser(@RequestHeader(required = true) String token) {
        userService.deleteUser(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
