package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Exception behavior tests")
public class ExceptionTest {

    private Library library;

    @BeforeEach
    void setUp() {
        library = new Library();
    }

    @Test
    @DisplayName("addBook(null) throws NullPointerException, because it calls book.getIsbn() internally")
    void testAddNullBookThrowsNPE() {
        assertThrows(NullPointerException.class, () -> library.addBook(null));
    }

    @Test
    @DisplayName("registerMember(null) throws NullPointerException, because it calls member.getMemberId() internally")
    void testRegisterNullMemberThrowsNPE() {
        assertThrows(NullPointerException.class, () -> library.registerMember(null));
    }

    @Test
    @DisplayName("issueBook with null isbn/memberId does NOT throw (HashMap.get(null) is legal)")
    void testIssueBookWithNullsDoesNotThrow() {
        assertDoesNotThrow(() -> library.issueBook(null, null));
        assertFalse(library.issueBook(null, null));
    }

    @Test
    @DisplayName("returnBook with null isbn/memberId does NOT throw")
    void testReturnBookWithNullsDoesNotThrow() {
        assertDoesNotThrow(() -> library.returnBook(null, null));
        assertFalse(library.returnBook(null, null));
    }

    @Test
    @DisplayName("deleteMember(null) does NOT throw")
    void testDeleteMemberWithNullDoesNotThrow() {
        assertDoesNotThrow(() -> library.deleteMember(null));
        assertFalse(library.deleteMember(null));
    }

    @Test
    @DisplayName("Book constructor never throws, even with all-null fields")
    void testBookConstructorNeverThrows() {
        assertDoesNotThrow(() -> new Book(null, null, null));
    }

    @Test
    @DisplayName("Member constructor never throws, even with all-null fields")
    void testMemberConstructorNeverThrows() {
        assertDoesNotThrow(() -> new Member(null, null));
    }

    @Test
    @DisplayName("Member.borrowBook(null) does not throw (ArrayList allows null elements)")
    void testBorrowNullBookDoesNotThrow() {
        Member member = new Member("STU001", "Rahman");
        assertDoesNotThrow(() -> member.borrowBook(null));
    }

    @Test
    @DisplayName("showLibraryStatus() never throws regardless of library state")
    void testShowLibraryStatusNeverThrows() {
        assertDoesNotThrow(() -> library.showLibraryStatus());

        library.addBook(new Book(null, null, null));
        library.registerMember(new Member(null, null));
        assertDoesNotThrow(() -> library.showLibraryStatus());
    }
}
