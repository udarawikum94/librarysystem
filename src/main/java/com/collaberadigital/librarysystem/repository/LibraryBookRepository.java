package com.collaberadigital.librarysystem.repository;

import com.collaberadigital.librarysystem.model.LibraryBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing LibraryBook entities.
 */
@Repository
public interface LibraryBookRepository extends JpaRepository<LibraryBook, Long> {

    /**
     * Finds books by their ISBN.
     *
     * @param isbn the ISBN of the books to find.
     * @return a list of books with the specified ISBN.
     */
    List<LibraryBook> findByIsbn(String isbn);

    /**
     * Finds books by their borrowed status.
     *
     * @param borrowStatus the borrowed status of the books to find.
     * @param pageable pagination information.
     * @return a paginated list of books with the specified borrowed status.
     */
    Page<LibraryBook> findByBorrowed(Boolean borrowStatus, Pageable pageable);
}
