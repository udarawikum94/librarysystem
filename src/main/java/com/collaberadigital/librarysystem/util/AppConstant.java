package com.collaberadigital.librarysystem.util;

/**
 * Constants used in the library management system.
 */
public final class AppConstant {

    /**
     * Default values for pagination and sorting.
     */
    public static final String DEFAULT_PAGE_NO = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";

    /**
     * Entity names used for exception handling.
     */
    public static final String BOOK = "Library Book";
    public static final String BORROWER = "Borrower";
    public static final String BORROWING = "Borrowing";

    /**
     * Identifier used for exception messages.
     */
    public static final String RECORD_ID = "id";

    private AppConstant() {
        // To prevent instantiation
        throw new AssertionError("Utility class should not be instantiated.");
    }
}
