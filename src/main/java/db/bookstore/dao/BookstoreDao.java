package db.bookstore.dao;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by StudentDB on 21.04.2015.
 */
public interface BookstoreDao {
    @NotNull
    List<Author> getAllAuthors();

    @NotNull
    Author addAuthor(@NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate);

    @NotNull
    List<Book> getAllBooks();

    @NotNull
    List<Book> getBooksOfAuthor(@NotNull Author author);

    @NotNull
    List<Book> getBooksWithPriceMoreThan(double price);

    @NotNull
    List<Book> getBooksWithPriceLessThan(double price);

    @NotNull
    Book addBook(@NotNull String name, double price, @NotNull DateTime publicationDate, @NotNull Iterable<Author> authors);

    void addAuthority(@NotNull Author author, @NotNull Book book);
}
