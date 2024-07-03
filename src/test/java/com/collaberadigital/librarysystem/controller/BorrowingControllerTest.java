package com.collaberadigital.librarysystem.controller;

import com.collaberadigital.librarysystem.dto.BorrowingInfoDTO;
import com.collaberadigital.librarysystem.dto.BorrowingPageResponseDTO;
import com.collaberadigital.librarysystem.service.BorrowingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This class contains unit tests for the BorrowingController class.
 */
@WebMvcTest(BorrowingController.class)
class BorrowingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingService borrowingService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test case for borrowing a book successfully.
     * @throws Exception if there's an error during the test execution.
     */
    @Test
    void testBorrowBook_Success() throws Exception {
        BorrowingInfoDTO borrowingInfo = BorrowingInfoDTO.builder()
                .id(1L)
                .isBorrowed(Boolean.TRUE)
                .build();

        when(borrowingService.borrowBook(anyLong(), anyLong())).thenReturn(borrowingInfo);

        mockMvc.perform(post("/api/v1/borrowing/1/borrow")
                        .param("borrowerId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.borrowingId").value("1"))
                .andExpect(jsonPath("$.borrowed").value("true"));
    }

    /**
     * Test case for returning a book successfully.
     * @throws Exception if there's an error during the test execution.
     */
    @Test
    void testReturnBook_Success() throws Exception {
        LocalDateTime returnDate = LocalDateTime.now();
        BorrowingInfoDTO borrowingInfo = BorrowingInfoDTO.builder()
                .id(1L)
                .returnDate(returnDate)
                .isBorrowed(Boolean.FALSE)
                .build();

        when(borrowingService.returnBook(anyLong())).thenReturn(borrowingInfo);

        mockMvc.perform(put("/api/v1/borrowing/1/return")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.borrowed").value("false"));
    }

    /**
     * Test case for getting borrowing info by borrower and book successfully.
     * @throws Exception if there's an error during the test execution.
     */
    @Test
    void testGetBorrowingInfoByBorrowerAndBook_Success() throws Exception {
        BorrowingInfoDTO borrowingInfo = BorrowingInfoDTO.builder().build();

        when(borrowingService.getBorrowingInfoByBorrowerAndBook(
                anyLong(), anyLong())).thenReturn(borrowingInfo);

        mockMvc.perform(get("/api/v1/borrowing/getBorrowingInfo/1/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Test case for getting all books by borrower successfully.
     * @throws Exception if there's an error during the test execution.
     */
    @Test
    void testGetAllBooksByBorrower_Success() throws Exception {
        BorrowingPageResponseDTO response = BorrowingPageResponseDTO.builder().build();

        when(borrowingService.getBorrowingInfoByBorrower(
                anyLong(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(response);

        mockMvc.perform(get("/api/v1/borrowing/1")
                        .param("pageNo", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "title")
                        .param("sortDir", "asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
