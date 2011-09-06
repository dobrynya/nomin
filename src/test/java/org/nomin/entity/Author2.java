package org.nomin.entity;

import java.util.*;

/**
 * A business entity.
 * @author Dmitry Dobrynin
 *         Date: 28.07.11 time: 3:36
 */
public class Author2 {
    private String name;
    private List<Book2> books = new ArrayList<Book2>();
    private Library2 library;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book2> getBooks() {
        return books;
    }

    public void setBooks(List<Book2> books) {
        this.books = books;
    }

    public Library2 getLibrary() {
        return library;
    }

    public void setLibrary(Library2 library) {
        this.library = library;
    }
}
