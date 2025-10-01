package com.gl.task.util;

import com.gl.task.dto.CustomerRequest;
import com.gl.task.dto.CustomerResponse;
import com.gl.task.entity.Customer;

public class CustomerMapper {

    public static Customer toEntity(CustomerRequest req) {
        Customer c = new Customer();
        c.setFirstName(req.firstName());
        c.setLastName(req.lastName());
        c.setEmail(req.email().toLowerCase());
        return c;
    }

    public static CustomerResponse toResponse(Customer c) {
        return new CustomerResponse(
                c.getFirstName(),
                c.getLastName(),
                c.getEmail(),
                c.getVerificationStatus()
        );
    }
}
