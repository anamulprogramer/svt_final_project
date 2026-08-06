package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Member class tests")
public class MemberTest {

    private Member member;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        member = new Member("STU001", "Rahman");
        book1 = new Book("978-1", "Java Programming", "Anamul");
        book2 = new Book("978-2", "Clean Code", "Robert C. Martin");
    }

    @Test
    @DisplayName("Constructor should set id and name correctly")
    void testConstructorFields() {
        assertEquals("STU001", member.getMemberId());
        assertEquals("Rahman", member.getName());
    }

    @Test
    @DisplayName("New member should have empty borrowed books list")
    void testInitiallyNoBorrowedBooks() {
        assertNotNull(member.getBorrowedBooks());
        assertTrue(member.getBorrowedBooks().isEmpty());
    }

    @Test
    @DisplayName("borrowBook should add book to borrowed list")
    void testBorrowBook() {
        member.borrowBook(book1);
        assertEquals(1, member.getBorrowedBooks().size());
        assertTrue(member.getBorrowedBooks().contains(book1));
    }

    @Test
    @DisplayName("borrowBook should support multiple books")
    void testBorrowMultipleBooks() {
        member.borrowBook(book1);
        member.borrowBook(book2);
        assertEquals(2, member.getBorrowedBooks().size());
    }

    @Test
    @DisplayName("returnBook should remove book from borrowed list")
    void testReturnBook() {
        member.borrowBook(book1);
        member.returnBook(book1);
        assertFalse(member.getBorrowedBooks().contains(book1));
        assertEquals(0, member.getBorrowedBooks().size());
    }

    @Test
    @DisplayName("returnBook on a book not borrowed should not throw or change size")
    void testReturnBookNotBorrowed() {
        member.borrowBook(book1);
        member.returnBook(book2); // book2 was never borrowed
        assertEquals(1, member.getBorrowedBooks().size());
    }

    @Test
    @DisplayName("toString should contain member id, name and borrowed count")
    void testToString() {
        member.borrowBook(book1);
        String result = member.toString();
        assertTrue(result.contains("STU001"));
        assertTrue(result.contains("Rahman"));
        assertTrue(result.contains("1"));
    }


    @Test
    @DisplayName("borrowBook(null) should not throw; null gets added to the list as-is")
    void testBorrowNullBook() {
        assertDoesNotThrow(() -> member.borrowBook(null));
        assertEquals(1, member.getBorrowedBooks().size());
        assertTrue(member.getBorrowedBooks().contains(null));
    }

    @Test
    @DisplayName("returnBook(null) on a member who never borrowed null should not throw")
    void testReturnNullBookWhenNotPresent() {
        member.borrowBook(book1);
        assertDoesNotThrow(() -> member.returnBook(null));
        assertEquals(1, member.getBorrowedBooks().size());
    }

    @Test
    @DisplayName("Member constructor should accept null name without throwing")
    void testNullNameDoesNotThrow() {
        Member m = assertDoesNotThrow(() -> new Member("STU999", null));
        assertNull(m.getName());
    }

    @Test
    @DisplayName("Two different Member objects with same id are NOT equal by default (no equals() override)")
    void testNoCustomEquals() {
        Member duplicateIdMember = new Member("STU001", "Someone Else");
        assertNotEquals(member, duplicateIdMember);
        assertEquals(member.getMemberId(), duplicateIdMember.getMemberId());
    }
}
