package com.gl.task;

import com.gl.task.dto.CustomerRequest;
import com.gl.task.dto.VerificationStatus;
import com.gl.task.service.VerificationService;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @MockitoBean
    private VerificationService verificationService;

    @Test
//    @DirtiesContext
    void shouldRegisterANewCustomer() {

        Mockito.when(verificationService.verify("example2@gmail.com")).thenReturn(CompletableFuture.completedFuture(VerificationStatus.VERIFIED));
        CustomerRequest customerRequest = new CustomerRequest("Pavlo", "Che", "example2@gmail.com");

        ResponseEntity<String> createResponse = restTemplate
                .withBasicAuth("pavlo", "abc123")
                .postForEntity("/customers", customerRequest, String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        DocumentContext documentContext = JsonPath.parse(createResponse.getBody());
        String email = documentContext.read("$.email");
        String verificationStatus = documentContext.read("$.status");

        assertThat(email).isEqualTo("example2@gmail.com");
        assertThat(verificationStatus).isEqualTo(VerificationStatus.VERIFIED.toString());
    }

    @Test
    void shouldNotCreateExistingCustomer() {
        CustomerRequest customerRequest = new CustomerRequest("Pavlo", "Che", "example@gmail.com");

        ResponseEntity<Void> createResponse = restTemplate
                .withBasicAuth("pavlo", "abc123")
                .postForEntity("/customers", customerRequest, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldReturnACustomer() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("pavlo", "abc123")
                .getForEntity("/customers/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String email = documentContext.read("$.email");
        String verificationStatus = documentContext.read("$.status");

        assertThat(email).isEqualTo("example@gmail.com");
        assertThat(verificationStatus).isEqualTo(VerificationStatus.PENDING.toString());
    }

}
