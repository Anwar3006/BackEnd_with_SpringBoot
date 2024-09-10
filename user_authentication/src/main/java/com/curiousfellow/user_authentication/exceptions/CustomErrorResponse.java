package com.curiousfellow.user_authentication.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorResponse {

    private int statusCode;
    private String message;

    public CustomErrorResponse(String message) {
        super();
        this.message = message;
    }
}
