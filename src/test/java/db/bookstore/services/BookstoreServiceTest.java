package db.bookstore.services;

import db.bookstore.dao.Author;
import db.bookstore.dao.Book;
import db.bookstore.dao.BookstoreDao;
import db.bookstore.dao.impl.DefaultBook;
import db.configs.MainJavaConfig;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Created by StudentDB on 21.04.2015.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainJavaConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookstoreServiceTest {

    @Autowired
    private BookstoreService service;

    private BookstoreDao dao;

    @Before
    public void setUp() throws Exception {
        dao = mock(BookstoreDao.class);
        service.setDao(dao);
        doReturn(Collections.emptyList()).when(dao).getAllBooks();
    }

    @Test
    public void testGetBookCache() throws Exception {
        System.out.println(service.getBook(1));
        System.out.println(service.getBook(1));
        System.out.println(service.getBook(1));
        verify(dao, only()).getBook(anyInt());
    }

    @Test
    public void testDeleteBookCache() throws Exception {
        System.out.println(service.getBook(1));
        System.out.println(service.getBook(1));
        service.deleteBook(1);
        System.out.println(service.getBook(1));
        verify(dao, times(2)).getBook(anyInt());
    }

    @Test
    public void testAuthorityDeleteCache() throws Exception {
        Book book = new DefaultBook(1, "a", 1.0, DateTime.now(), Collections.emptySet());
        System.out.println(service.getBook(1));
        System.out.println(service.getBook(1));
        service.addAuthority(mock(Author.class), book);
        System.out.println(service.getBook(1));
        System.out.println(service.getBook(1));
        service.deleteAuthority(mock(Author.class), book);
        System.out.println(service.getBook(1));
        System.out.println(service.getBook(1));
        verify(dao, times(3)).getBook(anyInt());
    }

    @Test
    public void testGetAuthorCache() throws Exception {
        System.out.println(service.getAuthor(1));
        System.out.println(service.getAuthor(1));
        System.out.println(service.getAuthor(1));
        verify(dao, only()).getAuthor(anyInt());
    }

    @Test
    public void testDeleteAuthorCache() throws Exception {
        System.out.println(service.getAuthor(1));
        System.out.println(service.getAuthor(1));
        service.deleteAuthor(1);
        System.out.println(service.getAuthor(1));
        System.out.println(service.getAuthor(1));
        verify(dao, times(2)).getAuthor(anyInt());
    }
}