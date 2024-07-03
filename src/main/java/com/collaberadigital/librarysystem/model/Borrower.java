package com.collaberadigital.librarysystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * Represents a borrower entity in the library system.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "borrower")
@EqualsAndHashCode(callSuper = true)
public class Borrower extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

}
