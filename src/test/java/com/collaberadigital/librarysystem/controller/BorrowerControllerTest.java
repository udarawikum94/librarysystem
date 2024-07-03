package com.collaberadigital.librarysystem.controller;

import com.collaberadigital.librarysystem.dto.BorrowerDTO;
import com.collaberadigital.librarysystem.dto.BorrowerPageResponseDTO;
import com.collaberadigital.librarysystem.dto.BorrowerRequestDTO;
import com.collaberadigital.librarysystem.factory.TestDataFactory;
import com.collaberadigital.librarysystem.service.BorrowerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This class contains unit tests for the BorrowerController class.
 */
@WebMvcTest(BorrowerController.class)
class BorrowerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowerService borrowerService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test case for registering a borrower successfully.
     * @throws Exception if there's an error during the test execution.
     */
    @Test
    void testRegisterBorrower_Success() throws Exception {
        BorrowerRequestDTO borrowerRequest = TestDataFactory.createBorrowerSuccessRequest();
        BorrowerDTO borrowerResponse = TestDataFactory.createBorrowerSuccessResponse();

        when(borrowerService.registerBorrower(
                any(BorrowerRequestDTO.class))).thenReturn(borrowerResponse);

        mockMvc.perform(post("/api/v1/borrower/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(borrowerRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.borrowerId").value(1L))
                .andExpect(jsonPath("$.name").value("Udara Wikum"))
                .andExpect(jsonPath("$.email").value("udarawikum@gmail.com"));
    }

    /**
     * Test case for registering a borrower with invalid input, expecting a bad request.
     * @throws Exception if there's an error during the test execution.
     */
    @Test
    void testRegisterBorrower_BadRequest() throws Exception {
        BorrowerRequestDTO borrowerRequest = new BorrowerRequestDTO("", "invalid-email");

        mockMvc.perform(post("/api/v1/borrower/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(borrowerRequest)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test case for retrieving a borrower by ID successfully.
     * @throws Exception if there's an error during the test execution.
     */
    @Test
    void testGetBorrowerById_Success() throws Exception {
        BorrowerDTO borrowerResponse = TestDataFactory.createBorrowerSuccessResponse();;

        when(borrowerService.getBorrowerById(anyLong())).thenReturn(borrowerResponse);

        mockMvc.perform(get("/api/v1/borrower/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.borrowerId").value(1L))
                .andExpect(jsonPath("$.name").value("Udara Wikum"))
                .andExpect(jsonPath("$.email").value("udarawikum@gmail.com"));
    }

    /**
     * Test case for retrieving all borrowers successfully.
     * @throws Exception if there's an error during the test execution.
     */
    @Test
    void testGetAllBorrowers_Success() throws Exception {
        BorrowerPageResponseDTO response = BorrowerPageResponseDTO.builder().build();

        when(borrowerService.getAllBorrower(
                anyInt(), anyInt(), anyString(), anyString())).thenReturn(response);

        mockMvc.perform(get("/api/v1/borrower")
                        .param("pageNo", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "name")
                        .param("sortDir", "asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
