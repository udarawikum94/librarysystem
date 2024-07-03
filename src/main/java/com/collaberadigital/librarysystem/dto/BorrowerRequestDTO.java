package com.collaberadigital.librarysystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) representing a request to create or update a borrower.
 * This class encapsulates the necessary information for creating or updating a borrower entity.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowerRequestDTO {
    @NotBlank(message = "{NotBlank.borrower.name}")
    private String name;

    @NotBlank(message = "{NotBlank.borrower.email}")
    @Email(message = "{Email.borrower.email}")
    private String email;
}
