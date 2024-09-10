package com.curiousfellow.user_authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NoSuchUserExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody CustomErrorResponse handleNoUserException(NoSuchUserExistsException ex) {
        return new CustomErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody CustomErrorResponse handleUserExistsException(UserAlreadyExistsException ex) {
        return new CustomErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody CustomErrorResponse handleBadRequestException(BadCredentialsException ex) {
        return new CustomErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(value = DisabledException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public @ResponseBody CustomErrorResponse handleForbiddenException(DisabledException ex) {
        return new CustomErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }
}
