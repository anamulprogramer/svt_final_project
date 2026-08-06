package org.example.dao.impl;

import org.example.Book;
import org.example.dao.BookDAO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class InMemoryBookDAO implements BookDAO {

    private final Map<String, Book> books = new HashMap<>();

    @Override
    public void save(Book book) {
        books.put(book.getIsbn(), book);
    }

    @Override
    public Book findByIsbn(String isbn) {
        return books.get(isbn);
    }

    @Override
    public boolean deleteByIsbn(String isbn) {
        return books.remove(isbn) != null;
    }

    @Override
    public Collection<Book> findAll() {
        return books.values();
    }
}
