package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Library - success (happy path) scenarios")
public class LibrarySuccessTest {

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
    @DisplayName("Add a book then it should be available for issuing")
    void testAddBookSuccess() {
        assertTrue(library.issueBook("978-1", "STU001"));
    }

    @Test
    @DisplayName("Register a member then that member should be able to borrow")
    void testRegisterMemberSuccess() {
        assertTrue(library.issueBook("978-1", "STU001"));
    }

    @Test
    @DisplayName("Issue a book successfully: book becomes issued, member gets it in their list")
    void testIssueBookSuccess() {
        boolean result = library.issueBook("978-1", "STU001");

        assertTrue(result);
        assertTrue(book.isIssued());
        assertTrue(member.getBorrowedBooks().contains(book));
    }

    @Test
    @DisplayName("Return a book successfully: book becomes available again, removed from member's list")
    void testReturnBookSuccess() {
        library.issueBook("978-1", "STU001");
        boolean result = library.returnBook("978-1", "STU001");

        assertTrue(result);
        assertFalse(book.isIssued());
        assertFalse(member.getBorrowedBooks().contains(book));
    }

    @Test
    @DisplayName("Delete an existing member successfully")
    void testDeleteMemberSuccess() {
        assertTrue(library.deleteMember("STU001"));
    }

    @Test
    @DisplayName("Full happy-path cycle: add -> register -> issue -> return -> delete")
    void testFullSuccessCycle() {
        Book newBook = new Book("978-2", "Clean Code", "Robert C. Martin");
        Member newMember = new Member("STU002", "Sabbir");

        library.addBook(newBook);
        library.registerMember(newMember);

        assertTrue(library.issueBook("978-2", "STU002"));
        assertTrue(library.returnBook("978-2", "STU002"));
        assertTrue(library.deleteMember("STU002"));
    }

    @Test
    @DisplayName("A returned book can be issued again to a different member")
    void testReissueAfterReturn() {
        Member secondMember = new Member("STU002", "Sabbir");
        library.registerMember(secondMember);

        library.issueBook("978-1", "STU001");
        library.returnBook("978-1", "STU001");

        assertTrue(library.issueBook("978-1", "STU002"));
        assertTrue(secondMember.getBorrowedBooks().contains(book));
    }

    @Test
    @DisplayName("showLibraryStatus runs without error after a full successful workflow")
    void testShowStatusAfterSuccessWorkflow() {
        library.issueBook("978-1", "STU001");
        assertDoesNotThrow(() -> library.showLibraryStatus());
    }
}
