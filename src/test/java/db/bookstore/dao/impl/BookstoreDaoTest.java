
package db.bookstore.dao.impl;
import db.bookstore.dao.Author;
import db.bookstore.dao.Book;
import db.bookstore.dao.BookstoreDao;
import db.configs.MainJavaConfig;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Created by StudentDB on 21.04.2015.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainJavaConfig.class)
@ActiveProfiles("test")
public class BookstoreDaoTest {

    @Autowired
    private BookstoreDao dao;

    @Test
    public void testGetAuthor() throws Exception {
        assertEquals(new DefaultAuthor(101, "author1", new DateTime("2000-04-05"), null), dao.getAuthor(101));
    }

    @Test(expected = DuplicateKeyException.class)
    public void testAddAuthor() throws Exception {
        dao.addAuthor("author1", new DateTime("2000-04-05"), null);
    }

    @Test
    public void testAddGetAndDeleteAuthor() throws Exception {
        Author author = dao.addAuthor("new_author", new DateTime("2000-04-05"), new DateTime("2010-04-05"));
        assertEquals(6, dao.getAllAuthors().size());
        assertEquals(author, dao.getAuthor(author.getId()));
        dao.deleteAuthor(author.getId());
        assertNull(dao.getAuthor(author.getId()));
        assertEquals(5, dao.getAllAuthors().size());
    }

    @Test
    public void testAddGetAndDeleteBook() throws Exception {
        Author author1 = dao.getAuthor(1101);
        assertNotNull(author1);
        Author author2 = dao.getAuthor(1102);
        assertNotNull(author2);
        Author author3 = dao.getAuthor(1103);
        assertNotNull(author3);
        Book book = dao.addBook("new_book", 10.0, DateTime.now(), Arrays.asList(author1, author2, author3));

        assertEquals(book, dao.getBook(book.getId()));
        assertTrue(dao.getAllBooks().contains(book));
        assertTrue(dao.getBooksOfAuthor(author1).contains(book));
        assertTrue(dao.getBooksOfAuthor(author2).contains(book));
        assertTrue(dao.getBooksOfAuthor(author3).contains(book));

        dao.deleteBook(book.getId());
        assertFalse(dao.getAllBooks().contains(book));
        assertFalse(dao.getBooksOfAuthor(author1).contains(book));
        assertFalse(dao.getBooksOfAuthor(author2).contains(book));
        assertFalse(dao.getBooksOfAuthor(author3).contains(book));
    }

    @Test
    public void testGetBooksOfAuthor() throws Exception {
        Author author = dao.getAuthor(101);
        assertNotNull(author);
        assertEquals(2, dao.getBooksOfAuthor(author).size());
    }

    @Test
    public void testGetBooksWithPriceMoreThan() throws Exception {
        assertEquals(3, dao.getBooksWithPriceMoreThan(33.0).size());
    }

    @Test
    public void testGetBooksWithPriceLessThan() throws Exception {
        assertEquals(2, dao.getBooksWithPriceLessThan(36.0).size());
    }

    @Test(expected = DuplicateKeyException.class)
    public void testAddBook() throws Exception {
        dao.addBook("test1", 30.0, new DateTime("2015-04-14"), Collections.emptyList());
    }

    @Test
    public void testAddAndDeleteAuthority() throws Exception {
        Book book = dao.getBook(701);
        assertNotNull(book);
        Author author = dao.getAuthor(101);
        assertNotNull(author);
        dao.addAuthority(author, book);
        assertTrue(dao.getBooksOfAuthor(author).contains(book));
        dao.deleteAuthority(author, book);
        assertFalse(dao.getBooksOfAuthor(author).contains(book));
    }

}