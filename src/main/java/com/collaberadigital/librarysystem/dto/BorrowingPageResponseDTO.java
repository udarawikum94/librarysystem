package com.collaberadigital.librarysystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing information about a borrowing transaction.
 * This class encapsulates the details of a book borrowing transaction.
 */
@Data
@AllArgsConstructor
@Builder
public class BorrowingPageResponseDTO {
    private List<BorrowingInfoDTO> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private long numberOfElements;
    private int totalPages;
    private boolean last;
}
