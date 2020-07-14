package com.oneso.core.dao;

import com.oneso.core.model.User;
import com.oneso.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface UserDao {
  Optional<User> findById(long id);

  long insertUser(User user);

  void updateUser(User user);

  void insertOrUpdate(User user);

  SessionManager getSessionManager();
}
