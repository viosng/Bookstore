package db.bookstore.services;

import com.google.common.collect.Lists;
import db.bookstore.dao.Author;
import db.bookstore.dao.Book;
import db.bookstore.dao.BookstoreDao;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by vio on 18.04.2015.
 */
@Service
public class BookstoreService {

    @Component
    public static class CacheHelper {

        @Autowired
        private BookstoreDao dao;

        @Nullable
        @Cacheable(value = "authorsCache")
        private Author getAuthorWithNormalizedDates(@NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate) {
            return dao.getAuthor(name, birthDate, deathDate);
        }

        @Cacheable(value = "authorsCache")
        @CacheEvict(value = "authorsCache", key = "'all'.hashCode()")
        private Author addAuthorToDao(@NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate) {
            return dao.addAuthor(name, birthDate, deathDate);
        }
    }

    @Autowired
    private BookstoreDao dao;

    @Autowired
    private CacheHelper cacheHelper;

    @Nullable
    @Cacheable(value = "authorsCache")
    public Author getAuthor(@NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate) {
        return cacheHelper.getAuthorWithNormalizedDates(name,
                birthDate.withTime(0, 0, 0, 0),
                deathDate == null ? null : deathDate.withTime(0, 0, 0, 0));
    }

    @NotNull
    public Author addAuthor(@NotNull String name, @NotNull DateTime birthDate) {
        return addAuthor(name, birthDate, null);
    }

    @NotNull
    public Author addAuthor(@NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate) {
        birthDate = birthDate.withTime(0, 0, 0, 0);
        deathDate = deathDate == null ? null : deathDate.withTime(0, 0, 0, 0);
        Author author = cacheHelper.getAuthorWithNormalizedDates(name, birthDate, deathDate);
        if (author != null) {
            return author;
        }
        return cacheHelper.addAuthorToDao(name, birthDate, deathDate);
    }


    @Cacheable(value = "authorsCache", key = "'all'.hashCode()")
    public @NotNull List<Author> getAllAuthors() {
        return dao.getAllAuthors();
    }

    @Nullable
    public Book getBook(@NotNull String name, double price, @NotNull DateTime publicationDate) {
        return dao.getBook(name, price, publicationDate);
    }

    public Book addBook(@NotNull String name, double price, @NotNull DateTime publicationDate,
                        @NotNull Author author, @NotNull Author... authors) {
        return addBook(name, price, publicationDate, Lists.asList(author, authors));
    }

    @Caching(cacheable = {
            @Cacheable(value = "booksCache", key = "T(java.util.Objects).hash('book', #name, #price, #publicationDate, #authors)"),
            @Cacheable(value = "booksCache", key = "T(java.util.Objects).hash('book', #name, #price, #publicationDate)")
    })
    public Book addBook(@NotNull String name, double price, @NotNull DateTime publicationDate, @NotNull List<Author> authors) {
        if (authors.isEmpty()) {
            throw new IllegalArgumentException("Empty authors list");
        }
        return addBookToDao(name, price, publicationDate, authors);
    }


    @Caching(
            evict = {
                    @CacheEvict(value = "booksCache", key = "'all'"),
                    @CacheEvict(value = "booksPriceCache", allEntries = true),
                    @CacheEvict(value = "booksCache", key = "T(java.util.Objects).hash('book', #name, #price, #publicationDate, #authors)")
            })
    private Book addBookToDao(@NotNull String name, double price, @NotNull DateTime publicationDate, @NotNull List<Author> authors) {
        return dao.addBook(name, price, publicationDate, authors);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "booksCache", key = "'all'"),
                    @CacheEvict(value = "booksPriceCache", allEntries = true),
                    @CacheEvict(value = "booksCache",
                            key = "T(java.util.Objects).hash('book', #book.getName(), #book.getPrice(), " +
                                    "#book.getPublicationDate())"),
                    @CacheEvict(value = "booksCache",
                            key = "T(java.util.Objects).hash('book', #book.getName(), #book.getPrice(), " +
                                    "#book.getPublicationDate(), #book.getAuthors())"),
            })
    public void addAuthority(@NotNull Author author, @NotNull Book book) {
        dao.addAuthority(author, book);
    }

    @Cacheable(value = "booksCache", key = "'all'")
    public @NotNull List<Book> getAllBooks() {
        return dao.getAllBooks();
    }

    @Cacheable(value="booksCache", key="'booksOfAuthor' + #author.toString()")
    public @NotNull List<Book> getBooksOfAuthor(@NotNull Author author) {
        return dao.getBooksOfAuthor(author);
    }

    @Cacheable(value="booksPriceCache", key="'booksPriceMoreThan' + #price.toString()")
    public @NotNull List<Book> getBooksWithPriceMoreThan(double price) {
        return dao.getBooksWithPriceMoreThan(price);
    }

    @Cacheable(value="booksPriceCache", key="'booksPriceLessThan' + #price.toString()")
    public @NotNull List<Book> getBooksWithPriceLessThan(double price) {
        return dao.getBooksWithPriceLessThan(price);
    }

    private int getAge(DateTime birthDate, DateTime deathDate) {
        return Years.yearsBetween(birthDate, deathDate == null ? DateTime.now() : deathDate).getYears();
    }

    private @NotNull List<Book> getFilteredAuthorsByAge(Comparator<Integer> ageComparator, int age) {
        return getAllBooks().stream().filter(book ->
                book.getAuthors().stream().allMatch(author ->
                        ageComparator.compare(getAge(author.getBirthDate(), author.getDeathDate()), age) > 0))
                .collect(Collectors.toList());
    }

    public @NotNull List<Book> getBooksOfAuthorsOlderThan(int age) {
        return getFilteredAuthorsByAge(Comparator.<Integer>naturalOrder(), age);
    }

    public @NotNull List<Book> getBooksOfAuthorsYoungerThan(int age) {
        return getFilteredAuthorsByAge(Comparator.<Integer>reverseOrder(), age);
    }

    public @NotNull List<Book> getBooksOfAliveAuthors() {
        return getAllBooks().stream().filter(book ->
                book.getAuthors().stream().anyMatch(author ->
                        author.getDeathDate() == null)).collect(Collectors.toList());
    }

    public @NotNull List<Book> getFilteredBooks(@NotNull Predicate<Book> bookFilter) {
        return getAllBooks().stream().filter(bookFilter).collect(Collectors.toList());
    }

    public @NotNull List<Author> getFilteredAuthors(@NotNull Predicate<Author> authorFilter) {
        return getAllAuthors().stream().filter(authorFilter).collect(Collectors.toList());
    }

}
