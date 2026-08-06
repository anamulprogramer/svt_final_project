package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Integration tests - full library workflow")
public class IntegrationTests {

    private Library library;

    @BeforeEach
    void setUp() {
        library = new Library();
    }

    @Test
    @DisplayName("Realistic multi-book, multi-member workflow end-to-end")
    void testRealisticLibraryWorkflow() {
        // ধাপ ১: বই ও মেম্বার যোগ করা
        Book book1 = new Book("978-1", "Java Programming", "Anamul");
        Book book2 = new Book("978-2", "Clean Code", "Robert C. Martin");
        Book book3 = new Book("978-3", "Design Patterns", "GoF");

        Member alice = new Member("STU001", "Alice");
        Member bob = new Member("STU002", "Bob");

        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        library.registerMember(alice);
        library.registerMember(bob);


        assertTrue(library.issueBook("978-1", "STU001"));
        assertTrue(library.issueBook("978-2", "STU001"));
        assertEquals(2, alice.getBorrowedBooks().size());


        assertTrue(library.issueBook("978-3", "STU002"));
        assertFalse(library.issueBook("978-1", "STU002"));

        assertTrue(library.returnBook("978-1", "STU001"));
        assertEquals(1, alice.getBorrowedBooks().size());

        assertTrue(library.issueBook("978-1", "STU002"));
        assertEquals(2, bob.getBorrowedBooks().size());

        assertTrue(library.deleteMember("STU001"));
        assertFalse(book2.isIssued(), "After Alice is deleted, her remaining books should become available.");

        Member charlie = new Member("STU003", "Charlie");
        library.registerMember(charlie);
        assertTrue(library.issueBook("978-2", "STU003"));
        assertDoesNotThrow(() -> library.showLibraryStatus());
    }

    @Test
    @DisplayName("Concurrent-like scenario: many members borrowing and returning in sequence keeps state consistent")
    void testManyIssueReturnCyclesKeepStateConsistent() {
        Book book = new Book("978-1", "Shared Book", "Author");
        library.addBook(book);

        for (int i = 0; i < 20; i++) {
            Member m = new Member("STU" + i, "Student " + i);
            library.registerMember(m);

            assertTrue(library.issueBook("978-1", "STU" + i));
            assertTrue(book.isIssued());

            assertTrue(library.returnBook("978-1", "STU" + i));
            assertFalse(book.isIssued());
        }
    }

    @Test
    @DisplayName("Deleting a member who never borrowed anything should not affect other members' books")
    void testDeleteMemberWithNoBooksDoesNotAffectOthers() {
        Book book = new Book("978-1", "Java Programming", "Anamul");
        Member borrower = new Member("STU001", "Alice");
        Member emptyMember = new Member("STU002", "Bob");

        library.addBook(book);
        library.registerMember(borrower);
        library.registerMember(emptyMember);

        library.issueBook("978-1", "STU001");

        assertTrue(library.deleteMember("STU002"));
        assertTrue(book.isIssued(), "\n" +
                "Deleting Bob should not change the status of the book borrowed by Alice.");
    }

    @Test
    @DisplayName("Multiple members and multiple books can be independently tracked without cross-interference")
    void testIndependentTrackingAcrossMultipleEntities() {
        for (int i = 0; i < 5; i++) {
            library.addBook(new Book("B" + i, "Book " + i, "Author " + i));
            library.registerMember(new Member("M" + i, "Member " + i));
        }

        for (int i = 0; i < 5; i++) {
            assertTrue(library.issueBook("B" + i, "M" + i));
        }

        assertFalse(library.issueBook("B0", "M1"));
        assertFalse(library.returnBook("B0", "M1"));
    }
}
