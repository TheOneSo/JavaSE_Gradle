package com.oneso.hibernate.core.dao;

import com.oneso.hibernate.core.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends Dao {

  Optional<User> findById(long id);

  List<User> findAll();

  long insertUser(User user);

  void updateUser(User user);

  void insertOrUpdate(User user);
}
