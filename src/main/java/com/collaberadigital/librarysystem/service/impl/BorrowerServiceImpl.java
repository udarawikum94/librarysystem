package com.collaberadigital.librarysystem.service.impl;

import com.collaberadigital.librarysystem.dto.*;
import com.collaberadigital.librarysystem.exception.InvalidBorrowerException;
import com.collaberadigital.librarysystem.exception.ResourceNotFoundException;
import com.collaberadigital.librarysystem.model.Borrower;
import com.collaberadigital.librarysystem.repository.BorrowerRepository;
import com.collaberadigital.librarysystem.service.BorrowerService;
import com.collaberadigital.librarysystem.util.AppConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the BorrowerService interface for managing borrower operations.
 */
@Service
public class BorrowerServiceImpl implements BorrowerService {

    /**
     * Logger instance for logging messages.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BorrowerServiceImpl.class);

    // Repository for managing borrower data
    private final BorrowerRepository borrowerRepository;

    /**
     * Constructor for BorrowerServiceImpl.
     *
     * @param borrowerRepository The repository providing data access operations for borrowers.
     */
    public BorrowerServiceImpl(final BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    /**
     * Retrieves the details of a borrower by ID.
     *
     * @param borrowerId the ID of the borrower.
     * @return the details of the borrower.
     * @throws ResourceNotFoundException if the borrower with the specified ID is not found.
     */
    @Override
    public BorrowerDTO getBorrowerById(final long borrowerId) {
        LOGGER.info("Fetching borrower with ID: {}", borrowerId);

        final Borrower borrower = borrowerRepository.findById(borrowerId).orElseThrow(()
                -> new ResourceNotFoundException(AppConstant.BORROWER, AppConstant.RECORD_ID, borrowerId));

        return toBorrowerDto(borrower);
    }

    /**
     * Retrieves all borrowers with pagination and sorting.
     *
     * @param pageNo the page number to retrieve.
     * @param pageSize the number of borrowers per page.
     * @param sortBy the field to sort by.
     * @param sortDir the direction to sort by (ASC/DESC).
     * @return a paginated response containing the list of borrowers.
     */
    @Override
    public BorrowerPageResponseDTO getAllBorrower(final int pageNo, final int pageSize,
                                                  final String sortBy, final String sortDir) {
        LOGGER.info("Fetching all borrowers - PageNo: {}, PageSize: {}, SortBy: {}, SortDir: {}",
                pageNo, pageSize, sortBy, sortDir);

        final Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        final Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        final Page<Borrower> pageBorrower = borrowerRepository.findAll(pageable);

        final List<BorrowerDTO> contentList = pageBorrower.getContent()
                .stream()
                .map(this::toBorrowerDto)
                .toList();

        return createResponse(contentList, pageBorrower, pageNo, pageSize);
    }

    /**
     * Registers a new borrower.
     *
     * @param borrowerDto the details of the borrower to be registered.
     * @return the registered borrower details.
     * @throws InvalidBorrowerException if the email ID already exists.
     */
    @Override
    public BorrowerDTO registerBorrower(final BorrowerRequestDTO borrowerDto) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Registering new borrower with email: {}", borrowerDto.getEmail());
        }

        if (borrowerRepository.existsByEmail(borrowerDto.getEmail())) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.error("Email ID already exists: {}", borrowerDto.getEmail());
            }

            throw new InvalidBorrowerException("Email ID already exists");
        }

        final Borrower borrower = toBorrowerEntity(borrowerDto);
        borrowerRepository.save(borrower);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Borrower registered successfully with ID: {}", borrower.getId());
        }

        return toBorrowerDto(borrower);
    }

    /**
     * Converts a Borrower entity to a BorrowerDTO.
     *
     * @param borrower the Borrower entity to convert.
     * @return the converted BorrowerDTO.
     */
    public BorrowerDTO toBorrowerDto(final Borrower borrower) {
        BorrowerDTO borrowerDto = new BorrowerDTO();
        BeanUtils.copyProperties(borrower, borrowerDto);

        return borrowerDto;
    }

    /**
     * Converts a BorrowerRequestDTO to a Borrower entity.
     *
     * @param borrowerDto the BorrowerRequestDTO to convert.
     * @return the converted Borrower entity.
     */
    public Borrower toBorrowerEntity(final BorrowerRequestDTO borrowerDto) {
        Borrower borrower = new Borrower();
        BeanUtils.copyProperties(borrowerDto, borrower);

        return borrower;
    }

    /**
     * Creates a BorrowerPageResponseDTO from a list of BorrowerDTOs and a Page of Borrowers.
     *
     * @param contentList the list of BorrowerDTOs.
     * @param pageBorrower the Page of Borrowers.
     * @param pageNo the current page number.
     * @param pageSize the number of borrowers per page.
     * @return the created BorrowerPageResponseDTO.
     */
    private BorrowerPageResponseDTO createResponse(
            final List<BorrowerDTO> contentList, final Page<Borrower> pageBorrower,
            final int pageNo, final int pageSize) {
        return BorrowerPageResponseDTO.builder()
                .content(contentList)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(pageBorrower.getTotalElements())
                .numberOfElements(pageBorrower.getNumberOfElements())
                .totalPages(pageBorrower.getTotalPages())
                .last(pageBorrower.isLast())
                .build();
    }
}
