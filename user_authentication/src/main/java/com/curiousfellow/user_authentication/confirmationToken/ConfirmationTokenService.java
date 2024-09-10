package com.curiousfellow.user_authentication.confirmationToken;

import com.curiousfellow.user_authentication.domain.AppUser;

public interface ConfirmationTokenService {

    public ConfirmationToken generateToken(AppUser user, String token);

    public void confirmToken(ConfirmationToken token);

    public ConfirmationToken getConfirmationToken(String token);
}
