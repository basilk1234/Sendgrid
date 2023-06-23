
package com.example.WebApp.exception;
        import org.springframework.http.HttpStatus;
        import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Invalid Login")
public class InvalidLoginException extends RuntimeException{
}
