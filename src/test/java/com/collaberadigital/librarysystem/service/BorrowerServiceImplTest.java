package com.collaberadigital.librarysystem.service;

import com.collaberadigital.librarysystem.dto.BorrowerDTO;
import com.collaberadigital.librarysystem.dto.BorrowerPageResponseDTO;
import com.collaberadigital.librarysystem.dto.BorrowerRequestDTO;
import com.collaberadigital.librarysystem.exception.InvalidBorrowerException;
import com.collaberadigital.librarysystem.exception.ResourceNotFoundException;
import com.collaberadigital.librarysystem.factory.TestDataFactory;
import com.collaberadigital.librarysystem.model.Borrower;
import com.collaberadigital.librarysystem.repository.BorrowerRepository;
import com.collaberadigital.librarysystem.service.impl.BorrowerServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link BorrowerServiceImpl} class.
 */
class BorrowerServiceImplTest {

    @Mock
    private BorrowerRepository borrowerRepository;

    @InjectMocks
    private BorrowerServiceImpl borrowerService;

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
     * Test case for registering a borrower successfully.
     */
    @Test
    void testRegisterBorrower_Success() {
        BorrowerRequestDTO requestDTO = TestDataFactory.createBorrowerSuccessRequest();
        Borrower borrower = TestDataFactory.createBorrowerEntity(requestDTO);

        when(borrowerRepository.existsByEmail(anyString())).thenReturn(false);
        when(borrowerRepository.save(any(Borrower.class))).thenReturn(borrower);

        BorrowerDTO responseDTO = borrowerService.registerBorrower(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(requestDTO.getName(), responseDTO.getName());
        assertEquals(requestDTO.getEmail(), responseDTO.getEmail());
        verify(borrowerRepository, times(1)).save(any(Borrower.class));
    }

    /**
     * Test case for attempting to register a borrower with an existing email,
     * expecting an InvalidBorrowerException.
     */
    @Test
    void testRegisterBorrower_InvalidBorrowerException() {
        BorrowerRequestDTO requestDTO = TestDataFactory.createBorrowerSuccessRequest();

        when(borrowerRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(InvalidBorrowerException.class,
                () -> borrowerService.registerBorrower(requestDTO));
    }

    /**
     * Test case for retrieving a borrower by their ID successfully.
     */
    @Test
    void testGetBorrowerById_Success() {
        Long borrowerId = 1L;
        Borrower borrower = TestDataFactory
                .createBorrowerEntity(TestDataFactory.createBorrowerSuccessRequest());

        when(borrowerRepository.findById(
                anyLong())).thenReturn(Optional.of(borrower));

        BorrowerDTO responseDTO = borrowerService.getBorrowerById(borrowerId);

        assertNotNull(responseDTO);
        assertEquals(borrower.getId(), responseDTO.getId());
        assertEquals(borrower.getName(), responseDTO.getName());
        assertEquals(borrower.getEmail(), responseDTO.getEmail());
    }

    /**
     * Test case for attempting to retrieve a borrower by their ID but they don't exist,
     * expecting a ResourceNotFoundException.
     */
    @Test
    void testGetBorrowerById_ResourceNotFoundException() {
        Long borrowerId = 1L;
        when(borrowerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> borrowerService.getBorrowerById(borrowerId));
    }

    /**
     * Test case for retrieving all borrowers successfully.
     */
    @Test
    void testGetAllBorrower_Success() {
        List<Borrower> borrowers = Collections.singletonList(new Borrower());
        Page<Borrower> pageBorrowers = new PageImpl<>(borrowers);

        when(borrowerRepository.findAll(
                any(PageRequest.class))).thenReturn(pageBorrowers);

        BorrowerPageResponseDTO response = borrowerService
                .getAllBorrower(pageNo, pageSize, sortBy, sortDir);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals(pageBorrowers.getTotalElements(), response.getTotalElements());
        assertEquals(pageBorrowers.getTotalPages(), response.getTotalPages());
        verify(borrowerRepository, times(1)).findAll(any(PageRequest.class));
    }
}

