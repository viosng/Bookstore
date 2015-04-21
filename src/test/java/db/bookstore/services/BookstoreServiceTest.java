package db.bookstore.services;

import db.configs.MainJavaConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by StudentDB on 21.04.2015.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainJavaConfig.class)
@ActiveProfiles("test")
public class BookstoreServiceTest {

    @Autowired
    private BookstoreService service;

    @Test
    public void testAllBooks() throws Exception {
        System.out.println(service.getAllBooks());
    }
}