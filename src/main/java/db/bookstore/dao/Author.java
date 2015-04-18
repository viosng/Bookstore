package db.bookstore.dao;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import javax.annotation.Nullable;

/**
 * Created by vio on 18.04.2015.
 */
public interface Author {

    int getId();

    @NotNull
    String getName();

    @NotNull
    DateTime getBirthDate();

    @Nullable
    DateTime getDeathDate();
}
