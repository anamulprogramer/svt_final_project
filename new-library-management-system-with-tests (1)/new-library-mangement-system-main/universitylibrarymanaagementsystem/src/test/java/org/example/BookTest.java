package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Book class tests")
public class BookTest {

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book("978-1", "Java Programming", "Anamul");
    }

    @Test
    @DisplayName("Constructor should set fields correctly")
    void testConstructorFields() {
        assertEquals("978-1", book.getIsbn());
        assertEquals("Java Programming", book.getTitle());
        assertEquals("Anamul", book.getAuthor());
    }

    @Test
    @DisplayName("New book should not be issued by default")
    void testNewBookNotIssued() {
        assertFalse(book.isIssued());
    }

    @Test
    @DisplayName("setIssued(true) should mark book as issued")
    void testSetIssuedTrue() {
        book.setIssued(true);
        assertTrue(book.isIssued());
    }

    @Test
    @DisplayName("setIssued(false) should mark book as available again")
    void testSetIssuedFalse() {
        book.setIssued(true);
        book.setIssued(false);
        assertFalse(book.isIssued());
    }

    @Test
    @DisplayName("toString should contain isbn, title and author")
    void testToStringContainsFields() {
        String result = book.toString();
        assertTrue(result.contains("978-1"));
        assertTrue(result.contains("Java Programming"));
        assertTrue(result.contains("Anamul"));
    }

    @Test
    @DisplayName("toString should indicate Available when not issued")
    void testToStringAvailable() {
        assertTrue(book.toString().contains("Available"));
    }

    @Test
    @DisplayName("toString should indicate Borrowed when issued")
    void testToStringBorrowed() {
        book.setIssued(true);
        assertTrue(book.toString().contains("Borrowed"));
    }

    // ---------- Null / invalid field handling ----------

    @Test
    @DisplayName("Book constructor should accept null isbn without throwing (stores null as-is)")
    void testNullIsbnDoesNotThrow() {
        Book b = assertDoesNotThrow(() -> new Book(null, "Some Title", "Some Author"));
        assertNull(b.getIsbn());
    }

    @Test
    @DisplayName("Book constructor should accept null title without throwing")
    void testNullTitleDoesNotThrow() {
        Book b = assertDoesNotThrow(() -> new Book("978-2", null, "Some Author"));
        assertNull(b.getTitle());
    }

    @Test
    @DisplayName("Book constructor should accept empty-string isbn as a valid (boundary) value")
    void testEmptyStringIsbn() {
        Book b = new Book("", "Title", "Author");
        assertEquals("", b.getIsbn());
    }

    @Test
    @DisplayName("toString should not throw even when title/author are null")
    void testToStringWithNullFields() {
        Book b = new Book("978-3", null, null);
        assertDoesNotThrow(b::toString);
    }
}
