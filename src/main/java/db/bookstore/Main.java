package db.bookstore;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import db.bookstore.configs.MainJavaConfig;
import db.bookstore.dao.Book;
import db.bookstore.serializers.impl.BookJsonSerializer;
import db.bookstore.services.BookstoreService;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * Created by vio on 18.04.2015.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainJavaConfig.class)) {
            BookstoreService service = context.getBean(BookstoreService.class);
            System.out.println("Start using bookstore");
            DateTime now = DateTime.parse("1998-01-01");
            String newBorn = "NewBorn" + DateTime.now().getMinuteOfDay();
            System.out.println(service.addAuthor(newBorn, now, null));
            System.out.println(service.addAuthor(newBorn, now, null));

            System.out.println(service.getAllAuthors());
            System.out.println(service.getAllAuthors());

            System.out.println(service.getAllBooks());
            List<Book> allBooks = service.getAllBooks();
            System.out.println(allBooks);

            BookJsonSerializer serializer = context.getBean(BookJsonSerializer.class);
            serializer.serialize(allBooks, "books.json");
            List<Book> deserializedBooks = serializer.deserialize("books.json");
            System.out.println(deserializedBooks);
            System.out.println(deserializedBooks.equals(allBooks));
            Thread.sleep(1000000);
        }
    }
}
