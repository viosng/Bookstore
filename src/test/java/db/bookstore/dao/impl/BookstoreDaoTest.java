package db.bookstore.dao.impl;

import db.bookstore.TestJavaConfig;
import db.bookstore.dao.BookstoreDao;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

/**
 * Created by StudentDB on 21.04.2015.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestJavaConfig.class)
public class BookstoreDaoTest {

    @Autowired
    private BookstoreDao dao;

    @Test
    public void testGetAllAuthors() throws Exception {

    }

    @Test
    public void testGetAuthor() throws Exception {

    }

    @Test(expected = DuplicateKeyException.class)
    public void testAddAuthor() throws Exception {
        dao.addAuthor("author1", new DateTime("2000-04-04"), null);
    }

    @Test
    public void testGetAllBooks() throws Exception {

    }

    @Test
    public void testGetBook() throws Exception {

    }

    @Test
    public void testGetBooksOfAuthor() throws Exception {

    }

    @Test
    public void testGetBooksWithPriceMoreThan() throws Exception {

    }

    @Test
    public void testGetBooksWithPriceLessThan() throws Exception {

    }

    @Test(expected = DuplicateKeyException.class)
    public void testAddBook() throws Exception {
        dao.addBook("test1", 30.0, new DateTime("2015-04-14"), Collections.emptyList());
    }

    @Test
    public void testAddAuthority() throws Exception {

    }
}