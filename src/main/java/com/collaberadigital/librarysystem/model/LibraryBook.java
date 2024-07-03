package com.collaberadigital.librarysystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * Represents a book entity in the library system.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "library_book")
@EqualsAndHashCode(callSuper = true)
public class LibraryBook extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private boolean borrowed;

}
