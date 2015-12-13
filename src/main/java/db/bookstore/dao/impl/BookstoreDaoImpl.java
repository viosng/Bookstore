package db.bookstore.dao.impl;

import db.bookstore.dao.Author;
import db.bookstore.dao.Book;
import db.bookstore.dao.BookstoreDao;
import org.apache.commons.math3.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by vio on 18.04.2015.
 */
@Repository
public class BookstoreDaoImpl implements BookstoreDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SimpleJdbcInsert bookInsert, authorInsert, authorityInsert;

    @PostConstruct
    private void init() {
        System.out.println("master");
        bookInsert = bookInsert
                .withTableName("BOOKS")
                .usingColumns("NAME", "PUBLICATION_DATE", "PRICE")
                .usingGeneratedKeyColumns("ID");
        authorInsert = authorInsert
                .withTableName("AUTHORS")
                .usingColumns("NAME", "BIRTH_DATE", "DEATH_DATE")
                .usingGeneratedKeyColumns("ID");
        authorityInsert = authorityInsert
                .withTableName("AUTHORITY")
                .usingColumns("BOOK_ID", "AUTHOR_ID")
                .usingGeneratedKeyColumns("ID");
    }

    private static RowMapper<Author> authorRowMapper(String idField, String nameField, String birthDateField, String deathDateField) {
        return (resultSet, i) -> {
            if (resultSet.getString(nameField) != null) {
                DateTime birthDate = new DateTime(resultSet.getDate(birthDateField));
                Date dbDeathDate = resultSet.getDate(deathDateField);
                DateTime deathDate = dbDeathDate != null ? new DateTime(dbDeathDate) : null;
                return new DefaultAuthor(resultSet.getInt(idField), resultSet.getString(nameField), birthDate, deathDate);
            } else {
                return null;
            }
        };
    }

    @Override
    @NotNull
    public List<Author> getAllAuthors() {
        return getAuthors("");
    }

    @Nullable
    @Override
    public Author getAuthor(int id) {
        List<Author> authors = getAuthors(" WHERE ID = " + id);
        return authors.isEmpty() ? null : authors.get(0);
    }

    @NotNull
    @Override
    public List<Author> findAuthorsByNamePrefix(@NotNull String namePrefix) {
        return jdbcTemplate.query("SELECT * FROM AUTHORS WHERE NAME LIKE ?",
                new Object[]{namePrefix + "%"},
                authorRowMapper("ID", "NAME", "BIRTH_DATE", "DEATH_DATE"));
    }

    @NotNull
    private List<Author> getAuthors(@NotNull String wherePart) {
        return jdbcTemplate.query("SELECT * FROM AUTHORS " + wherePart, authorRowMapper("ID", "NAME", "BIRTH_DATE", "DEATH_DATE"));
    }

    @Override
    @NotNull
    public Author addAuthor(@NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("NAME", name);
        parameters.put("BIRTH_DATE", new Date(birthDate.getMillis()));
        if (deathDate != null) {
            parameters.put("DEATH_DATE", new Date(deathDate.getMillis()));
        }
        int id = authorInsert.executeAndReturnKey(parameters).intValue();
        return new DefaultAuthor(id, name, birthDate, deathDate);
    }

    @Override
    public void updateAuthor(int id, @NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate) {
        jdbcTemplate.update("UPDATE AUTHORS SET NAME = ?, BIRTH_DATE = ?, DEATH_DATE = ? WHERE ID = ?",
                name, new Date(birthDate.getMillis()), deathDate != null ? new Date(deathDate.getMillis()) : null, id);
    }

    @Override
    public void deleteAuthor(int id) {
        jdbcTemplate.execute("DELETE FROM AUTHORS WHERE ID = " + id);
    }

    @Override
    @NotNull
    public List<Book> getAllBooks() {
        return getBooks("SELECT * FROM BOOKSTORE_VIEW");
    }

    @Nullable
    @Override
    public Book getBook(int id) {
        List<Book> books = getBooks("SELECT * FROM BOOKSTORE_VIEW WHERE BOOK_ID = ?", id);
        return books.isEmpty() ? null : books.get(0);
    }

    @NotNull
    @Override
    public List<Book> findBooksByNamePrefix(@NotNull String namePrefix) {
        return getBooks("SELECT * FROM BOOKSTORE_VIEW WHERE BOOK_NAME LIKE ?", namePrefix + "%");
    }

    @Override
    public void updateBook(int id, @NotNull String name, double price, @NotNull DateTime publicationDate) {
        jdbcTemplate.update("UPDATE BOOKS SET NAME = ?, PRICE = ?, PUBLICATION_DATE = ? WHERE ID = ?",
                name, price, new Date(publicationDate.getMillis()), id);
    }

    @Override
    public void deleteBook(int id) {
        jdbcTemplate.execute("DELETE FROM AUTHORITY WHERE BOOK_ID = " + id);
        jdbcTemplate.execute("DELETE FROM BOOKS WHERE ID = " + id);
    }

    @Override
    public @NotNull List<Book> getBooksOfAuthor(@NotNull Author author) {
        return getBooks(
                "SELECT * FROM BOOKSTORE_VIEW WHERE BOOK_ID IN (SELECT BOOK_ID FROM AUTHORITY WHERE AUTHOR_ID = ?)",
                author.getId());
    }

    @Override
    public @NotNull List<Book> getBooksWithPriceMoreThan(double price) {
        return getBooks("SELECT * FROM BOOKSTORE_VIEW WHERE BOOK_PRICE > ?", price);
    }

    @Override
    public @NotNull List<Book> getBooksWithPriceLessThan(double price) {
        return getBooks("SELECT * FROM BOOKSTORE_VIEW WHERE BOOK_PRICE < ?", price);
    }

    private List<Book> getBooks(String query, Object... args) {
        RowMapper<Author> authorRowMapper =
                authorRowMapper("AUTHOR_ID", "AUTHOR_NAME", "AUTHOR_BIRTH_DATE", "AUTHOR_DEATH_DATE");
        List<Pair<Book, Author>> resultSets = jdbcTemplate.query(query, args, (rs, rowNum) -> {
            Book book = new DefaultBook(
                    rs.getInt("BOOK_ID"),
                    rs.getString("BOOK_NAME"),
                    rs.getDouble("BOOK_PRICE"),
                    new DateTime(rs.getDate("BOOK_PUBLICATION_DATE")),
                    Collections.<Author>emptySet());
            return new Pair<>(book, authorRowMapper.mapRow(rs, 0));
        });

        Map<Book, Set<Author>> bookSetMap = resultSets.stream().collect(Collectors.groupingBy(new Function<Pair<Book, Author>, Book>() {
            @Override
            public Book apply(Pair<Book, Author> t) {
                return t.getKey();
            }
        }, Collectors.mapping(Pair::getValue, Collectors.toSet())));
        return bookSetMap.entrySet().stream().map(e ->
                new DefaultBook(
                        e.getKey().getId(),
                        e.getKey().getName(),
                        e.getKey().getPrice(),
                        e.getKey().getPublicationDate(),
                        e.getValue())).collect(Collectors.toList());
    }

    @Override
    @NotNull
    public Book addBook(@NotNull String name, double price, @NotNull DateTime publicationDate, @NotNull Iterable<Author> authors) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("NAME", name);
        parameters.put("PUBLICATION_DATE", publicationDate.toDate());
        parameters.put("PRICE", price);
        int id = bookInsert.executeAndReturnKey(parameters).intValue();
        Book book = new DefaultBook(id, name, price, publicationDate.withTime(0, 0, 0, 0), authors);
        for (Author author : authors) {
            addAuthority(author, book);
        }
        return book;
    }

    @Override
    public void addAuthority(@NotNull Author author, @NotNull Book book) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("BOOK_ID", book.getId());
        parameters.put("AUTHOR_ID", author.getId());
        authorityInsert.execute(parameters);
    }

    @Override
    public void deleteAuthority(@NotNull Author author, @NotNull Book book) {
        jdbcTemplate.execute("DELETE FROM AUTHORITY WHERE BOOK_ID = " + book.getId() + " AND AUTHOR_ID = " + author.getId());
    }

    @Override
    public void clearData() {
        jdbcTemplate.execute("DELETE FROM AUTHORITY");
        jdbcTemplate.execute("DELETE FROM BOOKS");
        jdbcTemplate.execute("DELETE FROM AUTHORS");
    }
}
