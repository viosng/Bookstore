package db;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import db.bookstore.dao.Author;
import db.bookstore.dao.Book;
import db.bookstore.services.BookstoreService;
import db.configs.MainJavaConfig;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.AbstractEnvironment;

import java.util.List;

/**
 * Created by vio on 18.04.2015.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.DEBUG);
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "dev");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainJavaConfig.class)) {
            BookstoreService service = context.getBean(BookstoreService.class);
            System.out.println("Start using bookstore");

            System.out.println(service.getAllAuthors());
            System.out.println(service.getBooksOfAliveAuthors());

            List<Author> allAuthors = service.getAllAuthors();
            System.out.println(allAuthors);

            System.out.println(service.getAllBooks());
            List<Book> allBooks = service.getAllBooks();
            System.out.println(allBooks);
            System.out.println("bbbb");
            Author author = service.addAuthor("Burn" + System.currentTimeMillis(), DateTime.now());
            System.out.println(service.getAuthor(author.getId()));
            System.out.println(service.getAuthor(author.getId()));
            System.out.println(2);
            System.out.println(service.getAllAuthors());
            System.out.println(service.getAllAuthors());
            System.out.println(3);
            System.out.println(service.getAllBooks());
            System.out.println(service.getAllBooks());

        }
    }
}
