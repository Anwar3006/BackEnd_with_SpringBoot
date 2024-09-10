package com.curiousfellow.user_authentication.services;

import java.util.Optional;
import java.util.UUID;
// import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import com.curiousfellow.user_authentication.confirmationToken.ConfirmationToken;
import com.curiousfellow.user_authentication.confirmationToken.ConfirmationTokenService;
import com.curiousfellow.user_authentication.domain.AppUser;
import com.curiousfellow.user_authentication.exceptions.NoSuchUserExistsException;
import com.curiousfellow.user_authentication.exceptions.UserAlreadyExistsException;
import com.curiousfellow.user_authentication.mailer.EMailSender;
import com.curiousfellow.user_authentication.repository.AppUserRepository;
import com.curiousfellow.user_authentication.requestDTO.LoginDTO;
import com.curiousfellow.user_authentication.requestDTO.RegisterDTO;
import com.curiousfellow.user_authentication.security.PasswordEncoder;
import com.curiousfellow.user_authentication.security.jwt.JwtGenerator;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;

    private final ConfirmationTokenService confirmationTokenService;
    private final EMailSender mailSender;

    @Override
    public AppUser registerUser(RegisterDTO registerReq) {
        Optional<AppUser> user = userRepository.findByEmail(registerReq.getEmail());
        if (user.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + registerReq.getEmail() + " already exists");
        }

        AppUser newUser = new AppUser();
        newUser.setFullName(registerReq.getFullName());
        newUser.setEmail(registerReq.getEmail());
        newUser.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(registerReq.getPassword()));
        newUser.setRole(registerReq.getRole());
        AppUser savedUser = userRepository.save(newUser);

        String randUUID = UUID.randomUUID().toString();
        ConfirmationToken token = confirmationTokenService.generateToken(newUser, randUUID);

        String confirmationLink = "http://localhost:8080/api/v1/auth/confirm?token=" + token.getToken();
        final String BODY = mailSender.buildEmail(newUser.getFullName().split(" ")[0], confirmationLink);
        mailSender.sendMail(newUser.getEmail(), BODY);

        // return jwtGenerator.generateToken(auth);
        return savedUser;
    }

    @Override
    public String loginUser(LoginDTO loginReq) {
        Optional<AppUser> userExists = userRepository.findByEmail(loginReq.getEmail());
        if (userExists.isEmpty()) {
            throw new NoSuchUserExistsException(
                    "User with " + loginReq.getEmail() + " not found. Consider Registering");
        }

        AppUser user = userExists.get();
        if (!user.isEnabled()) {
            throw new DisabledException("Account not confirmed. Please confirm your account.");
        }
        if (!passwordEncoder.bCryptPasswordEncoder().matches(loginReq.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password...");
        }

        Authentication authObj = createAuthObj(user);
        return jwtGenerator.generateToken(authObj);
    }

    @Override
    public void enableUser(String token) {
        ConfirmationToken ctoken = confirmationTokenService.getConfirmationToken(token);
        confirmationTokenService.confirmToken(ctoken);

        Optional<AppUser> getUser = userRepository.findById(ctoken.getUser().getId());
        if (getUser.isEmpty()) {
            throw new BadCredentialsException("Invalid token");
        }
        AppUser user = getUser.get();
        user.setEnabled(true);
        userRepository.save(user);
    }

    // Helper Method
    private Authentication createAuthObj(AppUser user) {
        Authentication authObj = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authObj);
        return authObj;
    }

}
