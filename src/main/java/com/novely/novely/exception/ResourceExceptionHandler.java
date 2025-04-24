package com.novely.novely.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {
    
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
        
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Not Found", e.getMessage(), request.getRequestURI());
        
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(SignUpException.class)
    public ResponseEntity<StandardError> signUpException(SignUpException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Bad request", e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<StandardError> UnauthorizedActionException(UnauthorizedActionException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Forbidden", e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(FavoritesException.class)
    public ResponseEntity<StandardError> FavoritesException(FavoritesException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Conflict", e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardError> handleBadCredentials(BadCredentialsException e, HttpServletRequest request) {
        
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(),"Authentication Failed", "Invalid email or password", request.getRequestURI());
        
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<StandardError> AuthenticationServiceException(AuthenticationServiceException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(),"Authentication Failed", e.getMessage(), request.getRequestURI());
        
        return ResponseEntity.status(status).body(err);
    }
}
