package com.oneso.jdbc.mapper;

public interface JdbcMapper<T> {
  long insert(T objectData);

  void update(T objectData);

  void insertOrUpdate(T objectData);

  T findById(long id, Class<T> clazz);
}
