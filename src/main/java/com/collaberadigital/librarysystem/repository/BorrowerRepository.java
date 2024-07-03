package com.collaberadigital.librarysystem.repository;

import com.collaberadigital.librarysystem.model.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Borrower entities.
 */
@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

    /**
     * Checks if a borrower exists by their email.
     *
     * @param email the email of the borrower to check.
     * @return true if a borrower with the specified email exists, false otherwise.
     */
    boolean existsByEmail(String email);
}
