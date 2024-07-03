package com.collaberadigital.librarysystem.service;

import com.collaberadigital.librarysystem.dto.LibraryBookDTO;
import com.collaberadigital.librarysystem.dto.LibraryBookPageResponseDTO;
import com.collaberadigital.librarysystem.dto.LibraryBookRequestDTO;

/**
 * Service interface for managing books in the library.
 */
public interface LibraryBookService {

    /**
     * Registers a new book in the library.
     *
     * @param bookDTO the details of the book to be registered.
     * @return the details of the registered book.
     */
    LibraryBookDTO registerBook(LibraryBookRequestDTO bookDTO);

    /**
     * Retrieves the details of a book by its ID.
     *
     * @param bookId the ID of the book to be retrieved.
     * @return the details of the book.
     */
    LibraryBookDTO getBookById(long bookId);

    /**
     * Retrieves a paginated list of all books in the library.
     *
     * @param pageNo the page number to retrieve.
     * @param pageSize the number of books per page.
     * @param sortBy the field by which to sort the books.
     * @param sortDir the direction in which to sort the books (ASC/DESC).
     * @return a paginated response containing the list of books.
     */
    LibraryBookPageResponseDTO getAllBooks(
            int pageNo, int pageSize,
            String sortBy, String sortDir);

    /**
     * Retrieves a paginated list of all available books for borrowing.
     *
     * @param pageNo the page number to retrieve.
     * @param pageSize the number of books per page.
     * @param sortBy the field by which to sort the books.
     * @param sortDir the direction in which to sort the books (ASC/DESC).
     * @return a paginated response containing the list of available books for borrowing.
     */
    LibraryBookPageResponseDTO getAllAvailableBorrow(
            int pageNo, int pageSize,
            String sortBy, String sortDir);
}
