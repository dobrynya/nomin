package org.nomin.entity;

import java.util.*;

/**
 * A business entity.
 * @author Dmitry Dobrynin
 *         Date: 28.07.11 time: 3:36
 */
public class Author {
    private String name;
    private List<Book> books = new ArrayList<Book>();
    private Library library;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }
}
