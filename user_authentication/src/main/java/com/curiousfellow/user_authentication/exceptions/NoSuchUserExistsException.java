package com.curiousfellow.user_authentication.exceptions;

import lombok.NoArgsConstructor;

/**
 * Exception to be throw when user tries to get/update or delete a user that
 * doesn't exist
 */
@NoArgsConstructor
public class NoSuchUserExistsException extends RuntimeException {

    @SuppressWarnings("unused")
    private String message;

    public NoSuchUserExistsException(String message) {
        super(message);
        this.message = message;
    }
}
