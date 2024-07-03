package com.collaberadigital.librarysystem.repository;

import com.collaberadigital.librarysystem.model.Borrowing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing Borrowing entities.
 */
@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    /**
     * Checks if there is an active borrowing record for a given book ID.
     * An active borrowing record is one where the return date is null.
     *
     * @param bookId the ID of the book to check.
     * @return true if there is an active borrowing record for the book, false otherwise.
     */
    boolean existsByBookIdAndReturnDateIsNull(Long bookId);

    /**
     * Finds the latest borrowing record for a given borrower ID and book ID.
     *
     * @param borrowerId the ID of the borrower.
     * @param bookId the ID of the book.
     * @return an Optional containing the latest borrowing record if found, or empty otherwise.
     */
    Optional<Borrowing> findTopByBorrowerIdAndBookId(Long borrowerId, Long bookId);

    /**
     * Finds all borrowing records for a given borrower ID with pagination support.
     *
     * @param borrowerId the ID of the borrower.
     * @param pageable the pagination information.
     * @return a page of borrowing records for the specified borrower ID.
     */
    Page<Borrowing> findByBorrowerId(Long borrowerId, Pageable pageable);
}
