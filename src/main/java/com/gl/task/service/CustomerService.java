package com.gl.task.service;

import com.gl.task.repository.CustomerRepository;
import com.gl.task.dto.CustomerRequest;
import com.gl.task.entity.Customer;
import com.gl.task.util.CustomerMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final VerificationService verificationService;

    public CustomerService(CustomerRepository repository,
                           VerificationService verificationService) {
        this.repository = repository;
        this.verificationService = verificationService;
    }

    public Customer register(CustomerRequest request) {

        Customer registeredCustomer = CustomerMapper.toEntity(request);
        Customer saved = repository.save(registeredCustomer);

//         trigger background verification
        verificationService.verify(saved.getEmail())
                .thenAccept(status -> {
                    saved.setVerificationStatus(status);
                    repository.save(saved);  // update asynchronously
                });

        return repository.save(saved);
    }

    public Optional<Customer> getCustomer(Long id) {
        return repository.findById(id);
    }

    public boolean existCustomer(String email) {
        return repository.existsByEmail(email);
    }
}
