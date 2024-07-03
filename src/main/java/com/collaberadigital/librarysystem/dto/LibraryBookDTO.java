package com.collaberadigital.librarysystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) representing a book entity.
 * This class extends the {@link LibraryBookRequestDTO} class.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LibraryBookDTO extends LibraryBookRequestDTO {

    /**
     * Unique identifier for the book information.
     */
    @JsonProperty(value = "bookId")
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private boolean borrowed;
}
