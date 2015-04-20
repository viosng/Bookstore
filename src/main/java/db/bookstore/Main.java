package db.bookstore;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import db.bookstore.configs.MainJavaConfig;
import db.bookstore.services.BookstoreService;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by vio on 18.04.2015.
 */
public class Main {
    public static void main(String[] args) {
        Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainJavaConfig.class)) {
            BookstoreService service = context.getBean(BookstoreService.class);
            System.out.println("Start using bookstore");
            DateTime now = DateTime.parse("1998-01-01");
            String newBorn = "NewBorn" + DateTime.now().getMinuteOfDay();
            System.out.println(service.addAuthor(newBorn, now, null));
            System.out.println(service.addAuthor(newBorn, now, null));
            System.out.println(service.addAuthor(newBorn, now, null));
            System.out.println(service.getAllAuthors());
            System.out.println(service.getAllAuthors());
            System.out.println(service.getAllAuthors());
        }
    }
}
