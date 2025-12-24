package com.QRPlatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailerService 
{
	@Autowired
    private JavaMailSender mailSender;

    @Async // <--- This runs the method in a separate thread
    public void sendScanNotification(String toEmail, String qrName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("harshhmehta.19@gmail.com");
            message.setTo(toEmail);
            message.setSubject("QR Code Scanned!");
            message.setText("Hello,\n\nYour QR Code '" + qrName + "' has just been scanned successfully.\n\nBest,\nQR Platform");

            mailSender.send(message);
            System.out.println("Scan notification sent to " + toEmail);
        } catch (Exception e) {
            // Log error but don't stop the app
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
