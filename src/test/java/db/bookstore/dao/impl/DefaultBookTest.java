package db.bookstore.dao.impl;

import db.bookstore.dao.Author;
import db.bookstore.dao.Book;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by StudentDB on 21.04.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultBookTest {

    private DateTime publicationDate;

    private List<Author> authors;

    private Book book;

    @Before
    public void setUp() throws Exception {
        publicationDate = new DateTime("2000-01-01");
        authors = Arrays.asList(mock(Author.class), mock(Author.class), mock(Author.class));
        book = new DefaultBook(1, "name", 30.0, publicationDate, authors);
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(1, book.getId());
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("name", book.getName());
    }

    @Test
    public void testGetPrice() throws Exception {
        assertEquals(30.0, book.getPrice(), 0.001);
    }

    @Test
    public void testGetPublicationDate() throws Exception {
        assertEquals(publicationDate, book.getPublicationDate());
    }

    @Test
    public void testGetAuthors() throws Exception {
        assertArrayEquals(authors.toArray(), book.getAuthors().toArray());
    }
}