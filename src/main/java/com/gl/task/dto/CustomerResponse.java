package com.gl.task.dto;

public record CustomerResponse(
        String firstName,
        String lastName,
        String email,
        VerificationStatus status
) {
    //default value when the status equals null
    public CustomerResponse {
        if (status == null) {
            status = VerificationStatus.PENDING;
        }
    }
}
