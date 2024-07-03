package com.collaberadigital.librarysystem.service;

import com.collaberadigital.librarysystem.dto.BorrowingInfoDTO;
import com.collaberadigital.librarysystem.dto.BorrowingPageResponseDTO;

/**
 * Service interface for managing book borrowings in the library.
 */
public interface BorrowingService {

    /**
     * Borrows a book for a borrower.
     *
     * @param bookId the ID of the book to be borrowed.
     * @param borrowerId the ID of the borrower who is borrowing the book.
     * @return the borrowing information of the borrowed book.
     */
    BorrowingInfoDTO borrowBook(long bookId, long borrowerId);

    /**
     * Returns a borrowed book.
     *
     * @param borrowingId the ID of the borrowing record.
     * @return the borrowing information of the returned book.
     */
    BorrowingInfoDTO returnBook(long borrowingId);

    /**
     * Retrieves borrowing information by borrower ID and book ID.
     *
     * @param borrowerId the ID of the borrower.
     * @param bookId the ID of the book.
     * @return the borrowing information for the specified borrower and book.
     */
    BorrowingInfoDTO getBorrowingInfoByBorrowerAndBook(long borrowerId, long bookId);

    /**
     * Retrieves a paginated list of all books borrowed by a specific borrower.
     *
     * @param borrowerId the ID of the borrower.
     * @param pageNo the page number to retrieve.
     * @param pageSize the number of borrowings per page.
     * @param sortBy the field by which to sort the borrowings.
     * @param sortDir the direction in which to sort the borrowings (ASC/DESC).
     * @return a paginated response containing the list of borrowings for the specified borrower.
     */
    BorrowingPageResponseDTO getBorrowingInfoByBorrower(
            long borrowerId,int pageNo, int pageSize,
            String sortBy, String sortDir);
}
