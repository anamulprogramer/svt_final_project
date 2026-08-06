package org.example;

import org.example.dao.BookDAO;
import org.example.dao.MemberDAO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Library tests using Mockito (mocked BookDAO / MemberDAO)")
public class LibraryMockitoTest {

    @Mock
    private BookDAO bookDAO;

    @Mock
    private MemberDAO memberDAO;

    @Test
    @DisplayName("addBook should delegate to bookDAO.save() exactly once, and never touch memberDAO")
    void testAddBookDelegatesToDao() {
        Library library = new Library(bookDAO, memberDAO);
        Book book = new Book("978-1", "Java Programming", "Anamul");

        library.addBook(book);

        verify(bookDAO, times(1)).save(book);
        verifyNoInteractions(memberDAO);
    }

    @Test
    @DisplayName("registerMember should delegate to memberDAO.save() exactly once, and never touch bookDAO")
    void testRegisterMemberDelegatesToDao() {
        Library library = new Library(bookDAO, memberDAO);
        Member member = new Member("STU001", "Rahman");

        library.registerMember(member);

        verify(memberDAO, times(1)).save(member);
        verifyNoInteractions(bookDAO);
    }

    @Test
    @DisplayName("issueBook: DAO returns a real book+member, book not issued -> success, state updates correctly")
    void testIssueBookSuccessWithMocks() {
        Book book = new Book("978-1", "Java Programming", "Anamul");
        Member member = new Member("STU001", "Rahman");

        when(bookDAO.findByIsbn("978-1")).thenReturn(book);
        when(memberDAO.findById("STU001")).thenReturn(member);

        Library library = new Library(bookDAO, memberDAO);
        boolean result = library.issueBook("978-1", "STU001");

        assertTrue(result);
        assertTrue(book.isIssued());
        assertTrue(member.getBorrowedBooks().contains(book));
        verify(bookDAO).findByIsbn("978-1");
        verify(memberDAO).findById("STU001");
    }

    @Test
    @DisplayName("issueBook: bookDAO.findByIsbn returns null (isbn not found) -> issueBook returns false")
    void testIssueBookFailsWhenBookNotFound() {
        when(bookDAO.findByIsbn("no-such-isbn")).thenReturn(null);
        when(memberDAO.findById("STU001")).thenReturn(new Member("STU001", "Rahman"));

        Library library = new Library(bookDAO, memberDAO);
        boolean result = library.issueBook("no-such-isbn", "STU001");

        assertFalse(result);
        verify(bookDAO).findByIsbn("no-such-isbn");
    }

    @Test
    @DisplayName("issueBook: memberDAO.findById returns null (member not found) -> issueBook returns false")
    void testIssueBookFailsWhenMemberNotFound() {
        when(bookDAO.findByIsbn("978-1")).thenReturn(new Book("978-1", "Java Programming", "Anamul"));
        when(memberDAO.findById("no-such-member")).thenReturn(null);

        Library library = new Library(bookDAO, memberDAO);
        assertFalse(library.issueBook("978-1", "no-such-member"));
    }

    @Test
    @DisplayName("issueBook: book already issued (per mock) -> returns false, member never borrows it")
    void testIssueBookFailsWhenAlreadyIssued() {
        Book book = new Book("978-1", "Java Programming", "Anamul");
        book.setIssued(true);
        Member member = new Member("STU001", "Rahman");

        when(bookDAO.findByIsbn("978-1")).thenReturn(book);
        when(memberDAO.findById("STU001")).thenReturn(member);

        Library library = new Library(bookDAO, memberDAO);
        assertFalse(library.issueBook("978-1", "STU001"));
        assertTrue(member.getBorrowedBooks().isEmpty());
    }

    @Test
    @DisplayName("returnBook: success path correctly flips book state when DAO-provided objects match")
    void testReturnBookSuccessWithMocks() {
        Book book = new Book("978-1", "Java Programming", "Anamul");
        Member member = new Member("STU001", "Rahman");
        book.setIssued(true);
        member.borrowBook(book);

        when(bookDAO.findByIsbn("978-1")).thenReturn(book);
        when(memberDAO.findById("STU001")).thenReturn(member);

        Library library = new Library(bookDAO, memberDAO);
        assertTrue(library.returnBook("978-1", "STU001"));
        assertFalse(book.isIssued());
    }

    @Test
    @DisplayName("deleteMember: member exists with borrowed books -> books freed, memberDAO.deleteById called")
    void testDeleteMemberFreesBooksWithMocks() {
        Book book1 = new Book("978-1", "Book One", "Author");
        Book book2 = new Book("978-2", "Book Two", "Author");
        Member member = new Member("STU001", "Rahman");
        book1.setIssued(true);
        book2.setIssued(true);
        member.borrowBook(book1);
        member.borrowBook(book2);

        when(memberDAO.findById("STU001")).thenReturn(member);
        when(memberDAO.deleteById("STU001")).thenReturn(true);

        Library library = new Library(bookDAO, memberDAO);
        assertTrue(library.deleteMember("STU001"));

        assertFalse(book1.isIssued());
        assertFalse(book2.isIssued());
        verify(memberDAO).deleteById("STU001");
    }

    @Test
    @DisplayName("deleteMember: memberDAO.findById returns null -> returns false, deleteById is NEVER called")
    void testDeleteMemberNotFoundWithMocks() {
        when(memberDAO.findById("no-such-id")).thenReturn(null);

        Library library = new Library(bookDAO, memberDAO);
        assertFalse(library.deleteMember("no-such-id"));

        verify(memberDAO, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("getStatusReport: reads all books/members through DAO.findAll(), and includes their data")
    void testGetStatusReportUsesFindAll() {
        Book book = new Book("978-1", "Java Programming", "Anamul");
        Member member = new Member("STU001", "Rahman");

        when(bookDAO.findAll()).thenReturn(Collections.singletonList(book));
        when(memberDAO.findAll()).thenReturn(Collections.singletonList(member));

        Library library = new Library(bookDAO, memberDAO);
        String report = library.getStatusReport();

        assertTrue(report.contains("978-1"));
        assertTrue(report.contains("STU001"));
        verify(bookDAO, atLeastOnce()).findAll();
        verify(memberDAO, atLeastOnce()).findAll();
    }
}
