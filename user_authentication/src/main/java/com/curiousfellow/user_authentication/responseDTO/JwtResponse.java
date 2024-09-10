package com.curiousfellow.user_authentication.responseDTO;

import lombok.Data;

@Data
public class JwtResponse {

    private String jwt;
    private String message;
}
