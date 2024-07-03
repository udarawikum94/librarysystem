package com.collaberadigital.librarysystem.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class contains unit tests for the LibraryBookRequestDTO class.
 */
@SpringBootTest
@AutoConfigureMockMvc
class LibraryBookRequestDTOTest {

    @Autowired
    private MockMvc mockMvc;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * Test case for validating a valid LibraryBookRequestDTO.
     */
    @Test
    void testValidBookRequestDTO() {
        LibraryBookRequestDTO bookRequestDTO = LibraryBookRequestDTO.builder()
                .isbn("0-061-96436-0")
                .title("Secret seven adventures")
                .author("Enid Bliton")
                .build();
        Set<ConstraintViolation<LibraryBookRequestDTO>> violations = validator.validate(bookRequestDTO);
        assertThat(violations).isEmpty();
    }

    /**
     * Test case for validating an invalid LibraryBookRequestDTO.
     */
    @Test
    void testInvalidBookRequestDTO() {
        Set<ConstraintViolation<LibraryBookRequestDTO>> violations = validator
                .validate(
                        LibraryBookRequestDTO.builder()
                                .isbn("")
                                .title("")
                                .author("")
                                .build());

        assertThat(violations).hasSize(4);
        assertThat(violations).extracting("message").containsExactlyInAnyOrder(
                "{NotBlank.book.isbn}",
                "{NotBlank.book.title}",
                "{NotBlank.book.author}",
                "{Size.book.isbn}"
        );
    }

    /**
     * Test case for validating bad request.
     * @throws Exception if there's an error during the test execution.
     */
    @Test
    void testBadRequestValidation() throws Exception {
        // Invalid request body with empty fields
        String requestBody = "{\"isbn\": \"\", \"title\": \"\", \"author\": \"\"}";

        mockMvc.perform(post("/api/v1/book/register")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}