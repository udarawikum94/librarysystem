package com.collaberadigital.librarysystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) representing a borrower.
 * This class extends the BorrowerRequestDTO to inherit its properties.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BorrowerDTO extends BorrowerRequestDTO {

    @JsonProperty("borrowerId")
    private Long id;
}
