package com.collaberadigital.librarysystem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * Represents a borrowing entity in the library system.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "borrow_map")
@EqualsAndHashCode(callSuper = true)
public class Borrowing extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private LibraryBook book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id")
    private Borrower borrower;

    @Column(nullable = false)
    private LocalDateTime borrowDate;

    @Column(nullable = true)
    private LocalDateTime returnDate;
}
