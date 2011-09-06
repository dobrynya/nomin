package org.nomin.functional

import org.nomin.core.Nomin
import org.nomin.entity.*

/**
 * Tests preventing infine loops when there is a cycle between entities.
 * @author Dmitry Dobrynin
 * Date: 28.07.11 time: 3:38
 */
class CyclicDependencyTest {
    def nomin = new Nomin()

    @org.junit.Test
    void test1() {
        Library l = new Library(name: "The Library")
        Author a = new Author(name: "Harry Harrison", library: l)
        Book b = new Book(name: "The Death Planet", library: l)
        a.books << b
        b.authors << a
        l.authors << a
        l.books << b

        def l2 = nomin.map(l, Library2)
        assert l2.name == l.name && l2.authors.size() == 1 && l2.authors[0].name == l.authors[0].name && l2.authors[0].library.is(l2)
        assert l2.books.size() == 1 && l2.books[0].name == l.books[0].name && l2.books[0].library.is(l2)
        assert l2.books[0].authors.size() == 1 && l2.books[0].authors[0].is(l2.authors[0])
        assert l2.authors[0].books.size() == 1 && l2.authors[0].books[0].is(l2.books[0])
    }

    @org.junit.Test
    void test2() {
        Library l = new Library(name: "The Library")
        Author a = new Author(name: "Harry Harrison", library: l)
        Book b = new Book(name: "The Death Planet", library: l)
        a.books << b
        b.authors << a
        l.authors << a
        l.books << b

        def l2 = nomin.map(l, new Library2())
        assert l2.name == l.name && l2.authors.size() == 1 && l2.authors[0].name == l.authors[0].name && l2.authors[0].library.is(l2)
        assert l2.books.size() == 1 && l2.books[0].name == l.books[0].name && l2.books[0].library.is(l2)
        assert l2.books[0].authors.size() == 1 && l2.books[0].authors[0].is(l2.authors[0])
        assert l2.authors[0].books.size() == 1 && l2.authors[0].books[0].is(l2.books[0])
    }
}
