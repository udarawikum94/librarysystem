package com.collaberadigital.librarysystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
