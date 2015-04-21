package db.bookstore.services.impl;

import db.bookstore.dao.Book;
import db.bookstore.dao.BookstoreDao;
import db.bookstore.serializers.Serializer;
import db.bookstore.services.SerializationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vio on 18.04.2015.
 */
@Service
public class SerializationServiceImpl implements SerializationService {

    private final static Logger log = LoggerFactory.getLogger(SerializationServiceImpl.class);

    @Autowired
    private BookstoreDao dao;

    @Autowired
    private List<Serializer<Book>> bookSerializers;

    @Scheduled(fixedDelay = 600000)
    public void dump() {
        log.info("Dump task is started");
        List<Book> allBooks = dao.getAllBooks();
        bookSerializers.parallelStream().forEach(s -> s.serialize(allBooks, s.getClass().getSimpleName() + "-dump"));
        log.info("Dump task is finished");
    }
}
