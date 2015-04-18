package db.bookstore;

import db.bookstore.configs.MainJavaConfig;
import db.bookstore.dao.Author;
import db.bookstore.dao.Book;
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
        Author author1 = service.addAuthor("V. Petrov", DateTime.parse("1988-01-01"));
        Author author = service.addAuthor("E. Ivanov", DateTime.parse("1968-01-01"), DateTime.parse("1998-01-01"));
        Book book = service.addBook("Main book", 30.0, DateTime.parse("1999-01-01"));
        service.addAuthority(author, book);
        service.addAuthority(author1, book);
        book = service.addBook("Main book", 30.0, DateTime.parse("1999-01-01"));
    }
}
