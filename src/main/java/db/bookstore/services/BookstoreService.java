package db.bookstore.services;

import db.bookstore.dao.*;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vio on 18.04.2015.
 */
@Service
public class BookstoreService {

    @Autowired
    private BookstoreDao dao;

    public Author addAuthor(@NotNull String name, @NotNull DateTime birthDate) {
        return dao.addAuthor(name, birthDate, null);
    }

    public Author addAuthor(@NotNull String name, @NotNull DateTime birthDate, @NotNull DateTime deathDate) {
        return dao.addAuthor(name, birthDate, deathDate);
    }

    public Book addBook(@NotNull String name, double price, @NotNull DateTime publicationDate) {
        return dao.addBook(name, price, publicationDate);
    }

    public void addAuthority(@NotNull Author author, @NotNull Book book) {
        dao.addAuthority(author, book);
    }

    public @NotNull List<Book> getAllBooks() {
        return Collections.emptyList();
    }

    public @NotNull List<Book> getBooksWithPriceMoreThan(double price) {
        return Collections.emptyList();
    }

    public @NotNull List<Book> getBooksOfAuthorsOlderThan(int age) {
        return Collections.emptyList();
    }

    public @NotNull List<Book> getBooksOfAuthorsYoungerThan(int age) {
        return Collections.emptyList();
    }

    public @NotNull List<Book> getBooksOfAliveAuthors() {
        return Collections.emptyList();
    }

    public @NotNull List<Book> getFilteredBooks(@NotNull Filter<Book> bookFilter) {
        return Collections.<Book>emptyList().stream().filter(bookFilter::filter).collect(Collectors.toList());
    }

    public @NotNull List<Author> getAllAuthors() {
        return dao.getAllAuthors();
    }

    public @NotNull List<Author> getFilteredAuthors(@NotNull Filter<Author> authorFilter) {
        return Collections.<Author>emptyList().stream().filter(authorFilter::filter).collect(Collectors.toList());
    }

}
