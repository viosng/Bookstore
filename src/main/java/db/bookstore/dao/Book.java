package db.bookstore.dao;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import java.util.Set;

/**
 * Created by vio on 18.04.2015.
 */
public interface Book {

    int getId();

    @NotNull
    String getName();

    double getPrice();

    @NotNull
    DateTime getPublicationDate();

    @NotNull
    Set<Author> getAuthors();

}
