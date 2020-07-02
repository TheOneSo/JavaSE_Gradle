package com.oneso.hibernate.dao;

import com.oneso.core.dao.DaoException;
import com.oneso.core.dao.UserDao;
import com.oneso.core.model.User;
import com.oneso.core.sessionmanager.SessionManager;
import com.oneso.hibernate.sessionmanager.DatabaseSessionHibernate;
import com.oneso.hibernate.sessionmanager.SessionManagerHibernate;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UserDaoHibernate implements UserDao {

  private static final Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

  private final SessionManagerHibernate sessionManager;

  public UserDaoHibernate(SessionManagerHibernate sessionManager) {
    this.sessionManager = sessionManager;
  }

  @Override
  public Optional<User> findById(long id) {
    DatabaseSessionHibernate sessionHibernate = sessionManager.getCurrentSession();
    try {
      return Optional.ofNullable(sessionHibernate.getSession().find(User.class, id));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
  }

  @Override
  public long insertUser(User user) {
    DatabaseSessionHibernate sessionHibernate = sessionManager.getCurrentSession();
    try {
      Session session = sessionHibernate.getSession();
      session.persist(user);
      session.flush();
      return user.getId();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new DaoException(e);
    }
  }

  @Override
  public void updateUser(User user) {
    DatabaseSessionHibernate sessionHibernate = sessionManager.getCurrentSession();
    try {
      Session session = sessionHibernate.getSession();
      session.merge(user);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new DaoException(e);
    }
  }

  @Override
  public void insertOrUpdate(User user) {
    DatabaseSessionHibernate sessionHibernate = sessionManager.getCurrentSession();
    try {
      Session session = sessionHibernate.getSession();
      if(user.getId() > 0) {
        session.merge(user);
      } else {
        session.persist(user);
        session.flush();
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new DaoException(e);
    }
  }

  @Override
  public SessionManager getSessionManager() {
    return sessionManager;
  }
}
