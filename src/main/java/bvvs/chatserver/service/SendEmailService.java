package bvvs.chatserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmailService {
    @Autowired public JavaMailSender emailSender;

    public void sendEmail(String recipientEmail, String messageText) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Service Notification");
        message.setText("You have a new message: " + messageText);
        this.emailSender.send(message);
    }
}

