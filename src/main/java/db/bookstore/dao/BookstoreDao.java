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

    @Nullable
    Author getAuthor(int id);

    @NotNull
    Author addAuthor(@NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate);

    void deleteAuthor(int id);

    @NotNull
    List<Book> getAllBooks();

    @Nullable
    Book getBook(int id);

    void deleteBook(int id);

    @NotNull
    List<Book> getBooksOfAuthor(@NotNull Author author);

    @NotNull
    List<Book> getBooksWithPriceMoreThan(double price);

    @NotNull
    List<Book> getBooksWithPriceLessThan(double price);

    @NotNull
    Book addBook(@NotNull String name, double price, @NotNull DateTime publicationDate, @NotNull Iterable<Author> authors);

    void addAuthority(@NotNull Author author, @NotNull Book book);

    void deleteAuthority(@NotNull Author author, @NotNull Book book);
}
