package db.bookstore.services.impl;

import db.bookstore.dao.Author;
import db.bookstore.dao.Book;
import db.bookstore.dao.BookstoreDao;
import db.bookstore.serializers.impl.BookJsonSerializer;
import db.bookstore.services.SerializationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by vio on 18.04.2015.
 */
@Service
public class SerializationServiceImpl implements SerializationService {
    private final static Logger log = LoggerFactory.getLogger(SerializationServiceImpl.class);

    @Autowired
    private BookstoreDao dao;

    @Value("${data-json-file}")
    private String DATA_JSON;

    @Autowired
    private BookJsonSerializer jsonSerializer;

    @Scheduled(fixedDelay = 60000)
    @Override
    public void dump() {
        log.info("Dump task is started");
        jsonSerializer.serialize(dao.getAllBooks(), DATA_JSON);
        log.info("Dump task is finished");
    }

    private <T> Map<T, T> getFromDatabase(Collection<T> elements, Function<T, T> getterFromDatabase) {
        Map<T, T> resultMap = new HashMap<>();
        for (T element : elements) {
            resultMap.put(element, getterFromDatabase.apply(element));
        }
        return resultMap;
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "bookCache", allEntries = true),
                    @CacheEvict(value = "authorCache", allEntries = true),
            }
    )
    public void importFromJson() {
        List<Book> deserializedBooks = jsonSerializer.deserialize(DATA_JSON);
        if (deserializedBooks.isEmpty()) {
            throw new IllegalStateException("Dump is empty! Revert changes");
        }
        dao.clearData();
        Map<Author, Author> dbAuthors = getFromDatabase(
                deserializedBooks.stream().flatMap(b -> b.getAuthors().stream()).collect(Collectors.toSet()),
                new Function<Author, Author>() {
                    @Override
                    public Author apply(Author t) {
                        return dao.addAuthor(t.getName(), t.getBirthDate(), t.getDeathDate());
                    }
                });
        Map<Book, Book> dbBooks= getFromDatabase(deserializedBooks,
                t -> dao.addBook(t.getName(), t.getPrice(), t.getPublicationDate(), Collections.emptyList()));
        deserializedBooks.forEach(b -> b.getAuthors().forEach(a -> dao.addAuthority(dbAuthors.get(a), dbBooks.get(b))));

    }


}
