package com.collaberadigital.librarysystem.controller;

import com.collaberadigital.librarysystem.annotations.CommonApiResponses;
import com.collaberadigital.librarysystem.dto.BorrowingInfoDTO;
import com.collaberadigital.librarysystem.dto.BorrowingPageResponseDTO;
import com.collaberadigital.librarysystem.service.BorrowingService;
import com.collaberadigital.librarysystem.util.AppConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for managing borrowing operations in the library system.
 * This class handles HTTP requests related to borrowing management.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/borrowing")
public class BorrowingController {

    // Tag value used for BorrowingController in Swagger API operations
    private static final String TAG_NAME = "Borrowing Management";

    // Service instance for handling borrowing operations
    private final BorrowingService borrowingService;

    /**
     * Constructor to initialize the BorrowingController with a BorrowingService instance.
     *
     * @param borrowingService the service instance used to manage borrowing operations
     */
    public BorrowingController(final BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    /**
     * Fetches borrowing information by borrower ID and book ID.
     *
     * @param borrowerId the ID of the borrower.
     * @param bookId the ID of the book.
     * @return the borrowing information.
     */
    @Operation(tags = TAG_NAME, description = "Get borrowing info by borrower ID and book ID")
    @CommonApiResponses
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Reference info not found")
    })
    @GetMapping(value = "/getBorrowingInfo/{borrowerId}/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BorrowingInfoDTO> getBorrowingInfoByBorrowerAndBook(
            @PathVariable(name = "borrowerId") final Long borrowerId,
            @PathVariable(name = "bookId") final Long bookId) {

        log.info("Received request to fetch borrowing info for borrowerId: {} and bookId: {}", borrowerId, bookId);

        final BorrowingInfoDTO response = borrowingService
                .getBorrowingInfoByBorrowerAndBook(borrowerId, bookId);
        log.info("Fetched borrowing info successfully: {}", response);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * Fetches all books borrowed by a borrower.
     *
     * @param borrowerId the ID of the borrower.
     * @param pageNo the page number to retrieve.
     * @param pageSize the number of items per page.
     * @param sortBy the field to sort by.
     * @param sortDir the direction of sorting (asc/desc).
     * @return a paginated list of borrowed books.
     */
    @Operation(tags = TAG_NAME, description = "Get all books borrowed by borrower")
    @CommonApiResponses
    @GetMapping(value = "/{borrowerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BorrowingPageResponseDTO> getAllBooksByBorrower(
            @PathVariable final Long borrowerId,
            @RequestParam(value = "pageNo", defaultValue = AppConstant.DEFAULT_PAGE_NO) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) final int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_SORT_BY) final String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "DESC") final String sortDir) {

        log.info("Received request to fetch all books borrowed by borrowerId: {}", borrowerId);

        final BorrowingPageResponseDTO response = borrowingService.getBorrowingInfoByBorrower(
                borrowerId, pageNo, pageSize, sortBy, sortDir);

        log.info("Fetched all books borrowed by borrowerId {} successfully: {}", borrowerId, response);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * Borrows a book for a borrower.
     *
     * @param bookId the ID of the book to borrow.
     * @param borrowerId the ID of the borrower.
     * @return the borrowing information.
     */
    @Operation(tags = TAG_NAME, description = "Borrow a book")
    @CommonApiResponses
    @PostMapping(value = "/{bookId}/borrow",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BorrowingInfoDTO> borrowBook(
            @PathVariable final Long bookId, @RequestParam final Long borrowerId) {

        log.info("Received request to borrow book with bookId: {} for borrowerId: {}", bookId, borrowerId);

        final BorrowingInfoDTO response = borrowingService.borrowBook(bookId, borrowerId);

        log.info("LibraryBook borrowed successfully: {}", response);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * Returns a borrowed book.
     *
     * @param borrowingId the ID of the borrowing record.
     * @return the borrowing information.
     */
    @Operation(tags = TAG_NAME, description = "Return a borrowed book")
    @CommonApiResponses
    @PutMapping(value = "/{borrowingId}/return",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BorrowingInfoDTO> returnBook(
            @PathVariable final Long borrowingId) {

        log.info("Received request to return book with borrowingId: {}", borrowingId);

        final BorrowingInfoDTO response = borrowingService.returnBook(borrowingId);

        log.info("LibraryBook returned successfully: {}", response);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
