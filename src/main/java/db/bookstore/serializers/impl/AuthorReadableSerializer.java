package db.bookstore.serializers.impl;

import db.bookstore.dao.Author;
import db.bookstore.serializers.Serializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by vio on 18.04.2015.
 */
@Component
public class AuthorReadableSerializer implements Serializer<Author> {
    @Override
    public void serialize(@NotNull Iterable<Author> elements, @NotNull String fileName) {

    }

    @NotNull
    @Override
    public List<Author> deserialize(@NotNull String fileName) {
        return null;
    }
}
