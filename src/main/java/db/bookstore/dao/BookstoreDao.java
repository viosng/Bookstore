package db.bookstore.dao;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static RowMapper<Author> authorRowMapper() {
        return (resultSet, i) -> {
            DateTime birthDate = new DateTime(resultSet.getDate("BIRTH_DATE"));
            Date dbDeathDate = resultSet.getDate("DEATH_DATE");
            DateTime deathDate = dbDeathDate != null ? new DateTime(dbDeathDate) : null;
            return new DefaultAuthor(resultSet.getInt("ID"), resultSet.getString("NAME"), birthDate, deathDate);
        };
    }

    @NotNull
    public List<Author> getAllAuthors() {
        return jdbcTemplate.query("SELECT * FROM AUTHORS", authorRowMapper());
    }

    @NotNull
    public Author addAuthor(@NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate) {
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
            ps.setDate(3, deathDate != null ? new Date(birthDate.getMillis()) : null);
        }, authorRowMapper());

        if (!authors.isEmpty()) {
            return authors.get(0);
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("NAME", name);
        parameters.put("BIRTH_DATE", birthDate.toDate());
        if (deathDate != null) {
            parameters.put("DEATH_DATE", name);
        }
        int id = authorInsert.execute(parameters);
        return new DefaultAuthor(id, name, birthDate, deathDate);
    }

    @NotNull
    public Book addBook(@NotNull String name, double price, @NotNull DateTime publicationDate) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("NAME", name);
        parameters.put("PUBLICATION_DATE", publicationDate.toDate());
        parameters.put("PRICE", price);
        int id = bookInsert.execute(parameters);
        return new DefaultBook(id, name, price, publicationDate, Collections.<Author>emptySet());
    }

    public void addAuthority(@NotNull Author author, @NotNull Book book) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("BOOK_ID", book.getId());
        parameters.put("AUTHOR_ID", author.getId());
        authorityInsert.execute(parameters);
    }

}
