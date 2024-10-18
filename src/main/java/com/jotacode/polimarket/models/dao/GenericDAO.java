package com.jotacode.polimarket.models.dao;

import java.util.List;

public interface GenericDAO<T> {

    void create(T entity);

    void edit(T entity) throws Exception;

    void destroy(Long id) throws Exception;

    T find(Long id);

    List<T> findAll();

    List<T> findEntities(int maxResults, int firstResult);

    int getCount();
}
