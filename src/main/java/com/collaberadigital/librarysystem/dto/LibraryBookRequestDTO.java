package com.collaberadigital.librarysystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The LibraryBookRequestDTO class is a Data Transfer Object used to
 * encapsulate the data required to create or update a Library Book.
 * Fields:
 * - isbn: The International Standard Book Number, which is a unique identifier for the book.
 * It must be between 10 and 13 characters long.
 * - title: The title of the book. This field cannot be blank.
 * - author: The author of the book. This field cannot be blank.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LibraryBookRequestDTO {
    @NotBlank(message = "{NotBlank.book.isbn}")
    @Size(min = 10, max = 13, message = "{Size.book.isbn}")
    private String isbn;

    @NotBlank(message = "{NotBlank.book.title}")
    private String title;

    @NotBlank(message = "{NotBlank.book.author}")
    private String author;
}
