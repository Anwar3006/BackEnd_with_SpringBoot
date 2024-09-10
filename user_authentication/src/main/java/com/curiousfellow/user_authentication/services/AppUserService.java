package com.curiousfellow.user_authentication.services;

import com.curiousfellow.user_authentication.domain.AppUser;

public interface AppUserService {

    public AppUser profile(String email);
}
