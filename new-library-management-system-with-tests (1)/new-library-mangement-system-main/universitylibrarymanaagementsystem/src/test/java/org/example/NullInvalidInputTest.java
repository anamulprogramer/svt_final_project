package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Null and invalid input tests")
public class NullInvalidInputTest {

    private Library library;

    @BeforeEach
    void setUp() {
        library = new Library();
        library.addBook(new Book("978-1", "Java Programming", "Anamul"));
        library.registerMember(new Member("STU001", "Rahman"));
    }

    @Test
    @DisplayName("issueBook(null, validMemberId) should return false, not throw")
    void testIssueBookNullIsbn() {
        assertFalse(library.issueBook(null, "STU001"));
    }

    @Test
    @DisplayName("issueBook(validIsbn, null) should return false, not throw")
    void testIssueBookNullMemberId() {
        assertFalse(library.issueBook("978-1", null));
    }

    @Test
    @DisplayName("issueBook(null, null) should return false, not throw")
    void testIssueBookBothNull() {
        assertFalse(library.issueBook(null, null));
    }

    @Test
    @DisplayName("returnBook(null, validMemberId) should return false, not throw")
    void testReturnBookNullIsbn() {
        library.issueBook("978-1", "STU001");
        assertFalse(library.returnBook(null, "STU001"));
    }

    @Test
    @DisplayName("returnBook(validIsbn, null) should return false, not throw")
    void testReturnBookNullMemberId() {
        library.issueBook("978-1", "STU001");
        assertFalse(library.returnBook("978-1", null));
    }

    @Test
    @DisplayName("returnBook(null, null) should return false, not throw")
    void testReturnBookBothNull() {
        assertFalse(library.returnBook(null, null));
    }

    @Test
    @DisplayName("deleteMember(null) should return false, not throw")
    void testDeleteMemberNullId() {
        assertFalse(library.deleteMember(null));
    }

    @Test
    @DisplayName("Empty-string isbn/memberId that were never registered should fail")
    void testEmptyStringUnregisteredKeysFail() {
        assertFalse(library.issueBook("", ""));
        assertFalse(library.returnBook("", ""));
        assertFalse(library.deleteMember(""));
    }

    @Test
    @DisplayName("Whitespace-padded ISBN is a different key than the trimmed original (no auto-trim)")
    void testWhitespacePaddedIsbnDoesNotMatch() {
        // key stored is "978-1", not " 978-1 "
        assertFalse(library.issueBook(" 978-1 ", "STU001"));
    }

    @Test
    @DisplayName("Case-different memberId should not match the registered one")
    void testCaseDifferentMemberIdDoesNotMatch() {
        assertFalse(library.issueBook("978-1", "stu001"));
    }
}
