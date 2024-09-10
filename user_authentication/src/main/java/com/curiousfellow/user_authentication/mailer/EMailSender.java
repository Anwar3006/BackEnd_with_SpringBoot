package com.curiousfellow.user_authentication.mailer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EMailSender implements MailSenderRepository {

    private final Logger logger = LoggerFactory.getLogger(EMailSender.class);
    private JavaMailSender emailsender;

    public EMailSender(JavaMailSender emailsender) {
        this.emailsender = emailsender;
    }

    @Override
    @Async
    public void sendMail(String recipient, String body) {
        MimeMessage mimeMsg = emailsender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMsg, "utf-8");
        try {
            mimeMessageHelper.setTo(recipient);
            mimeMessageHelper.setFrom("user_auth@curiousfellow.com");
            mimeMessageHelper.setSubject("Account Confirmation: Please confirm your email");
            mimeMessageHelper.setText(body, true);
            emailsender.send(mimeMsg);
            logger.info("Mail sent successfully");
        } catch (Exception e) {
            logger.error("Error while sending mail: ", e);
            throw new MailSendException("Error while sending mail: ", e);
        }
    }

    public String buildEmail(String firstName, String link) {
        return "<html>" +
                "<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0;\">" +
                "<div style=\"background-color: #f4f4f4; padding: 20px;\">" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">" +
                "<tr>" +
                "<td align=\"center\" style=\"padding: 40px 0 30px 0;\">" +
                "<img src=\"https://yourdomain.com/logo.png\" alt=\"Company Logo\" width=\"150\" style=\"display: block;\" />"
                +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td align=\"center\" bgcolor=\"#ffffff\" style=\"padding: 40px 30px 40px 30px; border-radius: 10px;\">"
                +
                "<h2 style=\"color: #333333;\">Welcome to Our Service, " + firstName + "!</h2>" +
                "<p style=\"color: #666666; font-size: 16px;\">" +
                "Thank you for registering with us. Please confirm your email address by clicking the button below." +
                "</p>" +
                "<a href=\"" + link
                + "\" style=\"display: inline-block; padding: 15px 25px; font-size: 16px; color: #ffffff; background-color: #28a745; border-radius: 5px; text-decoration: none;\">Verify Your Email</a>"
                +
                "<p style=\"color: #666666; font-size: 14px; padding-top: 10px;\">" +
                "If the button above doesn't work, copy and paste the following link into your browser: <br>" +
                "<a href=\"" + link + "\" style=\"color: #28a745;\">" + link + "</a>" +
                "</p>" +
                "<p style=\"color: #666666; font-size: 16px; padding-top: 5px;\"> This link will expire in 15 minutes.</p>"
                +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td align=\"center\" style=\"padding: 30px 30px 30px 30px;\">" +
                "<p style=\"color: #999999; font-size: 12px;\">" +
                "If you did not register for this account, please ignore this email or contact support." +
                "</p>" +
                "<p style=\"color: #999999; font-size: 12px;\">" +
                "&copy; 2024 Your Company Name. All rights reserved." +
                "</p>" +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

}
