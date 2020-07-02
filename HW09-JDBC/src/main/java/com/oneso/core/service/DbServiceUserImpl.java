package com.oneso.core.service;

import com.oneso.core.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.oneso.core.model.UserJdbc;

import java.util.Optional;

public class DbServiceUserImpl implements DBServiceUser {
  private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

  private final UserDao userDao;

  public DbServiceUserImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public long saveUser(UserJdbc userJdbc) {
    try (var sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        userDao.insertUser(userJdbc);
        sessionManager.commitSession();

        logger.info("created user: {}", userJdbc.id);
        return userJdbc.id;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }

  @Override
  public Optional<UserJdbc> getUser(long id) {
    try (var sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        Optional<UserJdbc> userOptional = userDao.findById(id);

        logger.info("user: {}", userOptional.orElse(null));
        return userOptional;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
      }
      return Optional.empty();
    }
  }
}
