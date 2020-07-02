package com.oneso.core.dao;

import com.oneso.core.model.User;

import java.util.Optional;

public interface UserDao extends Dao {

  Optional<User> findById(long id);

  long insertUser(User user);

  void updateUser(User user);

  void insertOrUpdate(User user);
}
