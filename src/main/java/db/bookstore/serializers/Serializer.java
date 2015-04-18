package db.bookstore.serializers;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by vio on 18.04.2015.
 */
public interface Serializer<T> {
    void serialize(@NotNull Iterable<T> elements, @NotNull String fileName);

    @NotNull
    List<T> deserialize(@NotNull String fileName);
}
