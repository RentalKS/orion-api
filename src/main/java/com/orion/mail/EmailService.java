package com.orion.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Log4j2
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAgreementEmail(String to, String token, LocalDateTime expirationTime) {
        try {
            String formattedExpirationTime = expirationTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String url = "https://localhost:8080/sign?token=" + token;
            String subject = "Sign Your Rental Agreement";

//            String logoUrl = "https://orionlogo.com/logo.png"; // URL to your company's logo
            String body = "<html>"
                        + "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0;'>"
                        + "<div style='background-color: #f7f7f7; padding: 20px;'>"
                        + "<div style='max-width: 600px; margin: auto; background-color: white; padding: 20px; border-radius: 10px;'>"
                        + "<div style='text-align: center;'>"
//                        + "<img src='" + logoUrl + "' alt='Company Logo' style='width: 150px; height: auto;' />"
                        + "</div>"
                        + "<h2 style='color: #333;'>Sign Your Rental Agreement</h2>"
                        + "<p style='color: #555;'>Please click the button below to sign your rental agreement. This link is valid until <strong>"
                        + formattedExpirationTime + "</strong>:</p>"
                        + "<div style='text-align: center; margin: 20px;'>"
                        + "<a href='" + url + "' style='background-color: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>"
                        + "Sign Agreement</a>"
                        + "</div>"
                        + "<p style='color: #555;'>If you have any questions, please contact our support team.</p>"
                        + "<p style='color: #777; font-size: 12px;'>This is an automated email, please do not reply.</p>"
                        + "</div>"
                        + "</div>"
                        + "</body>"
                        + "</html>";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
            log.info("Agreement email sent to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send agreement email to {}. Error: {}", to, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while sending agreement email to {}. Error: {}", to, e.getMessage());
        }
    }
}