package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Boundary value tests")
public class BoundaryTest {

    @Test
    @DisplayName("Empty library: issueBook should fail gracefully (no book, no member)")
    void testIssueOnCompletelyEmptyLibrary() {
        Library library = new Library();
        assertFalse(library.issueBook("978-1", "STU001"));
    }

    @Test
    @DisplayName("Empty library: returnBook should fail gracefully")
    void testReturnOnCompletelyEmptyLibrary() {
        Library library = new Library();
        assertFalse(library.returnBook("978-1", "STU001"));
    }

    @Test
    @DisplayName("Empty library: deleteMember should fail gracefully")
    void testDeleteOnCompletelyEmptyLibrary() {
        Library library = new Library();
        assertFalse(library.deleteMember("STU001"));
    }

    @Test
    @DisplayName("Empty library: showLibraryStatus should not throw")
    void testShowStatusOnEmptyLibrary() {
        Library library = new Library();
        assertDoesNotThrow(library::showLibraryStatus);
    }

    @Test
    @DisplayName("Empty-string ISBN is treated as a valid boundary key")
    void testEmptyStringIsbnAsKey() {
        Library library = new Library();
        Book book = new Book("", "No ISBN Book", "Unknown");
        Member member = new Member("STU001", "Rahman");

        library.addBook(book);
        library.registerMember(member);

        assertTrue(library.issueBook("", "STU001"));
    }

    @Test
    @DisplayName("Empty-string member ID is treated as a valid boundary key")
    void testEmptyStringMemberIdAsKey() {
        Library library = new Library();
        Book book = new Book("978-1", "Java Programming", "Anamul");
        Member member = new Member("", "No ID Student");

        library.addBook(book);
        library.registerMember(member);

        assertTrue(library.issueBook("978-1", ""));
    }

    @Test
    @DisplayName("Single-character ISBN and member ID should work normally")
    void testSingleCharacterIds() {
        Library library = new Library();
        Book book = new Book("A", "Short ISBN Book", "Author");
        Member member = new Member("1", "Short Id Student");

        library.addBook(book);
        library.registerMember(member);

        assertTrue(library.issueBook("A", "1"));
    }

    @Test
    @DisplayName("Very long ISBN/member ID strings should still work")
    void testVeryLongIdStrings() {
        Library library = new Library();
        String longIsbn = "978-" + "9".repeat(500);
        String longMemberId = "STU" + "0".repeat(500);

        Book book = new Book(longIsbn, "Big ISBN Book", "Author");
        Member member = new Member(longMemberId, "Student");

        library.addBook(book);
        library.registerMember(member);

        assertTrue(library.issueBook(longIsbn, longMemberId));
    }

    @Test
    @DisplayName("Whitespace-only ISBN/member ID is a distinct key from empty string / trimmed version")
    void testWhitespaceIsNotSameAsEmptyOrTrimmed() {
        Library library = new Library();
        Book book = new Book(" ", "Whitespace ISBN Book", "Author");
        Member member = new Member("STU001", "Rahman");

        library.addBook(book);
        library.registerMember(member);

       
        assertFalse(library.issueBook("", "STU001"));
        assertTrue(library.issueBook(" ", "STU001"));
    }

    @Test
    @DisplayName("A member with a large number of borrowed books (boundary of collection growth)")
    void testMemberWithManyBorrowedBooks() {
        Library library = new Library();
        Member member = new Member("STU001", "Rahman");
        library.registerMember(member);

        int totalBooks = 1000;
        for (int i = 0; i < totalBooks; i++) {
            Book book = new Book("ISBN-" + i, "Book " + i, "Author " + i);
            library.addBook(book);
            assertTrue(library.issueBook("ISBN-" + i, "STU001"));
        }

        assertEquals(totalBooks, member.getBorrowedBooks().size());
    }

    @Test
    @DisplayName("ISBN keys are case-sensitive (boundary of string equality)")
    void testIsbnKeysAreCaseSensitive() {
        Library library = new Library();
        Book book = new Book("ABC-1", "Casing Book", "Author");
        Member member = new Member("STU001", "Rahman");

        library.addBook(book);
        library.registerMember(member);

        assertFalse(library.issueBook("abc-1", "STU001"), "Lowercase isbn should NOT match the uppercase key");
        assertTrue(library.issueBook("ABC-1", "STU001"));
    }
}
