package com.gl.task.controller;


import com.gl.task.dto.CustomerResponse;
import com.gl.task.dto.CustomerRequest;
import com.gl.task.entity.Customer;
import com.gl.task.service.CustomerService;
import com.gl.task.util.CustomerMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> registerNewCustomer(@RequestBody CustomerRequest customerRequest,
                                                    UriComponentsBuilder ucb) {
        if (customerService.existCustomer(customerRequest.email())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Customer savedCustomer = customerService.register(customerRequest);
        CustomerResponse response = CustomerMapper.toResponse(savedCustomer);

        URI customerLocationURI = ucb
                .path("customers/{id}")
                .buildAndExpand(savedCustomer.getId())
                .toUri();

        return ResponseEntity.created(customerLocationURI).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findCustomer(@PathVariable Long id) {
        Optional<Customer> optionalCustomer = customerService.getCustomer(id);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            CustomerResponse response = CustomerMapper.toResponse(customer);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

}
