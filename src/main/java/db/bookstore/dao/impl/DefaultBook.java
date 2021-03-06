package db.bookstore.dao.impl;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import db.bookstore.dao.Author;
import db.bookstore.dao.Book;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by vio on 18.04.2015.
 */
public class DefaultBook implements Book, Serializable {

    private static final long serialVersionUID = 4682314345277970962L;

    private final int id;

    @NotNull
    private final String name;

    @NotNull
    private final DateTime publicationDate;

    private final double price;

    @NotNull
    private final Set<Author> authors;

    @Nullable
    private Integer hashCode;

    public DefaultBook(int id, @NotNull String name, double price, @NotNull DateTime publicationDate, @NotNull Iterable<Author> authors) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.publicationDate = publicationDate;
        this.authors = ImmutableSet.copyOf(authors);
    }

    @Override
    public int getId() {
        return id;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @NotNull
    @Override
    public DateTime getPublicationDate() {
        return publicationDate;
    }

    @NotNull
    @Override
    public Set<Author> getAuthors() {
        return authors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultBook that = (DefaultBook) o;
        return Objects.equal(price, that.price) &&
                Objects.equal(name, that.name) &&
                Objects.equal(publicationDate, that.publicationDate);
    }

    @Override
    public int hashCode() {
        return hashCode == null ? hashCode = Objects.hashCode(name, publicationDate, price) : hashCode;
    }

    @Override
    public String toString() {
        return "DefaultBook{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", publicationDate=" + publicationDate +
                ", price=" + price +
                ", authors=" + authors +
                '}';
    }
}
