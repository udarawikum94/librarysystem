package com.collaberadigital.librarysystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a generic response handler for common exceptions.
 * This class encapsulates the error message to be returned in the response body.
 */
@Data
@AllArgsConstructor
public class CommonResponseHandler {
	private String message;

}
