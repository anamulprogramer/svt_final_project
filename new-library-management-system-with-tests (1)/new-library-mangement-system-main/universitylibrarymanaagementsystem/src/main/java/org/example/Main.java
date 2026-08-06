package org.example;

import org.example.controller.LibraryController;
import org.example.view.LibraryView;

import javax.swing.SwingUtilities;


public class Main {

    public static void main(String[] args) {
        Library library = new Library();

        library.addBook(new Book("101", "Java Programming", "John Doe"));
        library.addBook(new Book("102", "Data Structures", "Ellis Horowitz"));
        library.registerMember(new Member("S1", "Anamul"));

        LibraryController controller = new LibraryController(library);

        SwingUtilities.invokeLater(() -> new LibraryView(controller));
    }
}
