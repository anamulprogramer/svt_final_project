package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Library - failure (invalid input / illegal state) scenarios")
public class LibraryFailureTest {

    private Library library;
    private Book book;
    private Member member;

    @BeforeEach
    void setUp() {
        library = new Library();
        book = new Book("978-1", "Java Programming", "Anamul");
        member = new Member("STU001", "Rahman");
        library.addBook(book);
        library.registerMember(member);
    }

    @Test
    @DisplayName("Issuing a book that is already issued to someone else should fail (no double borrowing)")
    void testDoubleIssueFails() {
        Member other = new Member("STU002", "Sabbir");
        library.registerMember(other);

        assertTrue(library.issueBook("978-1", "STU001"));
        assertFalse(library.issueBook("978-1", "STU002"));

        assertFalse(other.getBorrowedBooks().contains(book));
    }

    @Test
    @DisplayName("Issuing with a wrong/non-existent ISBN should fail")
    void testIssueWithWrongIsbnFails() {
        assertFalse(library.issueBook("does-not-exist", "STU001"));
    }

    @Test
    @DisplayName("Issuing with a wrong/non-existent member ID should fail")
    void testIssueWithWrongMemberIdFails() {
        assertFalse(library.issueBook("978-1", "does-not-exist"));
        assertFalse(book.isIssued());
    }

    @Test
    @DisplayName("Returning a book that was never issued should fail")
    void testReturnNeverIssuedBookFails() {
        assertFalse(library.returnBook("978-1", "STU001"));
    }

    @Test
    @DisplayName("Returning a book with wrong ISBN should fail")
    void testReturnWrongIsbnFails() {
        library.issueBook("978-1", "STU001");
        assertFalse(library.returnBook("does-not-exist", "STU001"));
    }

    @Test
    @DisplayName("Returning a book with wrong member ID should fail")
    void testReturnWrongMemberIdFails() {
        library.issueBook("978-1", "STU001");
        assertFalse(library.returnBook("978-1", "does-not-exist"));
    }

    @Test
    @DisplayName("Returning someone else's borrowed book should fail (member did not borrow it)")
    void testReturnByNonBorrowerFails() {
        Member other = new Member("STU002", "Sabbir");
        library.registerMember(other);

        library.issueBook("978-1", "STU001");
        assertFalse(library.returnBook("978-1", "STU002"));
        assertTrue(book.isIssued()); // still with STU001
    }

    @Test
    @DisplayName("Deleting a non-existent member should fail")
    void testDeleteNonExistentMemberFails() {
        assertFalse(library.deleteMember("does-not-exist"));
    }

    @Test
    @DisplayName("Deleting the same member twice: second delete should fail")
    void testDoubleDeleteFails() {
        assertTrue(library.deleteMember("STU001"));
        assertFalse(library.deleteMember("STU001"));
    }
}
