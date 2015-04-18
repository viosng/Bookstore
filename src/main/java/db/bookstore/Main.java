package db.bookstore;

import db.bookstore.configs.MainJavaConfig;
import db.bookstore.services.BookstoreService;
import org.joda.time.DateTime;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by vio on 18.04.2015.
 */
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainJavaConfig.class);
        BookstoreService service = context.getBean(BookstoreService.class);
        service.getAllAuthors();
        service.getBooksOfAliveAuthors();
        service.addAuthor("V. Petrov", DateTime.parse("1988-01-01"));
        service.getAllAuthors();
    }
}
