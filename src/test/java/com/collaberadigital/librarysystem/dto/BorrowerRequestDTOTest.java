package com.collaberadigital.librarysystem.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class contains unit tests for the BorrowerRequestDTO class.
 */
@SpringBootTest
@AutoConfigureMockMvc
class BorrowerRequestDTOTest {
    @Autowired
    private MockMvc mockMvc;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * Test case for validating bad request.
     * @throws Exception if there's an error during the test execution.
     */
    @Test
    void testBorrowerRequestDtoValidation() throws Exception {
        String invalidJson = "{ \"name\": \"\", \"email\": \"invalid-email\" }";
        mockMvc.perform(post("/api/v1/borrower/register")
                        .contentType("application/json")
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test case for validating a valid BorrowerRequestDTO.
     */
    @Test
    void testValidBorrowerRequestDto() {
        BorrowerRequestDTO dto = new BorrowerRequestDTO();
        dto.setName("John Doe");
        dto.setEmail("john@example.com");

        Set<ConstraintViolation<BorrowerRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    /**
     * Test case for validating an invalid BorrowerRequestDTO.
     */
    @Test
    void testInvalidBorrowerRequestDto() {
        BorrowerRequestDTO dto = new BorrowerRequestDTO();
        dto.setName("");
        dto.setEmail("invalid-email");

        Set<ConstraintViolation<BorrowerRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting("message").containsExactlyInAnyOrder(
                "{NotBlank.borrower.name}",
                "{Email.borrower.email}"
        );
    }
}
