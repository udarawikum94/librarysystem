package com.collaberadigital.librarysystem.factory;

import com.collaberadigital.librarysystem.dto.LibraryBookDTO;
import com.collaberadigital.librarysystem.dto.LibraryBookRequestDTO;
import com.collaberadigital.librarysystem.dto.BorrowerDTO;
import com.collaberadigital.librarysystem.dto.BorrowerRequestDTO;
import com.collaberadigital.librarysystem.model.LibraryBook;
import com.collaberadigital.librarysystem.model.Borrower;
import com.collaberadigital.librarysystem.model.Borrowing;

import java.time.LocalDateTime;

/**
 * Utility class providing factory methods to create test data objects for books and borrowers,
 * both request and response objects, as well as entity objects.
 */
public class TestDataFactory {

    /**
     * Creates a sample LibraryBookDTO object representing a successful response.
     * @return A LibraryBookDTO object representing a successful response.
     */
    public static LibraryBookDTO createBookSuccessResponse() {
        return LibraryBookDTO.builder().isbn("0-061-96436-0")
                .author("Enid Bliton")
                .title("Secret seven adventures")
                .borrowed(Boolean.FALSE)
                .id(1L)
                .build();
    }

    /**
     * Creates a sample LibraryBookRequestDTO object representing a successful request.
     * @return A LibraryBookRequestDTO object representing a successful request.
     */
    public static LibraryBookRequestDTO createBookSuccessRequest() {
        return LibraryBookRequestDTO.builder()
                .isbn("0-061-96436-0")
                .author("Enid Bliton")
                .title("Secret seven adventures")
                .build();
    }

    /**
     * Creates a sample BorrowerRequestDTO object representing a successful request.
     * @return A BorrowerRequestDTO object representing a successful request.
     */
    public static BorrowerRequestDTO createBorrowerSuccessRequest() {
        return BorrowerRequestDTO.builder()
                .name("Udara Wikum")
                .email("udarawikum@gmail.com")
                .build();
    }

    /**
     * Creates a sample BorrowerDTO object representing a successful response.
     * @return A BorrowerDTO object representing a successful response.
     */
    public static BorrowerDTO createBorrowerSuccessResponse() {
        return BorrowerDTO.builder().id(1L)
                .name("Udara Wikum")
                .email("udarawikum@gmail.com")
                .build();
    }

    /**
     * Creates a LibraryBook entity object based on the provided LibraryBookRequestDTO.
     * @param requestDTO The LibraryBookRequestDTO object.
     * @return A LibraryBook entity object.
     */
    public static LibraryBook createBookEntity(final LibraryBookRequestDTO requestDTO) {
        return LibraryBook.builder()
                .isbn(requestDTO.getIsbn())
                .title(requestDTO.getTitle())
                .author(requestDTO.getAuthor())
                .borrowed(Boolean.FALSE)
                .id(1L)
                .build();
    }

    /**
     * Creates a Borrower entity object based on the provided BorrowerRequestDTO.
     * @param requestDTO The BorrowerRequestDTO object.
     * @return A Borrower entity object.
     */
    public static Borrower createBorrowerEntity(final BorrowerRequestDTO requestDTO) {
        return Borrower.builder()
                .id(1L)
                .name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .build();
    }

    /**
     * Creates a Borrowing entity object with the specified borrower and book.
     * @param borrower The Borrower entity.
     * @param book The LibraryBook entity.
     * @return A Borrowing entity object.
     */
    public static Borrowing createBorrowingEntity(
            final Borrower borrower, final LibraryBook book) {
        return Borrowing.builder()
                .id(1L)
                .book(book)
                .borrower(borrower)
                .borrowDate(LocalDateTime.now())
                .build();
    }
}
