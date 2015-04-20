package db.bookstore.dao;

import com.google.common.base.Objects;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import javax.annotation.Nullable;

/**
 * Created by vio on 18.04.2015.
 */
public class DefaultAuthor implements Author {

    private final int id;

    @NotNull
    private final String name;

    @NotNull
    private final DateTime birthDate;

    @Nullable
    private final DateTime deathDate;

    @Nullable
    private Integer hashCode;

    public DefaultAuthor(int id, @NotNull String name, @NotNull DateTime birthDate, @Nullable DateTime deathDate) {
        if (deathDate != null && birthDate.isAfter(deathDate)) {
            throw new IllegalArgumentException("DeathDate should be after birthDate");
        }
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
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

    @NotNull
    @Override
    public DateTime getBirthDate() {
        return birthDate;
    }

    @Nullable
    @Override
    public DateTime getDeathDate() {
        return deathDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultAuthor that = (DefaultAuthor) o;
        return Objects.equal(name, that.name) &&
                Objects.equal(birthDate, that.birthDate) &&
                Objects.equal(deathDate, that.deathDate);
    }

    @Override
    public int hashCode() {
        return hashCode == null ? hashCode = Objects.hashCode(name, birthDate, deathDate) : hashCode;
    }

    @Override
    public String toString() {
        return "DefaultAuthor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", deathDate=" + deathDate +
                '}';
    }
}
