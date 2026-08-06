package org.example.controller;

import org.example.Library;
import org.example.factory.EntityFactory;


public class LibraryController {

    private final Library library;

    public LibraryController(Library library) {
        this.library = library;
    }

    public boolean registerMember(String memberId, String name) {
        if (isBlank(memberId) || isBlank(name)) {
            return false;
        }
        library.registerMember(EntityFactory.createMember(memberId, name));
        return true;
    }

    public boolean deleteMember(String memberId) {
        if (isBlank(memberId)) {
            return false;
        }
        return library.deleteMember(memberId);
    }

    public boolean addBook(String isbn, String title, String author) {
        if (isBlank(isbn) || isBlank(title) || isBlank(author)) {
            return false;
        }
        library.addBook(EntityFactory.createBook(isbn, title, author));
        return true;
    }

    public boolean issueBook(String isbn, String memberId) {
        return library.issueBook(isbn, memberId);
    }

    public boolean returnBook(String isbn, String memberId) {
        return library.returnBook(isbn, memberId);
    }

    public String getStatusReport() {
        return library.getStatusReport();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
