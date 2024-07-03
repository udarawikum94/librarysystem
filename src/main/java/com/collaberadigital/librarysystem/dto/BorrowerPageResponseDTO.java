package com.collaberadigital.librarysystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a page of borrower information.
 * This class encapsulates a list of borrower DTOs along with pagination details.
 */
@Data
@AllArgsConstructor
@Builder
public class BorrowerPageResponseDTO {
    private List<BorrowerDTO> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private long numberOfElements;
    private int totalPages;
    private boolean last;
}
