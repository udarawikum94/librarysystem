package com.collaberadigital.librarysystem.controller;

import com.collaberadigital.librarysystem.annotations.CommonApiResponses;
import com.collaberadigital.librarysystem.dto.*;
import com.collaberadigital.librarysystem.service.BorrowerService;
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
 * Controller class for managing operations related to borrowers in the library system.
 * This class handles HTTP requests related to borrower management.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/borrower")
public class BorrowerController {

    // Service instance for handling borrower operations
    private final BorrowerService borrowerService;

    /**
     * Constructor to initialize the BorrowerController with a BorrowerService instance.
     *
     * @param borrowerService the service instance used to manage borrower operations
     */
    public BorrowerController(final BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    /**
     * Registers a new borrower in the library.
     *
     * @param borrowerDto the borrower details to register.
     * @return the registered borrower details.
     */
    @Operation(tags = "Borrower Management", description = "Register a new borrower")
    @CommonApiResponses
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Borrower registered successfully")
    })
    @PostMapping(value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BorrowerDTO> registerBorrower(@Valid @RequestBody final BorrowerRequestDTO borrowerDto) {
        log.info("Received request to register a borrower: {}", borrowerDto);

        final BorrowerDTO borrowerDTO = borrowerService.registerBorrower(borrowerDto);

        log.info("Borrower registered successfully: {}", borrowerDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(borrowerDTO);
    }

    /**
     * Fetches the details of a borrower by their ID.
     *
     * @param borrowerId the ID of the borrower.
     * @return the borrower details.
     */
    @Operation(tags = "Borrower Management", description = "Get borrower details by ID")
    @CommonApiResponses
    @GetMapping(value = "/{borrowerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BorrowerDTO> getBorrowerById(@PathVariable(name = "borrowerId") final Long borrowerId) {
        log.info("Received request to fetch borrower by ID: {}", borrowerId);

        final BorrowerDTO response = borrowerService.getBorrowerById(borrowerId);

        log.info("Fetched borrower successfully: {}", response);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * Fetches all borrowers with pagination and sorting options.
     *
     * @param pageNo   the page number to retrieve.
     * @param pageSize the number of items per page.
     * @param sortBy   the field to sort by.
     * @param sortDir  the direction of sorting (asc/desc).
     * @return a paginated list of borrowers.
     */
    @Operation(tags = "Borrower Management", description = "Get all borrowers")
    @CommonApiResponses
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BorrowerPageResponseDTO> getAllBorrower(
            @RequestParam(value = "pageNo", defaultValue = AppConstant.DEFAULT_PAGE_NO) final int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) final int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_SORT_BY) final String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_DIRECTION) final String sortDir) {

        log.info("Received request to fetch all borrowers");

        final BorrowerPageResponseDTO response = borrowerService.getAllBorrower(
                pageNo, pageSize, sortBy, sortDir);

        log.info("Fetched all borrowers successfully: {}", response);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
