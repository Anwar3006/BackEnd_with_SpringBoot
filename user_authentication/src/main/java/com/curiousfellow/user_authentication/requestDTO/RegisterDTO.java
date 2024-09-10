package com.curiousfellow.user_authentication.requestDTO;

import com.curiousfellow.user_authentication.domain.USER_ROLES;

import lombok.Data;

@Data
public class RegisterDTO {

    private String fullName;
    private String email;
    private String password;
    private USER_ROLES role;
}
