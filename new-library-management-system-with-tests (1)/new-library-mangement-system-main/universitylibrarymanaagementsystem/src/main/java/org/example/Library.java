package org.example;

import org.example.dao.BookDAO;
import org.example.dao.MemberDAO;
import org.example.factory.DAOFactory;

import java.util.ArrayList;


public class Library {

    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;

    public Library() {
        this.bookDAO = DAOFactory.createBookDAO();
        this.memberDAO = DAOFactory.createMemberDAO();
    }


    public Library(BookDAO bookDAO, MemberDAO memberDAO) {
        this.bookDAO = bookDAO;
        this.memberDAO = memberDAO;
    }


    public void addBook(Book book) {
        bookDAO.save(book);
    }

    
    public void registerMember(Member member) {
        memberDAO.save(member);
    }

    public boolean deleteMember(String memberId) {
        Member member = memberDAO.findById(memberId);
        if (member != null) {
            for (Book book : new ArrayList<>(member.getBorrowedBooks())) {
                book.setIssued(false);
            }
            return memberDAO.deleteById(memberId);
        }
        return false;
    }
    public boolean issueBook(String isbn, String memberId) {
        Book book = bookDAO.findByIsbn(isbn);
        Member member = memberDAO.findById(memberId);
        if (book != null && member != null && !book.isIssued()) {
            book.setIssued(true);
            member.borrowBook(book);
            return true;
        }
        return false;
    }

    public boolean returnBook(String isbn, String memberId) {
        Book book = bookDAO.findByIsbn(isbn);
        Member member = memberDAO.findById(memberId);
        if (book != null && member != null && book.isIssued() && member.getBorrowedBooks().contains(book)) {
            book.setIssued(false);
            member.returnBook(book);
            return true;
        }
        return false;
    }

    public String getStatusReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n================= LIBRARY CURRENT STATUS =================\n");
        sb.append("--- BOOKS IN SYSTEM ---\n");
        if (bookDAO.findAll().isEmpty()) sb.append("No books registered yet.\n");
        for (Book b : bookDAO.findAll()) {
            sb.append(b).append("\n");
        }

        sb.append("\n--- REGISTERED STUDENTS ---\n");
        if (memberDAO.findAll().isEmpty()) sb.append("No students registered yet.\n");
        for (Member m : memberDAO.findAll()) {
            sb.append(m).append("\n");
        }
        sb.append("==========================================================\n");
        return sb.toString();
    }

   
    public void showLibraryStatus() {
        System.out.println(getStatusReport());
    }
}
