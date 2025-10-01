package com.gl.task.service;

import com.gl.task.repository.CustomerRepository;
import com.gl.task.dto.VerificationStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class VerificationService {

    private final RestTemplate restTemplate;
    private final String verificationUrl;

    public VerificationService(CustomerRepository repository,
                               @Value("${external.verification.url}") String verificationUrl) {
        this.restTemplate = new RestTemplate();
        this.verificationUrl = verificationUrl;
    }

    public CompletableFuture<VerificationStatus> verify(String email) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, String> payload = Map.of("email", email);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    verificationUrl,
                    payload,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String status = (String) response.getBody().get("status");
                return VerificationStatus.valueOf(status);
            }

            return VerificationStatus.FAILED;
        });
    }
}
