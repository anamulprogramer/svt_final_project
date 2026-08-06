package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Library class tests")
public class LibraryTest {

    private Library library;
    private Book book;
    private Member member;

    @BeforeEach
    void setUp() {
        library = new Library();
        book = new Book("978-1", "Java Programming", "Anamul");
        member = new Member("STU001", "Rahman");
    }

    @Nested
    @DisplayName("addBook / registerMember")
    class SetupTests {

        @Test
        @DisplayName("addBook should make the book issuable afterwards")
        void testAddBook() {
            library.addBook(book);
            library.registerMember(member);
            assertTrue(library.issueBook("978-1", "STU001"));
        }

        @Test
        @DisplayName("registerMember should make the member able to borrow")
        void testRegisterMember() {
            library.addBook(book);
            library.registerMember(member);
            assertTrue(library.issueBook("978-1", "STU001"));
        }
    }

    @Nested
    @DisplayName("issueBook")
    class IssueBookTests {

        @BeforeEach
        void addData() {
            library.addBook(book);
            library.registerMember(member);
        }

        @Test
        @DisplayName("Issuing an available book to a valid member should succeed")
        void testIssueSuccess() {
            boolean result = library.issueBook("978-1", "STU001");
            assertTrue(result);
            assertTrue(book.isIssued());
            assertTrue(member.getBorrowedBooks().contains(book));
        }

        @Test
        @DisplayName("Issuing an already-issued book should fail (no double borrowing)")
        void testIssueAlreadyIssuedBook() {
            Member other = new Member("STU002", "Sabbir");
            library.registerMember(other);

            library.issueBook("978-1", "STU001");
            boolean secondAttempt = library.issueBook("978-1", "STU002");

            assertFalse(secondAttempt);
            assertFalse(other.getBorrowedBooks().contains(book));
        }

        @Test
        @DisplayName("Issuing a book with a non-existent ISBN should fail")
        void testIssueInvalidIsbn() {
            boolean result = library.issueBook("000-0", "STU001");
            assertFalse(result);
        }

        @Test
        @DisplayName("Issuing a book to a non-existent member should fail")
        void testIssueInvalidMember() {
            boolean result = library.issueBook("978-1", "STU999");
            assertFalse(result);
            assertFalse(book.isIssued());
        }
    }

    @Nested
    @DisplayName("returnBook")
    class ReturnBookTests {

        @BeforeEach
        void addData() {
            library.addBook(book);
            library.registerMember(member);
        }

        @Test
        @DisplayName("Returning a properly issued book should succeed")
        void testReturnSuccess() {
            library.issueBook("978-1", "STU001");
            boolean result = library.returnBook("978-1", "STU001");

            assertTrue(result);
            assertFalse(book.isIssued());
            assertFalse(member.getBorrowedBooks().contains(book));
        }

        @Test
        @DisplayName("Returning a book that was never issued should fail")
        void testReturnNotIssuedBook() {
            boolean result = library.returnBook("978-1", "STU001");
            assertFalse(result);
        }

        @Test
        @DisplayName("Returning with wrong member (didn't borrow it) should fail")
        void testReturnByWrongMember() {
            Member other = new Member("STU002", "Sabbir");
            library.registerMember(other);

            library.issueBook("978-1", "STU001");
            boolean result = library.returnBook("978-1", "STU002");

            assertFalse(result);
            assertTrue(book.isIssued()); // still issued to STU001
        }

        @Test
        @DisplayName("Returning a book with invalid ISBN should fail")
        void testReturnInvalidIsbn() {
            boolean result = library.returnBook("000-0", "STU001");
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("deleteMember")
    class DeleteMemberTests {

        @BeforeEach
        void addData() {
            library.addBook(book);
            library.registerMember(member);
        }

        @Test
        @DisplayName("Deleting an existing member should succeed")
        void testDeleteExistingMember() {
            assertTrue(library.deleteMember("STU001"));
        }

        @Test
        @DisplayName("Deleting a non-existent member should fail")
        void testDeleteNonExistentMember() {
            assertFalse(library.deleteMember("STU999"));
        }

        @Test
        @DisplayName("Deleting a member should auto-return their borrowed books")
        void testDeleteMemberReturnsBooks() {
            library.issueBook("978-1", "STU001");
            assertTrue(book.isIssued());

            library.deleteMember("STU001");

            assertFalse(book.isIssued(), "Book should become available again after member is deleted");
        }

        @Test
        @DisplayName("After deleting a member, the freed book should be issuable to someone else")
        void testBookReusableAfterMemberDeleted() {
            library.issueBook("978-1", "STU001");
            library.deleteMember("STU001");

            Member newMember = new Member("STU002", "Sabbir");
            library.registerMember(newMember);

            assertTrue(library.issueBook("978-1", "STU002"));
        }
    }

    @Nested
    @DisplayName("showLibraryStatus (smoke test)")
    class ShowStatusTests {

        @Test
        @DisplayName("showLibraryStatus should not throw when library is empty")
        void testShowStatusEmptyLibrary() {
            assertDoesNotThrow(() -> library.showLibraryStatus());
        }

        @Test
        @DisplayName("showLibraryStatus should not throw when library has data")
        void testShowStatusWithData() {
            library.addBook(book);
            library.registerMember(member);
            library.issueBook("978-1", "STU001");
            assertDoesNotThrow(() -> library.showLibraryStatus());
        }
    }
}
