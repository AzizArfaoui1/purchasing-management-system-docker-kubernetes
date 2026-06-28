package tn.esprit.rh.achat.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler({
            BadCredentialsException.class,
            UsernameNotFoundException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<Void> handleUnauthorized() {
        return ResponseEntity.status(401).build();
    }
}
