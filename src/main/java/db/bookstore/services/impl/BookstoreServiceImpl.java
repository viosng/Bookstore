package db.bookstore.services.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import db.bookstore.dao.*;
import db.bookstore.dao.impl.DefaultAuthor;
import db.bookstore.dao.impl.DefaultBook;
import db.bookstore.services.BookstoreService;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class BookstoreServiceImpl implements BookstoreService {

    @Autowired
    private BookstoreDao dao;

    private LoadingCache<Author, Author> authorCache;
    private WeakReference<List<Author>> allAuthorsList;

    private LoadingCache<Book, Book> bookCache;
    private WeakReference<List<Book>> allBooksList;

    @Value("${authorCacheMaximumSize:100}")
    private int authorCacheMaximumSize;

    @Value("${authorExpireAfterMinutes:10}")
    private int authorExpireAfterMinutes;

    @Value("${authorCacheMaximumSize:100}")
    private int bookCacheMaximumSize;

    @Value("${bookExpireAfterMinutes:10}")
    private int bookExpireAfterMinutes;

    @PostConstruct
    private void init() {
        authorCache = CacheBuilder.newBuilder()
                .maximumSize(authorCacheMaximumSize)
                .expireAfterWrite(authorExpireAfterMinutes, TimeUnit.MINUTES)
                .softValues()
                .build(new CacheLoader<Author, Author>() {
                    @Override
                    public Author load(@NotNull Author fictiveAuthor) throws Exception {
                        return dao.addAuthor(fictiveAuthor.getName(), fictiveAuthor.getBirthDate(), fictiveAuthor.getDeathDate());
                    }
                });
        bookCache = CacheBuilder.newBuilder()
                .maximumSize(bookCacheMaximumSize)
                .expireAfterWrite(bookExpireAfterMinutes, TimeUnit.MINUTES)
                .softValues()
                .build(new CacheLoader<Book, Book>() {
                    @Override
                    public Book load(@NotNull Book fictiveBook) throws Exception {
                        return dao.addBook(fictiveBook.getName(), fictiveBook.getPrice(),
                                fictiveBook.getPublicationDate(), fictiveBook.getAuthors());
                    }
                });
    }

    @Override
    @NotNull
    public Author addAuthor(@NotNull String name, @NotNull DateTime birthDate) {
        return addAuthor(name, birthDate, null);
    }

    @Override
    @NotNull
    public Author addAuthor(@NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate) {
        birthDate = birthDate.withTime(0, 0, 0, 0);
        deathDate = deathDate == null ? null : deathDate.withTime(0, 0, 0, 0);
        return authorCache.getUnchecked(new DefaultAuthor(0, name, birthDate, deathDate));
    }

    @Override
    @NotNull
    public List<Author> getAllAuthors() {
        List<Author> allAuthors = allAuthorsList == null ? null : allAuthorsList.get();
        if (allAuthors == null) {
            allAuthors = dao.getAllAuthors();
            allAuthorsList = new WeakReference<>(allAuthors);
        }
        return allAuthors;
    }

    @Override
    public Book addBook(@NotNull String name, double price, @NotNull DateTime publicationDate,
                        @NotNull Author author, @NotNull Author... authors) {
        return addBook(name, price, publicationDate, Lists.asList(author, authors));
    }

    @Override
    public Book addBook(@NotNull String name, double price, @NotNull DateTime publicationDate, @NotNull List<Author> authors) {
        if (authors.isEmpty()) {
            throw new IllegalArgumentException("Empty authors list");
        }
        return bookCache.getUnchecked(new DefaultBook(0, name, price, publicationDate, authors));
    }

    @Override
    public void addAuthority(@NotNull Author author, @NotNull Book book) {
        dao.addAuthority(author, book);
        bookCache.refresh(book);
        allBooksList = null;
    }

    @Override
    public @NotNull List<Book> getAllBooks() {
        List<Book> allBooks = allBooksList == null ? null : allBooksList.get();
        if (allBooks == null) {
            allBooks = dao.getAllBooks();
            allBooksList = new WeakReference<>(allBooks);
        }
        return allBooks;
    }

    @Override
    public @NotNull List<Book> getBooksOfAuthor(@NotNull Author author) {
        return dao.getBooksOfAuthor(author);
    }

    @Override
    public @NotNull List<Book> getBooksWithPriceMoreThan(double price) {
        return dao.getBooksWithPriceMoreThan(price);
    }

    @Override
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

    @Override
    public @NotNull List<Book> getBooksOfAuthorsOlderThan(int age) {
        return getFilteredAuthorsByAge(Comparator.<Integer>naturalOrder(), age);
    }

    @Override
    public @NotNull List<Book> getBooksOfAuthorsYoungerThan(int age) {
        return getFilteredAuthorsByAge(Comparator.<Integer>reverseOrder(), age);
    }

    @Override
    public @NotNull List<Book> getBooksOfAliveAuthors() {
        return getAllBooks().stream().filter(book ->
                book.getAuthors().stream().anyMatch(author ->
                        author.getDeathDate() == null)).collect(Collectors.toList());
    }

    @Override
    public @NotNull List<Book> getFilteredBooks(@NotNull Predicate<Book> bookFilter) {
        return getAllBooks().stream().filter(bookFilter).collect(Collectors.toList());
    }

    @Override
    public @NotNull List<Author> getFilteredAuthors(@NotNull Predicate<Author> authorFilter) {
        return getAllAuthors().stream().filter(authorFilter).collect(Collectors.toList());
    }

}
