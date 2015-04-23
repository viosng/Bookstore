package db.bookstore.services;

import db.bookstore.dao.Book;
import db.bookstore.dao.BookstoreDao;
import db.configs.MainJavaConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by StudentDB on 23.04.2015.
 */

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainJavaConfig.class)
@ActiveProfiles("test")
public class SerializationServiceTest {

    @Autowired
    private BookstoreDao dao;

    @Autowired
    private SerializationService serializationService;

    @Test
    @Transactional
    @Rollback
    public void testImport() throws Exception {
        List<Book> allBooks = dao.getAllBooks();
        serializationService.importFromJson();
        List<Book> allBooks1 = dao.getAllBooks();
        assertEquals(allBooks, allBooks1);
    }
}
