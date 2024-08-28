package com.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRegistrationEmail(String to, String username) throws MessagingException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom("chiragsevent@gmail.com"); // change accordingly
            helper.setTo(to);
            helper.setSubject("Registration Successful");

            // Construct the email content using the provided template
            String content = "<h1>Welcome, " + username + "!</h1>"
                           + "<p>Thank you for registering with us. Your registration was successful!</p>"
                           + "<p>Best regards,<br>Chirag's Event</p>";

            // Set the email content and specify that it is HTML
            helper.setText(content, true);

            // Send the message
            mailSender.send(mimeMessage);
            System.out.println("Registration successful email sent successfully...");

        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}