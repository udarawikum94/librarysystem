package com.collaberadigital.librarysystem.controller;

import com.collaberadigital.librarysystem.annotations.CommonApiResponses;
import com.collaberadigital.librarysystem.dto.LibraryBookDTO;
import com.collaberadigital.librarysystem.dto.LibraryBookPageResponseDTO;
import com.collaberadigital.librarysystem.dto.LibraryBookRequestDTO;
import com.collaberadigital.librarysystem.service.LibraryBookService;
import com.collaberadigital.librarysystem.util.AppConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for managing operations related to books in the library system.
 * This class handles HTTP requests related to library book management.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/book")
public class LibraryBookController {

    // Service instance for handling library book operations
    private final LibraryBookService libraryBookService;

    /**
     * Constructor to initialize the LibraryBookController with a LibraryBookService instance.
     *
     * @param bookService the service instance used to interact with library book data
     */
    public LibraryBookController(final LibraryBookService bookService) {
        this.libraryBookService = bookService;
    }

    /**
     * Registers a new book in the library.
     *
     * @param bookDTO the book details to register.
     * @return the registered book details.
     */
    @Operation(tags = "Library Book Management", description = "Register a new book to the library")
    @CommonApiResponses
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Library book registered successfully")
    })
    @PostMapping(value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LibraryBookDTO> registerBook(
            @Valid @RequestBody final LibraryBookRequestDTO bookDTO) {
        log.info("Received request to register a new book: {}", bookDTO);

        final LibraryBookDTO dto = libraryBookService.registerBook(bookDTO);
        log.info("LibraryBook registered successfully: {}", dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto);
    }


    /**
     * Fetches the details of a book by its ID.
     *
     * @param bookId the ID of the book.
     * @return the book details.
     */
    @Operation(tags = "Library Book Management", description = "Fetch book details by ID")
    @CommonApiResponses
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "No data available for given book ID")
    })
    @GetMapping(value = "/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LibraryBookDTO> getPostById(@PathVariable(name = "bookId") Long bookId) {
        log.info("Received request to fetch book details for ID: {}", bookId);

        final LibraryBookDTO response = libraryBookService.getBookById(bookId);
        log.info("LibraryBook details fetched successfully: {}", response);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * Fetches all books with pagination and sorting options.
     *
     * @param pageNo   the page number to retrieve.
     * @param pageSize the number of items per page.
     * @param sortBy   the field to sort by.
     * @param sortDir  the direction of sorting (asc/desc).
     * @return a paginated list of books.
     */
    @Operation(tags = "Library Book Management", description = "Fetch all books")
    @CommonApiResponses
    @GetMapping(value = "/getAllBooks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LibraryBookPageResponseDTO> getAllBooks(
            @RequestParam(value = "pageNo", defaultValue = AppConstant.DEFAULT_PAGE_NO) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) final int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_SORT_BY) final String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_DIRECTION) final String sortDir) {

        log.info("Received request to fetch all books");

        final LibraryBookPageResponseDTO response = libraryBookService.getAllBooks(
                pageNo, pageSize, sortBy, sortDir);
        log.info("Fetched all books successfully: {}", response);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * Fetches all available books for borrowing with pagination and sorting options.
     *
     * @param pageNo   the page number to retrieve.
     * @param pageSize the number of items per page.
     * @param sortBy   the field to sort by.
     * @param sortDir  the direction of sorting (asc/desc).
     * @return a paginated list of available books for borrowing.
     */
    @Operation(tags = "Library Book Management", description = "Fetch all available books to borrow")
    @CommonApiResponses
    @GetMapping(value = "/getAvailable", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LibraryBookPageResponseDTO> getAllAvailableToBorrow(
            @RequestParam(value = "pageNo", defaultValue = AppConstant.DEFAULT_PAGE_NO) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) final int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_SORT_BY) final String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_DIRECTION) final String sortDir) {

        log.info("Received request to fetch all available books to borrow");

        final LibraryBookPageResponseDTO response = libraryBookService.getAllAvailableBorrow(
                pageNo, pageSize, sortBy, sortDir);
        log.info("Fetched all available books to borrow successfully: {}", response);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
