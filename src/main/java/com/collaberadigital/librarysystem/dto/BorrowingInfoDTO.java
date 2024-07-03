package com.collaberadigital.librarysystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing a request to create or update a borrower.
 * This class encapsulates the necessary information for creating or updating a borrower entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowingInfoDTO {
    /**
     * Unique identifier for the borrowing information.
     */
    @JsonProperty(value = "borrowingId")
    private Long id;
    private BorrowerDTO borrower;
    private LibraryBookDTO bookInfo;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private boolean isBorrowed;

    /**
     * Determines if the book is currently borrowed based on the return date.
     *
     * @return true if the book is currently borrowed; false otherwise.
     */
    public boolean isBorrowed() {
        return returnDate == null
                ? Boolean.TRUE : Boolean.FALSE;
    }
}
