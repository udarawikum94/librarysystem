package com.collaberadigital.librarysystem.service.impl;

import com.collaberadigital.librarysystem.dto.LibraryBookDTO;
import com.collaberadigital.librarysystem.dto.LibraryBookPageResponseDTO;
import com.collaberadigital.librarysystem.dto.LibraryBookRequestDTO;
import com.collaberadigital.librarysystem.exception.InvalidBookException;
import com.collaberadigital.librarysystem.exception.ResourceNotFoundException;
import com.collaberadigital.librarysystem.model.LibraryBook;
import com.collaberadigital.librarysystem.repository.LibraryBookRepository;
import com.collaberadigital.librarysystem.service.LibraryBookService;
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
 * Implementation of the LibraryBookService interface for managing book operations.
 */
@Service
public class LibraryBookServiceImpl implements LibraryBookService {

    /**
     * Logger instance for logging messages.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryBookServiceImpl.class);

    /**
     * Repository for managing library book data.
     */
    private final LibraryBookRepository bookRepository;

    /**
     * Constructor for LibraryBookServiceImpl.
     *
     * @param bookRepository      Repository providing data access operations for library books.
     */
    public LibraryBookServiceImpl(final LibraryBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Registers a new book in the library.
     *
     * @param bookDTO the details of the book to be registered.
     * @return the registered book details.
     */
    @Override
    public LibraryBookDTO registerBook(final LibraryBookRequestDTO bookDTO) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Registering a new book with ISBN: {}", bookDTO.getIsbn());
        }

        validateIsbn(bookDTO);

        LibraryBook book = mapToBook(bookDTO);
        book.setBorrowed(Boolean.FALSE);

        bookRepository.save(book);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Library Book registered successfully with ISBN: {}", bookDTO.getIsbn());
        }

        return mapToBookDto(book);
    }

    /**
     * Fetches the details of a book by its ID.
     *
     * @param bookId the ID of the book.
     * @return the details of the book.
     */
    @Override
    public LibraryBookDTO getBookById(final long bookId) {
        LOGGER.info("Fetching book details for ID: {}", bookId);

        final LibraryBook book = bookRepository.findById(bookId).orElseThrow(() -> {
            LOGGER.error("Library Book not found with ID: {}", bookId);
            return new ResourceNotFoundException(AppConstant.BOOK, AppConstant.RECORD_ID, bookId);
        });

        LOGGER.info("Library Book details fetched successfully for ID: {}", bookId);
        return mapToBookDto(book);
    }

    /**
     * Fetches all books with pagination and sorting.
     *
     * @param pageNo the page number to retrieve.
     * @param pageSize the number of books per page.
     * @param sortBy the field to sort by.
     * @param sortDir the direction to sort by (ASC/DESC).
     * @return a paginated response containing the list of books.
     */
    @Override
    public LibraryBookPageResponseDTO getAllBooks(
            final int pageNo, final int pageSize,
            final String sortBy, final String sortDir) {
        LOGGER.info("Fetching all books with pagination (pageNo: {}, pageSize: {})", pageNo, pageSize);

        final Sort sort = getSortDirection(sortBy, sortDir);

        final Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        final Page<LibraryBook> pageBooks = bookRepository.findAll(pageable);

        final List<LibraryBookDTO> contentList = pageBooks.getContent()
                .stream()
                .map(this::mapToBookDto)
                .toList();

        LOGGER.info("All books fetched successfully with pagination (pageNo: {}, pageSize: {})", pageNo, pageSize);
        return createResponse(contentList, pageBooks, pageNo, pageSize);
    }

    /**
     * Fetches all available books for borrowing with pagination and sorting.
     *
     * @param pageNo the page number to retrieve.
     * @param pageSize the number of books per page.
     * @param sortBy the field to sort by.
     * @param sortDir the direction to sort by (ASC/DESC).
     * @return a paginated response containing the list of available books for borrowing.
     */
    @Override
    public LibraryBookPageResponseDTO getAllAvailableBorrow(
            final int pageNo, final int pageSize,
            final String sortBy, final String sortDir) {
        LOGGER.info("Fetching all available books for borrow with pagination (pageNo: {}, pageSize: {})",
                pageNo, pageSize);

        final Sort sort = getSortDirection(sortBy, sortDir);

        final Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        final Page<LibraryBook> pageBooks = bookRepository.findByBorrowed(Boolean.FALSE, pageable);

        final List<LibraryBookDTO> contentList = pageBooks.getContent().stream()
                .map(this::mapToBookDto)
                .toList();

        LOGGER.info("All available books for borrow fetched successfully with pagination (pageNo: {}, pageSize: {})",
                pageNo, pageSize);
        return createResponse(contentList, pageBooks, pageNo, pageSize);
    }

    /**
     * Converts a LibraryBook entity to a LibraryBookDTO.
     *
     * @param book the LibraryBook entity to convert.
     * @return the converted LibraryBookDTO.
     */
    private LibraryBookDTO mapToBookDto(final LibraryBook book) {
        LibraryBookDTO bookDto = new LibraryBookDTO();
        BeanUtils.copyProperties(book, bookDto);

        return bookDto;
    }

    /**
     * Converts a LibraryBookRequestDTO to a LibraryBook entity.
     *
     * @param bookDto the LibraryBookRequestDTO to convert.
     * @return the converted LibraryBook entity.
     */
    private LibraryBook mapToBook(final LibraryBookRequestDTO bookDto) {
        LibraryBook book = new LibraryBook();
        BeanUtils.copyProperties(bookDto, book);

        return book;
    }

    /**
     * Validates the ISBN of a book to ensure it has the same title and author if it already exists.
     *
     * @param bookDTO the LibraryBookRequestDTO containing the book details.
     * @throws InvalidBookException if the ISBN exists but the title and author do not match.
     */
    private void validateIsbn(final LibraryBookRequestDTO bookDTO) {
        final List<LibraryBook> existingBooks = bookRepository.findByIsbn(bookDTO.getIsbn());

        //Explore existing books by isbn and check title and author
        if (!existingBooks.isEmpty()) {
            for (LibraryBook existingBook : existingBooks) {
                if (!existingBook.getTitle().equals(bookDTO.getTitle()) ||
                        !existingBook.getAuthor().equals(bookDTO.getAuthor())) {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error("ISBN number must have the same title and author for ISBN: {}", bookDTO.getIsbn());
                    }

                    throw new InvalidBookException("ISBN number must have the same title and author.");
                }
            }
        }
    }

    /**
     * Creates a LibraryBookPageResponseDTO from a list of BookDTOs and a Page of Books.
     *
     * @param contentList the list of BookDTOs.
     * @param pageBooks the Page of Books.
     * @param pageNo the current page number.
     * @param pageSize the number of books per page.
     * @return the created LibraryBookPageResponseDTO.
     */
    private LibraryBookPageResponseDTO createResponse(
            final List<LibraryBookDTO> contentList, final Page<LibraryBook> pageBooks,
            final int pageNo, final int pageSize) {

        return LibraryBookPageResponseDTO.builder()
                .content(contentList)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(pageBooks.getTotalElements())
                .numberOfElements(pageBooks.getNumberOfElements())
                .totalPages(pageBooks.getTotalPages())
                .last(pageBooks.isLast())
                .build();
    }

    /**
     * Determines the sort direction for a pageable request.
     *
     * @param sortBy the field to sort by.
     * @param sortDir the direction to sort by (ASC/DESC).
     * @return the Sort object representing the sorting direction.
     */
    private Sort getSortDirection(final String sortBy, final String sortDir) {
        return sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    }
}
