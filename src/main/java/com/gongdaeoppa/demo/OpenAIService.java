package com.gongdaeoppa.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@Service
@RequiredArgsConstructor
public class OpenAIService {
    
    private final RestTemplate restTemplate;
    
    public String getAIResponse(String prompt) {
        String url = "https://api.openai.com/v1/chat/completions";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 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");
        headers.set("Content-Type", "application/json");
        
        String requestBody = String.format(
                "{\n" +
                        "  \"model\": \"gpt-3.5-turbo\",\n" +
                        "  \"messages\": [\n" +
                        "    {\"role\": \"user\", \"content\": \"%s\"}\n" +
                        "  ],\n" +
                        "  \"max_tokens\": 1000\n" +
                        "}", prompt);
        
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        
        int retries = 5;
        while (retries > 0) {
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
                return response.getBody();
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                    System.err.println("Too many requests. Retrying...");
                    retries--;
                    try {
                        Thread.sleep(60000);  // 1 minute delay before retrying
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    throw e;  // If not a 429 error, rethrow it
                }
            }
        }
        throw new RuntimeException("Exceeded retry attempts due to too many requests.");
    }
}
