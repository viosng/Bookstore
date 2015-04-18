package db.bookstore.services;

/**
 * Created by vio on 18.04.2015.
 */
public interface Filter<T> {
    boolean filter(T element);
}
