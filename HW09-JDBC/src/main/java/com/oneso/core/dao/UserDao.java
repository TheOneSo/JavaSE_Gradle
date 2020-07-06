package com.oneso.core.dao;

import com.oneso.core.model.UserJdbc;
import com.oneso.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface UserDao {
  Optional<UserJdbc> findById(long id);

  long insertUser(UserJdbc userJdbc);

  void updateUser(UserJdbc userJdbc);

  void insertOrUpdate(UserJdbc userJdbc);

  SessionManager getSessionManager();
}
