package db.bookstore.services;

import db.bookstore.dao.Author;
import db.bookstore.dao.Book;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by StudentDB on 21.04.2015.
 */
public interface BookstoreService {

    @NotNull
    Author addAuthor(@NotNull String name, @NotNull DateTime birthDate);

    @NotNull
    Author addAuthor(@NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate);

    @NotNull
    List<Author> getAllAuthors();

    @Nullable
    Author getAuthor(int id);

    @NotNull
    Book addBook(@NotNull String name, double price, @NotNull DateTime publicationDate,
                 @NotNull Author author, @NotNull Author... authors);

    @NotNull
    Book addBook(@NotNull String name, double price, @NotNull DateTime publicationDate, @NotNull List<Author> authors);

    void addAuthority(@NotNull Author author, @NotNull Book book);

    @NotNull
    List<Book> getAllBooks();

    @Nullable
    Book getBook(int id);

    @NotNull
    List<Book> getBooksOfAuthor(@NotNull Author author);

    @NotNull
    List<Book> getBooksWithPriceMoreThan(double price);

    @NotNull
    List<Book> getBooksWithPriceLessThan(double price);

    @NotNull
    List<Book> getBooksOfAuthorsOlderThan(int age);

    @NotNull
    List<Book> getBooksOfAuthorsYoungerThan(int age);

    @NotNull
    List<Book> getBooksOfAliveAuthors();
}
