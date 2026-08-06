package org.example.factory;

import org.example.Book;
import org.example.Member;


public class EntityFactory {

    private EntityFactory() {
       
    }

    public static Book createBook(String isbn, String title, String author) {
        return new Book(isbn, title, author);
    }

    public static Member createMember(String memberId, String name) {
        return new Member(memberId, name);
    }
}
