package db.bookstore.services;

import db.bookstore.dao.Author;
import db.bookstore.dao.Book;
import db.bookstore.dao.BookstoreDao;
import db.bookstore.serializers.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vio on 18.04.2015.
 */
@Service
public class SerializationService {

    @Autowired
    private BookstoreDao dao;

    @Autowired
    private List<Serializer<Book>> bookSerializers;

    @Autowired
    private List<Serializer<Author>> authorSerializers;
}
