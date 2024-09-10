package com.curiousfellow.user_authentication.requestDTO;

import lombok.Data;

@Data
public class LoginDTO {

    private String email;
    private String password;
}
