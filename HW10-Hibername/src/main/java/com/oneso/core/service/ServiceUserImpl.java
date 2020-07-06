package com.oneso.core.service;

import com.oneso.core.dao.UserDao;
import com.oneso.core.model.User;
import com.oneso.core.sessionmanager.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ServiceUserImpl implements ServiceUser {

  private static final Logger logger = LoggerFactory.getLogger(ServiceUserImpl.class);

  private final UserDao userDao;

  public ServiceUserImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public long saveUser(User user) {
    try (SessionManager sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();

      try {
        userDao.insertOrUpdate(user);
        long id = user.getId();
        sessionManager.commitSession();
        logger.info("Create user: {}", id);
        return id;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new ServiceException(e);
      }
    }
  }

  @Override
  public Optional<User> getUser(long id) {
    try(SessionManager sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();

      try {
        Optional<User> userOptional = userDao.findById(id);
        logger.info("User: {}", userOptional.orElse(null));
        return userOptional;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new ServiceException(e);
      }
    }
  }
}
