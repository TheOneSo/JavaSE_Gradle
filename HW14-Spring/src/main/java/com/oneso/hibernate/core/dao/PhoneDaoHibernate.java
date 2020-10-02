package com.oneso.hibernate.core.dao;

import com.oneso.hibernate.exceptions.DaoException;
import com.oneso.hibernate.core.model.Phone;
import com.oneso.hibernate.core.sessionmanager.SessionManager;
import com.oneso.hibernate.core.sessionmanager.SessionManagerHibernate;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PhoneDaoHibernate implements PhoneDao {

  private static final Logger logger = LoggerFactory.getLogger(PhoneDaoHibernate.class);

  private final SessionManagerHibernate sessionManager;

  public PhoneDaoHibernate(SessionManagerHibernate sessionManager) {
    this.sessionManager = sessionManager;
  }

  @Override
  public Optional<Phone> findById(long id) {
    try {
      return Optional.ofNullable(sessionManager.getCurrentSession().getSession().find(Phone.class, id));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
  }

  @Override
  public long insertPhone(Phone phone) {
    try {
      Session session = sessionManager.getCurrentSession().getSession();
      session.persist(phone);
      session.flush();
      return phone.getId();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new DaoException(e);
    }
  }

  @Override
  public void updatePhone(Phone phone) {
    try {
      Session session = sessionManager.getCurrentSession().getSession();
      session.merge(phone);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new DaoException(e);
    }
  }

  @Override
  public void insertOrUpdate(Phone phone) {
    try {
      Session session = sessionManager.getCurrentSession().getSession();
      if(phone.getId() > 0) {
        session.merge(phone);
      } else {
        session.persist(phone);
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
