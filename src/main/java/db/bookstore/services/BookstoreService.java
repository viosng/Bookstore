package db.bookstore.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import db.bookstore.dao.Author;
import db.bookstore.dao.Book;
import db.bookstore.dao.BookstoreDao;
import db.bookstore.dao.DefaultAuthor;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.lang.ref.WeakReference;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by vio on 18.04.2015.
 */
@Service
public class BookstoreService {

    @Autowired
    private BookstoreDao dao;

    private LoadingCache<Author, Author> authorCache;
    private WeakReference<List<Author>> allAuthorsList;

    @PostConstruct
    private void init() {
        authorCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .softValues()
                .build(new CacheLoader<Author, Author>() {
                    @Override
                    public Author load(@NotNull Author fictiveAuthor) throws Exception {
                        return dao.addAuthor(fictiveAuthor.getName(), fictiveAuthor.getBirthDate(), fictiveAuthor.getDeathDate());
                    }
                });
    }

    @NotNull
    public Author addAuthor(@NotNull String name, @NotNull DateTime birthDate) {
        return addAuthor(name, birthDate, null);
    }

    @NotNull
    public Author addAuthor(@NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate) {
        birthDate = birthDate.withTime(0, 0, 0, 0);
        deathDate = deathDate == null ? null : deathDate.withTime(0, 0, 0, 0);
        return authorCache.getUnchecked(new DefaultAuthor(0, name, birthDate, deathDate));
    }

    @NotNull
    public List<Author> getAllAuthors() {
        List<Author> allAuthors = allAuthorsList == null ? null : allAuthorsList.get();
        if (allAuthors == null) {
            allAuthors = dao.getAllAuthors();
            allAuthorsList = new WeakReference<>(allAuthors);
        }
        return allAuthors;
    }

    @Nullable
    public Book getBook(@NotNull String name, double price, @NotNull DateTime publicationDate) {
        return dao.getBook(name, price, publicationDate);
    }

    public Book addBook(@NotNull String name, double price, @NotNull DateTime publicationDate,
                        @NotNull Author author, @NotNull Author... authors) {
        return addBook(name, price, publicationDate, Lists.asList(author, authors));
    }

    public Book addBook(@NotNull String name, double price, @NotNull DateTime publicationDate, @NotNull List<Author> authors) {
        if (authors.isEmpty()) {
            throw new IllegalArgumentException("Empty authors list");
        }
        return addBookToDao(name, price, publicationDate, authors);
    }

    private Book addBookToDao(@NotNull String name, double price, @NotNull DateTime publicationDate, @NotNull List<Author> authors) {
        return dao.addBook(name, price, publicationDate, authors);
    }

    public void addAuthority(@NotNull Author author, @NotNull Book book) {
        dao.addAuthority(author, book);
    }

    public @NotNull List<Book> getAllBooks() {
        return dao.getAllBooks();
    }

    public @NotNull List<Book> getBooksOfAuthor(@NotNull Author author) {
        return dao.getBooksOfAuthor(author);
    }

    public @NotNull List<Book> getBooksWithPriceMoreThan(double price) {
        return dao.getBooksWithPriceMoreThan(price);
    }

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
