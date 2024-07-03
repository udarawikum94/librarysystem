package com.collaberadigital.librarysystem.service.impl;

import com.collaberadigital.librarysystem.dto.*;
import com.collaberadigital.librarysystem.exception.CommonSystemException;
import com.collaberadigital.librarysystem.exception.ResourceNotFoundException;
import com.collaberadigital.librarysystem.model.LibraryBook;
import com.collaberadigital.librarysystem.model.Borrower;
import com.collaberadigital.librarysystem.model.Borrowing;
import com.collaberadigital.librarysystem.repository.LibraryBookRepository;
import com.collaberadigital.librarysystem.repository.BorrowerRepository;
import com.collaberadigital.librarysystem.repository.BorrowingRepository;
import com.collaberadigital.librarysystem.service.BorrowingService;
import com.collaberadigital.librarysystem.util.AppConstant;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of the BorrowingService interface for managing borrowing operations.
 */
@Service
public class BorrowingServiceImpl implements BorrowingService {

    /**
     * Logger instance for logging messages.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BorrowingServiceImpl.class);

    /**
     * Repository for managing borrowing data.
     */
    private final BorrowingRepository borrowingRepository;

    /**
     * Repository for managing library book data.
     */
    private final LibraryBookRepository bookRepository;

    /**
     * Repository for managing borrower data.
     */
    private final BorrowerRepository borrowerRepository;

    /**
     * Constructor for BorrowingServiceImpl.
     *
     * @param borrowingRepository Repository providing data access operations for borrowings.
     * @param bookRepository      Repository providing data access operations for library books.
     * @param borrowerRepository  Repository providing data access operations for borrowers.
     */
    public BorrowingServiceImpl(
            final BorrowingRepository borrowingRepository,
            final LibraryBookRepository bookRepository,
            final BorrowerRepository borrowerRepository) {

        this.borrowingRepository = borrowingRepository;
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
    }

    /**
     * Borrows a book for a borrower.
     *
     * @param bookId the ID of the book to be borrowed.
     * @param borrowerId the ID of the borrower.
     * @return the borrowing information.
     */
    @Override
    @Transactional
    public BorrowingInfoDTO borrowBook(final long bookId, final long borrowerId) {
        LOGGER.info("Attempting to borrow book with ID: {} by borrower ID: {}", bookId, borrowerId);

        final Borrower borrower = getBorrower(borrowerId);
        final LibraryBook book = getAndValidateBook(bookId);

        final Borrowing borrowing = mapBorrowing(borrower, book);
        borrowingRepository.save(borrowing);

        LOGGER.info("Library Book with ID: {} borrowed successfully by borrower ID: {}", bookId, borrowerId);

        return mapBorrowingResponse(borrowing);
    }

    /**
     * Returns a borrowed book.
     *
     * @param borrowingId the ID of the borrowing record.
     * @return the borrowing information after returning the book.
     */
    @Override
    @Transactional
    public BorrowingInfoDTO returnBook(final long borrowingId) {
        LOGGER.info("Returning book for borrowing ID: {}", borrowingId);
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> {
                    LOGGER.error("Borrowing not found with ID: {}", borrowingId);
                    return new ResourceNotFoundException(
                                AppConstant.BORROWING, AppConstant.RECORD_ID, borrowingId);});

        checkReturnStatus(borrowing);
        updateBorrowStatus(borrowing.getBook().getId(), Boolean.FALSE);

        borrowing.setReturnDate(LocalDateTime.now());

        borrowingRepository.save(borrowing);
        LOGGER.info("Library Book returned successfully for borrowing ID: {}", borrowingId);

