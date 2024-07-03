package com.collaberadigital.librarysystem.service;

import com.collaberadigital.librarysystem.dto.LibraryBookDTO;
import com.collaberadigital.librarysystem.dto.LibraryBookPageResponseDTO;
import com.collaberadigital.librarysystem.dto.LibraryBookRequestDTO;
import com.collaberadigital.librarysystem.exception.InvalidBookException;
import com.collaberadigital.librarysystem.exception.ResourceNotFoundException;
import com.collaberadigital.librarysystem.factory.TestDataFactory;
import com.collaberadigital.librarysystem.model.LibraryBook;
import com.collaberadigital.librarysystem.repository.LibraryBookRepository;
import com.collaberadigital.librarysystem.service.impl.LibraryBookServiceImpl;
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
 * Unit tests for the {@link LibraryBookServiceImpl} class.
 */
class LibraryBookServiceImplTest {

    @Mock
    private LibraryBookRepository bookRepository;

    @InjectMocks
    private LibraryBookServiceImpl bookService;

    /**
     * Set up method to initialize Mockito mocks.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    final static int pageNo = 0;
    final static int pageSize = 10;
    final static String sortBy = "title";
    final static String sortDir = "asc";

    /**
     * Test case for registering a book successfully.
     */
    @Test
    void testRegisterBook_Success() {
        LibraryBookRequestDTO requestDTO = TestDataFactory.createBookSuccessRequest();
        LibraryBook book = TestDataFactory.createBookEntity(requestDTO);

        when(bookRepository.findByIsbn(anyString())).thenReturn(Collections.emptyList());
        when(bookRepository.save(any(LibraryBook.class))).thenReturn(book);

        LibraryBookDTO responseDTO = bookService.registerBook(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(requestDTO.getIsbn(), responseDTO.getIsbn());
        assertEquals(requestDTO.getTitle(), responseDTO.getTitle());
        assertEquals(requestDTO.getAuthor(), responseDTO.getAuthor());
        verify(bookRepository, times(1)).save(any(LibraryBook.class));
    }

    /**
     * Test case for registering a book when the ISBN already exists, expecting an InvalidBookException.
     */
    @Test
    void testRegisterBook_InvalidBookException() {
        LibraryBookRequestDTO requestDTO = TestDataFactory.createBookSuccessRequest();
        LibraryBook existingBook = TestDataFactory.createBookEntity(requestDTO);
        existingBook.setAuthor("Different Author");

        when(bookRepository.findByIsbn(anyString())).thenReturn(Collections.singletonList(existingBook));

        assertThrows(InvalidBookException.class, () -> bookService.registerBook(requestDTO));
    }

    /**
     * Test case for retrieving a book by its ID successfully.
     */
    @Test
    void testGetBookById_Success() {
        Long bookId = 1L;
        LibraryBook book = TestDataFactory.createBookEntity(
                TestDataFactory.createBookSuccessRequest());

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        LibraryBookDTO responseDTO = bookService.getBookById(bookId);

        assertNotNull(responseDTO);
        assertEquals(book.getId(), responseDTO.getId());
        assertEquals(book.getIsbn(), responseDTO.getIsbn());
        assertEquals(book.getTitle(), responseDTO.getTitle());
        assertEquals(book.getAuthor(), responseDTO.getAuthor());
    }

    /**
     * Test case for attempting to retrieve a book by its ID but it doesn't exist,
     * expecting a ResourceNotFoundException.
     */
    @Test
    void testGetBookById_ResourceNotFoundException() {
        Long bookId = 1L;
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(bookId));
    }

    /**
     * Test case for retrieving all books successfully.
     */
    @Test
    void testGetAllBooks_Success() {
        List<LibraryBook> books = Collections.singletonList(new LibraryBook());
        Page<LibraryBook> pageBooks = new PageImpl<>(books);

        when(bookRepository.findAll(any(PageRequest.class))).thenReturn(pageBooks);

        LibraryBookPageResponseDTO response = bookService.getAllBooks(pageNo, pageSize, sortBy, sortDir);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals(pageBooks.getTotalElements(), response.getTotalElements());
        assertEquals(pageBooks.getTotalPages(), response.getTotalPages());
        verify(bookRepository, times(1)).findAll(any(PageRequest.class));
    }

    /**
     * Test case for retrieving all available books for borrowing successfully.
     */
    @Test
    void testGetAllAvailableBorrow_Success() {
        List<LibraryBook> books = Collections.singletonList(new LibraryBook());
        Page<LibraryBook> pageBooks = new PageImpl<>(books);

        when(bookRepository.findByBorrowed(anyBoolean(), any(PageRequest.class))).thenReturn(pageBooks);

        LibraryBookPageResponseDTO response = bookService.getAllAvailableBorrow(pageNo, pageSize, sortBy, sortDir);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals(pageBooks.getTotalElements(), response.getTotalElements());
        assertEquals(pageBooks.getTotalPages(), response.getTotalPages());
        verify(bookRepository, times(1))
                .findByBorrowed(anyBoolean(), any(PageRequest.class));
    }
}
