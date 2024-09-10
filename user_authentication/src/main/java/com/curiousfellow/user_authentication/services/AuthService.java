package com.curiousfellow.user_authentication.services;

import com.curiousfellow.user_authentication.domain.AppUser;
import com.curiousfellow.user_authentication.requestDTO.LoginDTO;
import com.curiousfellow.user_authentication.requestDTO.RegisterDTO;

public interface AuthService {

    public AppUser registerUser(RegisterDTO registerReq);

    public String loginUser(LoginDTO loginReq);

    public void enableUser(String token);
}
