package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Parameterized tests for Library")
public class ParameterizedTests {

    private Library library;

    @BeforeEach
    void setUp() {
        library = new Library();
        library.addBook(new Book("978-1", "Java Programming", "Anamul"));
        library.registerMember(new Member("STU001", "Rahman"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"000-0", "invalid", "978-2", "XXXX", "not-a-real-isbn"})
    @DisplayName("issueBook should return false for any non-existent ISBN")
    void testIssueWithVariousInvalidIsbns(String invalidIsbn) {
        assertFalse(library.issueBook(invalidIsbn, "STU001"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"STU999", "unknown", "S0", "XXXX", "not-a-real-id"})
    @DisplayName("issueBook should return false for any non-existent member ID")
    void testIssueWithVariousInvalidMemberIds(String invalidMemberId) {
        assertFalse(library.issueBook("978-1", invalidMemberId));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("issueBook should return false for null or empty ISBN when that key was never registered")
    void testIssueWithNullOrEmptyIsbn(String isbnValue) {
        assertFalse(library.issueBook(isbnValue, "STU001"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("issueBook should return false for null or empty member ID when that key was never registered")
    void testIssueWithNullOrEmptyMemberId(String memberIdValue) {
        assertFalse(library.issueBook("978-1", memberIdValue));
    }

    @ParameterizedTest
    @CsvSource({
            "978-1, STU001, true",
            "978-1, STU999, false",
            "000-0, STU001, false",
            "000-0, STU999, false"
    })
    @DisplayName("issueBook result should match expected outcome for each isbn/memberId combination")
    void testIssueBookCombinations(String isbn, String memberId, boolean expected) {
        assertEquals(expected, library.issueBook(isbn, memberId));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10, 50})
    @DisplayName("Registering N members should let all of them successfully borrow different books")
    void testMultipleMembersCanEachBorrowOneBook(int memberCount) {
        Library freshLibrary = new Library();
        for (int i = 0; i < memberCount; i++) {
            freshLibrary.addBook(new Book("ISBN-" + i, "Book " + i, "Author"));
            freshLibrary.registerMember(new Member("MEM-" + i, "Student " + i));
        }

        for (int i = 0; i < memberCount; i++) {
            assertTrue(freshLibrary.issueBook("ISBN-" + i, "MEM-" + i));
        }
    }
}
