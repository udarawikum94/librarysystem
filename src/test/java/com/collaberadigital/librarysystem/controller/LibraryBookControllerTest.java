package com.collaberadigital.librarysystem.controller;

import com.collaberadigital.librarysystem.dto.LibraryBookDTO;
import com.collaberadigital.librarysystem.dto.LibraryBookPageResponseDTO;
import com.collaberadigital.librarysystem.dto.LibraryBookRequestDTO;
import com.collaberadigital.librarysystem.factory.TestDataFactory;
import com.collaberadigital.librarysystem.service.LibraryBookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link LibraryBookController}.
 */
@WebMvcTest(LibraryBookController.class)
class LibraryBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryBookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test case for validating book creation with validation errors.
     * @throws Exception if there's an error during the test execution.
     */
    @Test
    void testCreateBook_ValidationErrors() throws Exception {
        String requestBody = createEmptyRequestBody();

        mockMvc.perform(post("/api/v1/book/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(createValidationResponseBody()));
    }

    /**
     * Test case for successful book creation.
     * @throws Exception if there's an error during the test execution.
     */
    @Test
    void testCreateBook_Success() throws Exception {
        LibraryBookRequestDTO validBookRequest = TestDataFactory.createBookSuccessRequest();
        LibraryBookDTO bookResponse = TestDataFactory.createBookSuccessResponse();

        when(bookService.registerBook(any(LibraryBookRequestDTO.class))).thenReturn(bookResponse);

        mockMvc.perform(post("/api/v1/book/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(bookResponse)));
    }

    /**
     * Test case for retrieving book by ID successfully.
     * @throws Exception if there's an error during the test execution.
     */
    @Test
    void testGetBookById_Success() throws Exception {
        LibraryBookDTO bookResponse = TestDataFactory.createBookSuccessResponse();

        when(bookService.getBookById(anyLong())).thenReturn(bookResponse);

        mockMvc.perform(get("/api/v1/book/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookId").value(1L))
                .andExpect(jsonPath("$.isbn").value("0-061-96436-0"))
                .andExpect(jsonPath("$.title").value("Secret seven adventures"))
                .andExpect(jsonPath("$.author").value("Enid Bliton"))
                .andExpect(jsonPath("$.borrowed").value(false));
    }

    /**
     * Test case for retrieving all available books for borrowing successfully.
     * @throws Exception if there's an error during the test execution.
     */
    @Test
    void testGetAllAvailableToBorrow_Success() throws Exception {
        LibraryBookPageResponseDTO response = LibraryBookPageResponseDTO.builder().build();

        when(bookService.getAllAvailableBorrow(
                anyInt(), anyInt(), anyString(), anyString())).thenReturn(response);

        mockMvc.perform(get("/api/v1/book/getAvailable")
                        .param("pageNo", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "title")
                        .param("sortDir", "asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Creates an empty request body for testing.
     * @return the empty request body as a JSON string.
     * @throws JSONException if there's an error creating the JSON object.
     */
    private String createEmptyRequestBody() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("isbn", "");
        object.put("title", "");
        object.put("author", "");

        return object.toString();
    }

    /**
     * Creates a validation response body for testing.
     * @return the validation response body as a JSON string.
     * @throws JSONException if there's an error creating the JSON object.
     */
    private String createValidationResponseBody() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("isbn", "Isbn is required");
        object.put("title", "Title is required");
        object.put("author", "Author is required");

        return object.toString();
    }

}
