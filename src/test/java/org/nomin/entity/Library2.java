package org.nomin.entity;

import java.util.*;

/**
 * A business entity.
 * @author Dmitry Dobrynin
 *         Date: 29.07.11 time: 0:28
 */
public class Library2 {
    private String name;
    private List<Book2> books = new ArrayList<Book2>();
    private List<Author2> authors = new ArrayList<Author2>();

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

    public List<Author2> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author2> authors) {
        this.authors = authors;
    }
}
