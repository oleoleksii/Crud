package com.bymdev.service;

import java.util.List;

/**
 * Created by oleksii on 17.12.16.
 */

public interface GenericService<T> {
    void create(T product);
    T read(Long id);
    List<T> readAll();
    void update(T product);
    void delete(T product);
    boolean isExist(T product);
}
