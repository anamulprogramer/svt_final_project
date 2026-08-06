package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Branch coverage tests for Library conditions")
public class BranchCoverageTest {

    private Library library;

    @BeforeEach
    void setUp() {
        library = new Library();
    }

    @Nested
    @DisplayName("issueBook branches: book != null && member != null && !book.isIssued()")
    class IssueBookBranches {

        @Test
        @DisplayName("Branch 1: book == null -> short-circuit false")
        void branchBookNull() {
            library.registerMember(new Member("STU001", "Rahman"));
            assertFalse(library.issueBook("no-such-isbn", "STU001"));
        }

        @Test
        @DisplayName("Branch 2: book != null, member == null -> false")
        void branchMemberNull() {
            library.addBook(new Book("978-1", "Java Programming", "Anamul"));
            assertFalse(library.issueBook("978-1", "no-such-member"));
        }

        @Test
        @DisplayName("Branch 3: book != null, member != null, book.isIssued() == true -> false")
        void branchBookAlreadyIssued() {
            Book book = new Book("978-1", "Java Programming", "Anamul");
            Member member1 = new Member("STU001", "Rahman");
            Member member2 = new Member("STU002", "Sabbir");
            library.addBook(book);
            library.registerMember(member1);
            library.registerMember(member2);

            library.issueBook("978-1", "STU001"); // book becomes issued
            assertFalse(library.issueBook("978-1", "STU002")); // this branch: already issued
        }

        @Test
        @DisplayName("Branch 4: book != null, member != null, book.isIssued() == false -> true (all conditions pass)")
        void branchAllConditionsTrue() {
            library.addBook(new Book("978-1", "Java Programming", "Anamul"));
            library.registerMember(new Member("STU001", "Rahman"));
            assertTrue(library.issueBook("978-1", "STU001"));
        }
    }

    @Nested
    @DisplayName("returnBook branches: book!=null && member!=null && book.isIssued() && member.borrowedBooks.contains(book)")
    class ReturnBookBranches {

        @Test
        @DisplayName("Branch 1: book == null -> false")
        void branchBookNull() {
            library.registerMember(new Member("STU001", "Rahman"));
            assertFalse(library.returnBook("no-such-isbn", "STU001"));
        }

        @Test
        @DisplayName("Branch 2: book != null, member == null -> false")
        void branchMemberNull() {
            library.addBook(new Book("978-1", "Java Programming", "Anamul"));
            assertFalse(library.returnBook("978-1", "no-such-member"));
        }

        @Test
        @DisplayName("Branch 3: book != null, member != null, book.isIssued() == false -> false")
        void branchBookNotIssued() {
            library.addBook(new Book("978-1", "Java Programming", "Anamul"));
            library.registerMember(new Member("STU001", "Rahman"));
            // never issued
            assertFalse(library.returnBook("978-1", "STU001"));
        }

        @Test
        @DisplayName("Branch 4: book issued == true, but member's list does NOT contain the book -> false")
        void branchMemberDidNotBorrowThisBook() {
            Book book = new Book("978-1", "Java Programming", "Anamul");
            Member owner = new Member("STU001", "Rahman");
            Member stranger = new Member("STU002", "Sabbir");

            library.addBook(book);
            library.registerMember(owner);
            library.registerMember(stranger);

            library.issueBook("978-1", "STU001"); // owner has it
            assertFalse(library.returnBook("978-1", "STU002")); // stranger tries to return it
        }

        @Test
        @DisplayName("Branch 5: all conditions true -> return succeeds")
        void branchAllConditionsTrue() {
            library.addBook(new Book("978-1", "Java Programming", "Anamul"));
            library.registerMember(new Member("STU001", "Rahman"));

            library.issueBook("978-1", "STU001");
            assertTrue(library.returnBook("978-1", "STU001"));
        }
    }

    @Nested
    @DisplayName("deleteMember branches: members.containsKey(memberId)")
    class DeleteMemberBranches {

        @Test
        @DisplayName("Branch 1: containsKey == false -> return false immediately")
        void branchMemberDoesNotExist() {
            assertFalse(library.deleteMember("STU999"));
        }

        @Test
        @DisplayName("Branch 2: containsKey == true, member has ZERO borrowed books -> loop body runs 0 times, delete succeeds")
        void branchMemberExistsWithNoBooks() {
            library.registerMember(new Member("STU001", "Rahman"));
            assertTrue(library.deleteMember("STU001"));
        }

        @Test
        @DisplayName("Branch 3: containsKey == true, member has borrowed books -> loop runs, each book freed, delete succeeds")
        void branchMemberExistsWithBooks() {
            Book book1 = new Book("978-1", "Book One", "Author");
            Book book2 = new Book("978-2", "Book Two", "Author");
            Member member = new Member("STU001", "Rahman");

            library.addBook(book1);
            library.addBook(book2);
            library.registerMember(member);
            library.issueBook("978-1", "STU001");
            library.issueBook("978-2", "STU001");

            assertTrue(library.deleteMember("STU001"));
            assertFalse(book1.isIssued());
            assertFalse(book2.isIssued());
        }
    }
}
