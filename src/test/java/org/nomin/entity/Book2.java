package org.nomin.entity;

import java.util.*;

/**
 * A business entity.
 * @author Dmitry Dobrynin
 *         Date: 28.07.11 time: 3:37
 */
public class Book2 {
    private String name;
    private List<Author2> authors = new ArrayList<Author2>();
    private Library2 library;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Author2> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author2> authors) {
        this.authors = authors;
    }

    public Library2 getLibrary() {
        return library;
    }

    public void setLibrary(Library2 library) {
        this.library = library;
    }
}
