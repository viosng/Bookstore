package db.bookstore.dao;

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
public class BookstoreDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SimpleJdbcInsert bookInsert, authorInsert, authorityInsert;

    @PostConstruct
    private void init() {
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

    @NotNull
    public List<Author> getAllAuthors() {
        return jdbcTemplate.query("SELECT * FROM AUTHORS", authorRowMapper("ID", "NAME", "BIRTH_DATE", "DEATH_DATE"));
    }

    @Nullable
    public Author getAuthor(@NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate) {
        String query;
        if (deathDate != null) {
            query = "SELECT * FROM AUTHORS WHERE NAME = ? AND BIRTH_DATE = ? AND DEATH_DATE = ?";
        } else {
            query = "SELECT * FROM AUTHORS WHERE NAME = ? AND BIRTH_DATE = ?";
        }
        List<Author> authors =
                jdbcTemplate.query(query, ps -> {
                    ps.setString(1, name);
                    ps.setDate(2, new Date(birthDate.getMillis()));
                    if (deathDate != null) {
                        ps.setDate(3, new Date(deathDate.getMillis()));
                    }
                }, authorRowMapper("ID", "NAME", "BIRTH_DATE", "DEATH_DATE"));

        return authors.isEmpty() ? null : authors.get(0);
    }


    @NotNull
    public Author addAuthor(@NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate) {
        Author author = getAuthor(name, birthDate, deathDate);
        if (author != null) {
            return author;
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("NAME", name);
        parameters.put("BIRTH_DATE", new Date(birthDate.getMillis()));
        if (deathDate != null) {
            parameters.put("DEATH_DATE", new Date(deathDate.getMillis()));
        }
        int id = authorInsert.execute(parameters);
        return new DefaultAuthor(id, name, birthDate, deathDate);
    }

    @NotNull
    public List<Book> getAllBooks() {
        return getBooks("");
    }

    public @NotNull List<Book> getBooksOfAuthor(@NotNull Author author) {
        return getBooks(" WHERE BOOKS.ID IN (SELECT BOOK_ID FROM AUTHORITY WHERE AUTHOR_ID = " + author.getId() + ")");
    }

    private List<Book> getBooks(String whereClause) {
        String query =
                "SELECT AUTHORS.ID AID, AUTHORS.NAME ANAME, AUTHORS.BIRTH_DATE, AUTHORS.DEATH_DATE, " +
                        " BOOKS.ID BID, BOOKS.NAME BNAME, BOOKS.PRICE, BOOKS.PUBLICATION_DATE FROM " +
                        "    AUTHORS INNER JOIN AUTHORITY " +
                        "        ON AUTHORS.ID = AUTHORITY.AUTHOR_ID " +
                        "        INNER JOIN BOOKS " +
                        "            ON BOOKS.ID = AUTHORITY.BOOK_ID ";
        query += whereClause;
        RowMapper<Author> authorRowMapper =
                authorRowMapper("AID", "ANAME", "BIRTH_DATE", "DEATH_DATE");
        List<Pair<Book, Author>> resultSets = jdbcTemplate.query(query, (rs, rowNum) -> {
            Book book = new DefaultBook(
                    rs.getInt("BID"),
                    rs.getString("BNAME"),
                    rs.getDouble("PRICE"),
                    new DateTime(rs.getDate("PUBLICATION_DATE")),
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

    @Nullable
    public Book getBook(@NotNull String name, double price, @NotNull DateTime publicationDate) {
        String query =
                "SELECT AUTHORS.ID AID, AUTHORS.NAME NAME, AUTHORS.BIRTH_DATE BD, AUTHORS.DEATH_DATE DD, BOOKS.ID FROM " +
                        "    AUTHORS INNER JOIN AUTHORITY " +
                        "        ON AUTHORS.ID = AUTHORITY.AUTHOR_ID " +
                        "        INNER JOIN BOOKS " +
                        "            ON BOOKS.ID = AUTHORITY.BOOK_ID " +
                        "WHERE BOOKS.NAME = ? AND PRICE = ? AND PUBLICATION_DATE = ?";
        RowMapper<Author> authorRowMapper = authorRowMapper("AID", "NAME", "BD", "DD");
        List<Pair<Integer, Author>> dbAuthors =
                jdbcTemplate.query(query, ps -> {
                    ps.setString(1, name);
                    ps.setDouble(2, price);
                    ps.setDate(3, new Date(publicationDate.getMillis()));
                }, (rs, i) -> new Pair<>(rs.getInt(5), authorRowMapper.mapRow(rs, i)));

        Set<Author> authors;
        if (dbAuthors.isEmpty()) {
            return null;
        } else {
            authors = dbAuthors.stream().map(Pair::getValue).collect(Collectors.toSet());
        }
        return new DefaultBook(dbAuthors.get(0).getFirst(), name, price, publicationDate, authors);
    }

    @NotNull
    public Book addBook(@NotNull String name, double price, @NotNull DateTime publicationDate) {
        Book book = getBook(name, price, publicationDate);
        if (book != null) {
            return book;
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("NAME", name);
        parameters.put("PUBLICATION_DATE", publicationDate.toDate());
        parameters.put("PRICE", price);
        int id = bookInsert.execute(parameters);
        return new DefaultBook(id, name, price, publicationDate, Collections.<Author>emptySet());
    }

    public void addAuthority(@NotNull Author author, @NotNull Book book) {
        boolean hasAuthority = jdbcTemplate.query("SELECT * FROM AUTHORITY WHERE AUTHOR_ID = ? AND BOOK_ID = ?", ps -> {
            ps.setInt(1, author.getId());
            ps.setInt(2, book.getId());
        }, (rs, i) -> rs).size() > 0;
        if (hasAuthority)  {
            return;
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("BOOK_ID", book.getId());
        parameters.put("AUTHOR_ID", author.getId());
        authorityInsert.execute(parameters);
    }

}
