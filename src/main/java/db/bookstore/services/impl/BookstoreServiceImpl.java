package db.bookstore.services.impl;

import com.google.common.collect.Lists;
import db.bookstore.dao.Author;
import db.bookstore.dao.Book;
import db.bookstore.dao.BookstoreDao;
import db.bookstore.services.BookstoreService;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vio on 18.04.2015.
 */
@Service
@Transactional
public class BookstoreServiceImpl implements BookstoreService {

    private BookstoreDao dao;

    @Override
    @Autowired
    public void setDao(BookstoreDao dao) {
        this.dao = dao;
    }

    @Override
    @NotNull
    public Author addAuthor(@NotNull String name, @NotNull DateTime birthDate) {
        return dao.addAuthor(name, birthDate.withTime(0, 0, 0, 0), null);
    }

    @Override
    @NotNull
    public Author addAuthor(@NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate) {
        birthDate = birthDate.withTime(0, 0, 0, 0);
        deathDate = deathDate == null ? null : deathDate.withTime(0, 0, 0, 0);
        return dao.addAuthor(name, birthDate, deathDate);
    }

    @Override
    @NotNull
    public List<Author> getAllAuthors() {
        return dao.getAllAuthors();
    }

    @NotNull
    @Override
    public List<Author> findAuthorsByNamePrefix(@NotNull String namePrefix) {
        return dao.findAuthorsByNamePrefix(namePrefix);
    }

    @Nullable
    @Override
    @Cacheable(value = "authorCache")
    public Author getAuthor(int id) {
        return dao.getAuthor(id);
    }

    @Override
    @CacheEvict(value = "authorCache", key = "#id")
    public void updateAuthor(int id, @NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate) {
        dao.updateAuthor(id, name, birthDate, deathDate);
    }

    @Override
    @CacheEvict(value = "authorCache")
    public void deleteAuthor(int id) {
        dao.deleteAuthor(id);
    }

    @Override
    @NotNull
    public Book addBook(@NotNull String name, double price, @NotNull DateTime publicationDate,
                        @NotNull Author author, @NotNull Author... authors) {
        return dao.addBook(name, price, publicationDate, Lists.asList(author, authors));
    }

    @Override
    @NotNull
    public Book addBook(@NotNull String name, double price, @NotNull DateTime publicationDate, @NotNull List<Author> authors) {
        if (authors.isEmpty()) {
            throw new IllegalArgumentException("Empty authors list");
        }
        return dao.addBook(name, price, publicationDate, authors);
    }

    @Override
    @CacheEvict(value = "bookCache", key = "#book.getId()")
    public void addAuthority(@NotNull Author author, @NotNull Book book) {
        dao.addAuthority(author, book);
    }

    @Override
    @CacheEvict(value = "bookCache", key = "#book.getId()")
    public void deleteAuthority(@NotNull Author author, @NotNull Book book) {
        dao.deleteAuthority(author, book);
    }

    @Override
    public @NotNull List<Book> getAllBooks() {
        return dao.getAllBooks();
    }

    @NotNull
    @Override
    public List<Book> findBooksByNamePrefix(@NotNull String namePrefix) {
        return dao.findBooksByNamePrefix(namePrefix);
    }

    @Nullable
    @Override
    @Cacheable(value = "bookCache")
    public Book getBook(int id) {
        return dao.getBook(id);
    }

    @Override
    @CacheEvict(value = "bookCache", key = "#id")
    public void updateBook(int id, @NotNull String name, double price, @NotNull DateTime publicationDate) {
        dao.updateBook(id, name, price, publicationDate);
    }

    @Override
    @CacheEvict(value = "bookCache")
    public void deleteBook(int id) {
        dao.deleteBook(id);
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
}
