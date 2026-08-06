package org.example;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private boolean isIssued;

    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.isIssued = false;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isIssued() { return isIssued; }
    public void setIssued(boolean issued) { isIssued = issued; }

    @Override
    public String toString() {
        return "[ID/ISBN: " + isbn + "] " + title + " (Author: " + author + ") - " + (isIssued ? "Borrowed ❌" : "Available  ");
    }
}