package com.curiousfellow.user_authentication.confirmationToken;

import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.curiousfellow.user_authentication.domain.AppUser;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public ConfirmationToken generateToken(AppUser user, String token) {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setUser(user);
        confirmationToken.setToken(token);

        return confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public void confirmToken(ConfirmationToken token) {
        ConfirmationToken getToken = getConfirmationToken(token.getToken());

        getToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(getToken);
    }

    @Override
    public ConfirmationToken getConfirmationToken(String token) {
        Optional<ConfirmationToken> getToken = confirmationTokenRepository.findByToken(token);
        if (getToken.isEmpty()) {
            throw new BadCredentialsException("Invalid token");
        }

        return getToken.get();
    }
}