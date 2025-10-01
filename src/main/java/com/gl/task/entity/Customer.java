package com.gl.task.entity;

import com.gl.task.dto.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus;
}
