package com.oneso.hwhibernate.core.service;

import com.oneso.cache.HwListener;
import com.oneso.cache.MyCache;
import com.oneso.hwhibernate.core.dao.UserDao;
import com.oneso.hwhibernate.core.model.User;
import com.oneso.hwhibernate.core.sessionmanager.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ServiceUserImpl implements ServiceUser {

  private static final Logger logger = LoggerFactory.getLogger(ServiceUserImpl.class);

  private final MyCache<Long, User> cache = new MyCache<>();
  HwListener<Long, User> listener = new HwListener<Long, User>() {
    @Override
    public void notify(Long key, User value, String action) {
      logger.info("User id: {}, Info: {}, action: {}", key, value, action);
    }
  };

  private final UserDao userDao;
  private final boolean useCache;

  public ServiceUserImpl(UserDao userDao, boolean useCache) {
    this.userDao = userDao;
    this.useCache = useCache;

    if(useCache) {
      cache.addListener(listener);
    }
  }

  @Override
  public long saveUser(User user) {
    try (SessionManager sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();

      try {
        userDao.insertOrUpdate(user);
        long id = user.getId();

        if(useCache) {
          cache.put(id, user);
        }

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
    if(useCache) {
      User user = cache.get(id);
      if(user != null) {
        return Optional.of(user);
      }
    }

    try(SessionManager sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();

      try {
        Optional<User> userOptional = userDao.findById(id);
        if(useCache) {
          cache.put(id, userOptional.orElse(null));
        }

        logger.info("User: {}", userOptional.orElse(null));
        return userOptional;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new ServiceException(e);
      }
    }
  }

  public void clearListener() {
    cache.removeListener(listener);
  }
}
