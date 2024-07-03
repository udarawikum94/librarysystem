package com.collaberadigital.librarysystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a book entity.
 * This class extends the {@link LibraryBookRequestDTO} class.
 */
@Data
@AllArgsConstructor
@Builder
public class LibraryBookPageResponseDTO {
    private List<LibraryBookDTO> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private long numberOfElements;
    private int totalPages;
    private boolean last;
}
