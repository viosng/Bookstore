package db.bookstore.serializers.impl;

import db.bookstore.dao.Book;
import db.bookstore.serializers.Serializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by vio on 18.04.2015.
 */
@Component
public class BookReadableSerializer implements Serializer<Book> {
    @Override
    public void serialize(@NotNull Iterable<Book> elements, @NotNull String fileName) {

    }

    @NotNull
    @Override
    public List<Book> deserialize(@NotNull String fileName) {
        return null;
    }
}
