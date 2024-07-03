package com.collaberadigital.librarysystem.reporitory;

import com.collaberadigital.librarysystem.model.LibraryBook;
import com.collaberadigital.librarysystem.repository.LibraryBookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link LibraryBookRepository} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class LibraryBookRepositoryTest {
    @Mock
    private LibraryBookRepository bookRepository;

    /**
     * Test case for finding books by ISBN.
     */
    @Test
    public void testFindByIsbn() {
        String isbn = "12345678910";
        List<LibraryBook> expectedBooks = getBookList();
        expectedBooks.add(
                LibraryBook.builder()
                        .isbn("12345678910")
                        .author("test auth 2")
                        .title("title 2").build());

        when(bookRepository.findByIsbn(eq(isbn))).thenReturn(expectedBooks);

        List<LibraryBook> actualBooks = bookRepository.findByIsbn(isbn);

        verify(bookRepository).findByIsbn(eq(isbn));

        assertEquals(expectedBooks, actualBooks);
    }

    /**
     * Test case for finding books by borrowed status.
     */
    @Test
    public void testFindByBorrowed() {
        Boolean borrowStatus = false;
        Pageable pageable = PageRequest.of(
                0, 10, Sort.by("title").ascending());
        Page<LibraryBook> expectedPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(bookRepository.findByBorrowed(
                eq(borrowStatus), eq(pageable))).thenReturn(expectedPage);

        Page<LibraryBook> actualPage = bookRepository.findByBorrowed(borrowStatus, pageable);

        verify(bookRepository).findByBorrowed(eq(borrowStatus), eq(pageable));

        assertEquals(expectedPage, actualPage);
    }

    /**
     * Helper method to generate a list of test books.
     *
     * @return A list of test books.
     */
    private List<LibraryBook> getBookList() {
        return IntStream.range(0, 4)
                .mapToObj(i -> LibraryBook.builder()
                        .isbn("12343443342")
                        .title("test title")
                        .author("test author")
                        .build())
                .collect(Collectors.toList());
    }
}
