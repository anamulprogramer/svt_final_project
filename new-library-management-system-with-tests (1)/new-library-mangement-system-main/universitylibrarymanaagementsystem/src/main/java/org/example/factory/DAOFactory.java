package org.example.factory;

import org.example.dao.BookDAO;
import org.example.dao.MemberDAO;
import org.example.dao.impl.InMemoryBookDAO;
import org.example.dao.impl.InMemoryMemberDAO;


public class DAOFactory {

    private DAOFactory() {

    }

    public static BookDAO createBookDAO() {
        return new InMemoryBookDAO();
    }

    public static MemberDAO createMemberDAO() {
        return new InMemoryMemberDAO();
    }
}
