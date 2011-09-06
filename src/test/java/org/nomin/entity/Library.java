package org.nomin.entity;

import java.util.*;

/**
 * A business entity.
 * @author Dmitry Dobrynin
 *         Date: 29.07.11 time: 0:28
 */
public class Library {
    private String name;
    private List<Book> books = new ArrayList<Book>();
    private List<Author> authors = new ArrayList<Author>();

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

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
}