        return mapBorrowingResponse(borrowing);
    }

    /**
     * Retrieves borrowing information for a borrower and a book.
     *
     * @param borrowerId the ID of the borrower.
     * @param bookId the ID of the book.
     * @return the borrowing information.
     */
    @Override
    public BorrowingInfoDTO getBorrowingInfoByBorrowerAndBook(final long borrowerId, final long bookId) {
        LOGGER.info("Getting borrowing info for borrower ID: {} and book ID: {}", borrowerId, bookId);

        final Borrowing borrowing = borrowingRepository.findTopByBorrowerIdAndBookId(borrowerId, bookId)
                .orElseThrow(() -> {
                    LOGGER.error(
                            "Borrowing record not found for borrower ID: {} and book ID: {}", borrowerId, bookId);
                    return new ResourceNotFoundException(AppConstant.BORROWING, AppConstant.RECORD_ID, bookId);
                });

        return mapBorrowingResponse(borrowing);
    }

    /**
     * Retrieves borrowing information for a borrower with pagination.
     *
     * @param borrowerId the ID of the borrower.
     * @param pageNo the page number.
     * @param pageSize the number of records per page.
     * @param sortBy the field to sort by.
     * @param sortDir the sort direction (ASC/DESC).
     * @return a paginated response containing the borrowing information.
     */
    @Override
    public BorrowingPageResponseDTO getBorrowingInfoByBorrower(
            final long borrowerId, final int pageNo, final int pageSize,
            final String sortBy, final String sortDir) {
        LOGGER.info("Getting borrowing info for borrower ID: {} with pagination (pageNo: {}, pageSize: {})",
                borrowerId, pageNo, pageSize);

        final Sort sort = getSortDirection(sortBy, sortDir);

        final Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        final Page<Borrowing> pageBorrowing = borrowingRepository.findByBorrowerId(borrowerId, pageable);

        final List<BorrowingInfoDTO> contentList = pageBorrowing.getContent()
                .stream()
                .map(this::mapBorrowingResponse)
                .toList();

        return createResponse(contentList, pageBorrowing, pageNo, pageSize);
    }

    /**
     * Maps a Borrower and a LibraryBook to a Borrowing entity.
     *
     * @param borrower the Borrower entity.
     * @param book the LibraryBook entity.
     * @return the mapped Borrowing entity.
     */
    private Borrowing mapBorrowing(final Borrower borrower, final LibraryBook book) {
        return Borrowing.builder()
                .borrower(borrower)
                .book(book)
                .borrowDate(LocalDateTime.now())
                .build();
    }

    /**
     * Maps a Borrowing entity to a BorrowingInfoDTO.
     *
     * @param borrowing the Borrowing entity.
     * @return the BorrowingInfoDTO.
     */
    private BorrowingInfoDTO mapBorrowingResponse(final Borrowing borrowing) {
        return BorrowingInfoDTO.builder()
                .id(borrowing.getId())
                .borrower(toBorrowerDto(borrowing.getBorrower()))
                .bookInfo(toBookDto(borrowing.getBook()))
                .borrowDate(borrowing.getBorrowDate())
                .returnDate(borrowing.getReturnDate())
                .build();
    }

    /**
     * Converts a Borrower entity to a BorrowerDTO.
     *
     * @param borrower the Borrower entity.
     * @return the BorrowerDTO.
     */
    public BorrowerDTO toBorrowerDto(final Borrower borrower) {
        BorrowerDTO borrowerDto = new BorrowerDTO();
        BeanUtils.copyProperties(borrower, borrowerDto);

        return borrowerDto;
    }

    /**
     * Converts a LibraryBook entity to a LibraryBookDTO.
     *
     * @param book the LibraryBook entity.
     * @return the LibraryBookDTO.
     */
    private LibraryBookDTO toBookDto(final LibraryBook book) {
        LibraryBookDTO bookDto = new LibraryBookDTO();
        BeanUtils.copyProperties(book, bookDto);

        return bookDto;
    }

    /**
     * Creates a BorrowingPageResponseDTO from a list of BorrowingInfoDTOs and a Page of Borrowings.
     *
     * @param contentList the list of BorrowingInfoDTOs.
     * @param pageBorrowing the Page of Borrowings.
     * @param pageNo the current page number.
     * @param pageSize the number of records per page.
     * @return the BorrowingPageResponseDTO.
     */
    private BorrowingPageResponseDTO createResponse(
            final List<BorrowingInfoDTO> contentList, final Page<Borrowing> pageBorrowing,
            final int pageNo, final int pageSize) {

        return BorrowingPageResponseDTO.builder()
                .content(contentList)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(pageBorrowing.getTotalElements())
                .numberOfElements(pageBorrowing.getNumberOfElements())
                .totalPages(pageBorrowing.getTotalPages())
                .last(pageBorrowing.isLast())
                .build();
    }

    /**
     * Updates the borrow status of a library book identified by its ID.
     * Throws a ResourceNotFoundException if the book with the given ID is not found.
     *
     * @param bookId       The ID of the library book to update.
     * @param borrowStatus The new borrow status to set (true for borrowed, false for not borrowed).
     * @return The updated LibraryBook entity.
     * @throws ResourceNotFoundException If the book with the given ID is not found.
     */
    private LibraryBook updateBorrowStatus(final Long bookId, final Boolean borrowStatus) {
        LibraryBook book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    LOGGER.error("Library Book not found with ID: {}", bookId);
                    return new ResourceNotFoundException(
                            AppConstant.BOOK, AppConstant.RECORD_ID, bookId);
                });
        book.setBorrowed(borrowStatus);

        bookRepository.save(book);

        return book;
    }

    /**
     * Retrieves a borrower entity by its ID.
     * Throws a ResourceNotFoundException if the borrower with the given ID is not found.
     *
     * @param borrowerId The ID of the borrower to retrieve.
     * @return The Borrower entity.
     * @throws ResourceNotFoundException If the borrower with the given ID is not found.
     */
    private Borrower getBorrower(final Long borrowerId) {
        return borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> {
                    LOGGER.error("Borrower not found with ID: {}", borrowerId);
                    return new ResourceNotFoundException(
                            AppConstant.BORROWER, AppConstant.RECORD_ID, borrowerId);
                });
    }

    /**
     * Fetches and validates a library book by its ID, updating its borrow status to borrowed (true).
     * Throws a CommonSystemException if the book is already borrowed.
     *
     * @param bookId The ID of the library book to fetch and validate.
     * @return The validated LibraryBook entity.
     * @throws CommonSystemException If the book is already borrowed.
     */
    private LibraryBook getAndValidateBook(final Long bookId) {
        final LibraryBook book = updateBorrowStatus(bookId, Boolean.TRUE);
        if (borrowingRepository.existsByBookIdAndReturnDateIsNull(bookId)) {
            LOGGER.error("Library Book with ID: {} is already borrowed", bookId);
            throw new CommonSystemException("LibraryBook is already borrowed by someone");
        }

        return book;
    }

    /**
     * Determines the sort direction based on the provided sort direction string.
     *
     * @param sortBy  The field to sort by.
     * @param sortDir The sort direction ("ASC" for ascending, "DESC" for descending).
     * @return A Sort object representing the sorting criteria.
     */
    private Sort getSortDirection(final String sortBy, final String sortDir) {
        return sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    }

    /**
     * Checks if a borrowing entity's book is already returned.
     * Throws a CommonSystemException if the book is already returned but has a return date.
     *
     * @param borrowing The borrowing entity to check.
     * @throws CommonSystemException If the book is already returned by the borrower.
     */
    private void checkReturnStatus(final Borrowing borrowing){
        if (Boolean.FALSE.equals(borrowing.getBook().isBorrowed())
                && borrowing.getReturnDate() != null) {
            LOGGER.error(
                    "Library Book already returned by borrower for ID: {}", borrowing.getId());
            throw new CommonSystemException("LibraryBook already returned by borrower");
        }
    }
}
