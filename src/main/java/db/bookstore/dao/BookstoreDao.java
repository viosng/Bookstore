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
    List<Author> findAuthorsByNamePrefix(@NotNull String namePrefix);

    @NotNull
    Author addAuthor(@NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate);

    void updateAuthor(int id, @NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate);

    void deleteAuthor(int id);

    @NotNull
    List<Book> getAllBooks();

    @Nullable
    Book getBook(int id);

    @NotNull
    List<Book> findBooksByNamePrefix(@NotNull String namePrefix);

    void updateBook(int id, @NotNull String name, double price, @NotNull DateTime publicationDate);

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

    void clearData();
}
