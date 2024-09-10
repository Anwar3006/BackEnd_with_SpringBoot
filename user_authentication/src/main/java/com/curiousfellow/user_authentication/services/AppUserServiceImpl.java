package com.curiousfellow.user_authentication.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.curiousfellow.user_authentication.domain.AppUser;
import com.curiousfellow.user_authentication.exceptions.NoSuchUserExistsException;
import com.curiousfellow.user_authentication.repository.AppUserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private AppUserRepository userRepository;

    @Override
    public AppUser profile(String email) {
        Optional<AppUser> userExists = userRepository.findByEmail(email);
        if (userExists.isEmpty()) {
            throw new NoSuchUserExistsException(
                    "User with " + email + " not found. Consider Registering");
        }

        return userExists.get();

    }

}
