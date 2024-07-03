package com.collaberadigital.librarysystem.service;

import com.collaberadigital.librarysystem.dto.LibraryBookRequestDTO;
import com.collaberadigital.librarysystem.dto.BorrowerRequestDTO;
import com.collaberadigital.librarysystem.dto.BorrowingInfoDTO;
import com.collaberadigital.librarysystem.dto.BorrowingPageResponseDTO;
import com.collaberadigital.librarysystem.exception.CommonSystemException;
import com.collaberadigital.librarysystem.exception.ResourceNotFoundException;
import com.collaberadigital.librarysystem.factory.TestDataFactory;
import com.collaberadigital.librarysystem.model.LibraryBook;
import com.collaberadigital.librarysystem.model.Borrower;
import com.collaberadigital.librarysystem.model.Borrowing;
import com.collaberadigital.librarysystem.repository.LibraryBookRepository;
import com.collaberadigital.librarysystem.repository.BorrowerRepository;
import com.collaberadigital.librarysystem.repository.BorrowingRepository;
import com.collaberadigital.librarysystem.service.impl.BorrowingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link BorrowingServiceImpl} class.
 */
class BorrowingServiceImplTest {

    @Mock
    private BorrowingRepository borrowingRepository;

    @Mock
    private LibraryBookRepository bookRepository;

    @Mock
    private BorrowerRepository borrowerRepository;

    @InjectMocks
    private BorrowingServiceImpl borrowingService;


    static Borrower borrower;
    static LibraryBook book;

    /**
     * Set up method to initialize Mockito mocks and create test data.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        BorrowerRequestDTO borrowerRequestDTO = TestDataFactory.createBorrowerSuccessRequest();
        borrower = TestDataFactory.createBorrowerEntity(borrowerRequestDTO);

        LibraryBookRequestDTO bookRequestDTO = TestDataFactory.createBookSuccessRequest();
        book = TestDataFactory.createBookEntity(bookRequestDTO);
    }

    static final int pageNo = 0;
    static final int pageSize = 10;
    static final String sortBy = "title";
    static final String sortDir = "asc";
    static final Long borrowerId = 1L;
    static final Long bookId = 1L;
    static final Long borrowingId = 1L;

    /**
     * Test case for successfully borrowing a book.
     */
    @Test
    void testBorrowBook_Success() {
        when(borrowerRepository.findById(borrower.getId())).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(borrowingRepository.existsByBookIdAndReturnDateIsNull(book.getId())).thenReturn(false);

        Borrowing borrowing = TestDataFactory.createBorrowingEntity(borrower, book);

        when(borrowingRepository.save(any(Borrowing.class))).thenReturn(borrowing);

        BorrowingInfoDTO response = borrowingService.borrowBook(book.getId(), borrower.getId());

        assertNotNull(response);
        assertEquals(book.getId(), response.getBookInfo().getId());
        assertEquals(borrower.getId(), response.getBorrower().getId());
        verify(borrowingRepository, times(1)).save(any(Borrowing.class));
    }

    /**
     * Test case for attempting to borrow a book that is already borrowed.
     */
    @Test
    void testBorrowBook_AlreadyBorrowed() {
        when(borrowerRepository.findById(borrowerId))
                .thenReturn(Optional.of(new Borrower()));
        when(bookRepository.findById(bookId))
                .thenReturn(Optional.of(new LibraryBook()));
        when(borrowingRepository.existsByBookIdAndReturnDateIsNull(bookId))
                .thenReturn(true);

        assertThrows(CommonSystemException.class,
                () -> borrowingService.borrowBook(bookId, borrowerId));
    }

    /**
     * Test case for attempting to borrow a book with a borrower ID that doesn't exist.
     */
    @Test
    void testBorrowBook_BorrowerNotFound() {
        when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> borrowingService.borrowBook(bookId, borrowerId));
    }

    /**
     * Test case for attempting to borrow a book with a book ID that doesn't exist.
     */
    @Test
    void testBorrowBook_BookNotFound() {
        when(borrowerRepository.findById(borrowerId))
                .thenReturn(Optional.of(new Borrower()));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> borrowingService.borrowBook(bookId, borrowerId));
    }

    /**
     * Test case for successfully returning a borrowed book.
     */
    @Test
    void testReturnBook_Success() {
        Borrowing borrowing = TestDataFactory.createBorrowingEntity(borrower, book);
        //borrowing.setReturnDate(LocalDateTime.now());

        when(borrowerRepository.findById(borrower.getId())).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(borrowingRepository.existsByBookIdAndReturnDateIsNull(book.getId())).thenReturn(false);
        when(borrowingRepository.findById(1L)).thenReturn(Optional.of(borrowing));
        when(borrowingRepository.save(any(Borrowing.class))).thenReturn(borrowing);

        BorrowingInfoDTO response = borrowingService.returnBook(borrowingId);

        assertNotNull(response);
        assertNotNull(response.getReturnDate());
        verify(borrowingRepository, times(1)).save(any(Borrowing.class));
        verify(bookRepository, times(1)).save(any(LibraryBook.class));
    }

    /**
     * Test case for attempting to return a book that was not found in the borrowing records.
     */
    @Test
    void testReturnBook_BorrowingNotFound() {
        Long borrowingId = 1L;

        when(borrowingRepository.findById(borrowingId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> borrowingService.returnBook(borrowingId));
    }

    /**
     * Test case for retrieving borrowing information for a borrower and book successfully.
     */
    @Test
    void testGetBorrowingInfoByBorrowerAndBook_Success() {

        Borrowing borrowing = TestDataFactory.createBorrowingEntity(borrower, book);

        when(borrowingRepository.findTopByBorrowerIdAndBookId(borrowerId, bookId))
                .thenReturn(Optional.of(borrowing));

        BorrowingInfoDTO response = borrowingService
                .getBorrowingInfoByBorrowerAndBook(borrowerId, bookId);

        assertNotNull(response);
        assertEquals(borrowing.getId(), response.getId());
    }

    /**
     * Test case for attempting to retrieve borrowing information for a borrower and book,
     * but not finding any records.
     */
    @Test
    void testGetBorrowingInfoByBorrowerAndBook_NotFound() {
        Long borrowerId = 1L;
        Long bookId = 1L;

        when(borrowingRepository.findTopByBorrowerIdAndBookId(borrowerId, bookId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> borrowingService.getBorrowingInfoByBorrowerAndBook(borrowerId, bookId));
    }

    /**
     * Test case for retrieving borrowing information for a borrower successfully.
     */
    @Test
    void testGetBorrowingInfoByBorrower_Success() {
        Long borrowerId = 1L;
        Borrowing borrowing = TestDataFactory.createBorrowingEntity(borrower, book);

        List<Borrowing> borrowings = Collections.singletonList(borrowing);
        Page<Borrowing> pageBorrowings = new PageImpl<>(borrowings);

        when(borrowingRepository.findByBorrowerId(anyLong(), any(PageRequest.class)))
                .thenReturn(pageBorrowings);

        BorrowingPageResponseDTO response = borrowingService.getBorrowingInfoByBorrower(
                borrowerId, pageNo, pageSize, sortBy, sortDir);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        verify(borrowingRepository, times(1))
                .findByBorrowerId(anyLong(), any(PageRequest.class));
    }
}
