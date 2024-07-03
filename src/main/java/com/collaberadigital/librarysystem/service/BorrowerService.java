package com.collaberadigital.librarysystem.service;

import com.collaberadigital.librarysystem.dto.*;

/**
 * Service interface for managing borrowers in the library.
 */
public interface BorrowerService {

    /**
     * Retrieves the details of a borrower by their ID.
     *
     * @param borrowerId the ID of the borrower to be retrieved.
     * @return the details of the borrower.
     */
    BorrowerDTO getBorrowerById(long borrowerId);

    /**
     * Retrieves a paginated list of all borrowers in the library.
     *
     * @param pageNo the page number to retrieve.
     * @param pageSize the number of borrowers per page.
     * @param sortBy the field by which to sort the borrowers.
     * @param sortDir the direction in which to sort the borrowers (ASC/DESC).
     * @return a paginated response containing the list of borrowers.
     */
    BorrowerPageResponseDTO getAllBorrower(
            int pageNo, int pageSize,
            String sortBy, String sortDir);

    /**
     * Registers a new borrower in the library.
     *
     * @param borrowerDto the details of the borrower to be registered.
     * @return the details of the registered borrower.
     */
    BorrowerDTO registerBorrower(BorrowerRequestDTO borrowerDto);

}
