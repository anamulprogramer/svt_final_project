package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Duplicate data tests")
public class DuplicateDataTest {

    private Library library;

    @BeforeEach
    void setUp() {
        library = new Library();
    }

    @Test
    @DisplayName("Adding two books with the SAME isbn: the second book overwrites the first in the library")
    void testDuplicateIsbnOverwritesBook() {
        Book originalBook = new Book("978-1", "Original Title", "Author A");
        Book duplicateBook = new Book("978-1", "Replacement Title", "Author B");
        Member member = new Member("STU001", "Rahman");

        library.addBook(originalBook);
        library.addBook(duplicateBook);
        library.registerMember(member);

        assertTrue(library.issueBook("978-1", "STU001"));

        assertTrue(duplicateBook.isIssued());
        assertFalse(originalBook.isIssued(), "The old book is no longer being tracked within the library, so its 'isIssued' status should not be changed.");
    }

    @Test
    @DisplayName("Registering two members with the SAME memberId: the second member overwrites the first")
    void testDuplicateMemberIdOverwritesMember() {
        Book book = new Book("978-1", "Java Programming", "Anamul");
        Member originalMember = new Member("STU001", "Original Name");
        Member duplicateMember = new Member("STU001", "Replacement Name");

        library.addBook(book);
        library.registerMember(originalMember);
        library.registerMember(duplicateMember); // same id -> replaces originalMember

        assertTrue(library.issueBook("978-1", "STU001"));

        // book must be borrowed by the SECOND (duplicate) member, not the first
        assertTrue(duplicateMember.getBorrowedBooks().contains(book));
        assertTrue(originalMember.getBorrowedBooks().isEmpty(),
                "The old member object is no longer registered with the library, so nothing should be added to its list.");
    }

    @Test
    @DisplayName("Re-adding the same book object with the same isbn is a harmless no-op")
    void testReAddingSameBookObjectIsHarmless() {
        Book book = new Book("978-1", "Java Programming", "Anamul");
        Member member = new Member("STU001", "Rahman");

        library.addBook(book);
        library.addBook(book); // same object, same isbn, added twice
        library.registerMember(member);

        assertTrue(library.issueBook("978-1", "STU001"));
        assertTrue(book.isIssued());
    }

    @Test
    @DisplayName("Duplicate isbn added AFTER issuing: overwritten book entry is fresh (not issued) even though old one was")
    void testDuplicateAddAfterIssueResetsAvailability() {
        Book originalBook = new Book("978-1", "Original Title", "Author A");
        Member member = new Member("STU001", "Rahman");

        library.addBook(originalBook);
        library.registerMember(member);
        library.issueBook("978-1", "STU001");
        assertTrue(originalBook.isIssued());


        Book freshBook = new Book("978-1", "Original Title", "Author A");
        library.addBook(freshBook);

        Member anotherMember = new Member("STU002", "Sabbir");
        library.registerMember(anotherMember);
        assertTrue(library.issueBook("978-1", "STU002"));
    }

    @Test
    @DisplayName("Deleting a duplicate-overwritten member only removes the currently mapped member")
    void testDeleteAfterDuplicateRegistration() {
        Member originalMember = new Member("STU001", "Original Name");
        Member duplicateMember = new Member("STU001", "Replacement Name");

        library.registerMember(originalMember);
        library.registerMember(duplicateMember);

        assertTrue(library.deleteMember("STU001"));

        assertFalse(library.deleteMember("STU001"));
    }
}
