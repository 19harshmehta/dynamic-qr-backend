package com.QRPlatform.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class MailerService {

    @Value("${discord.webhook.url}")
    private String webhookUrl;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Async
    public void sendScanNotification(String ignoredEmail, String qrName) {
        try {
            // Simple JSON payload for Discord
            String jsonPayload = String.format(
                "{\"content\": \"🚨 **QR Code Scanned!** 🚨\\nYour QR Code **'%s'** was just visited.\"}", 
                qrName
            );

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webhookUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Discord notification sent!");

        } catch (Exception e) {
            System.err.println("Failed to send Discord notification: " + e.getMessage());
        }
    }
}