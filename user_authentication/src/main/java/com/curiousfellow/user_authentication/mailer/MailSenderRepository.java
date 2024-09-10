package com.curiousfellow.user_authentication.mailer;

import org.springframework.stereotype.Repository;

@Repository
public interface MailSenderRepository {

    public void sendMail(String recipient, String body);
}
