package db.bookstore.services;

import db.bookstore.dao.Author;
import db.bookstore.dao.Book;
import db.bookstore.dao.BookstoreDao;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by StudentDB on 21.04.2015.
 */
public interface BookstoreService {

    void setDao(BookstoreDao dao);

    @NotNull
    Author addAuthor(@NotNull String name, @NotNull DateTime birthDate);

    @NotNull
    Author addAuthor(@NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate);

    @NotNull
    List<Author> getAllAuthors();

    @NotNull
    List<Author> findAuthorsByNamePrefix(@NotNull String namePrefix);

    @Nullable
    Author getAuthor(int id);

    void updateAuthor(int id, @NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate);

    void deleteAuthor(int id);

    void addAuthority(@NotNull Author author, @NotNull Book book);

    void deleteAuthority(@NotNull Author author, @NotNull Book book);

    @NotNull
    Book addBook(@NotNull String name, double price, @NotNull DateTime publicationDate,
                 @NotNull Author author, @NotNull Author... authors);

    @NotNull
    Book addBook(@NotNull String name, double price, @NotNull DateTime publicationDate, @NotNull List<Author> authors);

    @NotNull
    List<Book> getAllBooks();

    @NotNull
    List<Book> findBooksByNamePrefix(@NotNull String namePrefix);

    @Nullable
    Book getBook(int id);

    void updateBook(int id, @NotNull String name, double price, @NotNull DateTime publicationDate);

    void deleteBook(int id);

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
