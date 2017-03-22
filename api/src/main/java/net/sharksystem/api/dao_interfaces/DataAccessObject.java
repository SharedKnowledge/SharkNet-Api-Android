package net.sharksystem.api.dao_interfaces;

import java.util.List;

/**
 * Created by j4rvis on 3/22/17.
 */

public interface DataAccessObject<T, S> {
    List<T> getAll();
    T get(S id);
    void update(T object);
    void remove(T object);
    void add(T object);
}
