package com.curiousfellow.user_authentication.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserAlreadyExistsException extends RuntimeException {

    @SuppressWarnings("unused")
    private String message;

    public UserAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }
}
