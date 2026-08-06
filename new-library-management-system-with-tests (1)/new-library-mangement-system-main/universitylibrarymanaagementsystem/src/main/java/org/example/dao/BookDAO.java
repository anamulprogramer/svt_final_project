package org.example.dao;

import org.example.Book;
import java.util.Collection;

public interface BookDAO {

    void save(Book book);

    Book findByIsbn(String isbn);

    boolean deleteByIsbn(String isbn);

    Collection<Book> findAll();
}
