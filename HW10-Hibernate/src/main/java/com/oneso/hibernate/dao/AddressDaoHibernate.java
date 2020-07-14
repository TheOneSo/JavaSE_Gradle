package com.oneso.hibernate.dao;

import com.oneso.core.dao.AddressDao;
import com.oneso.core.dao.DaoException;
import com.oneso.core.model.Address;
import com.oneso.core.sessionmanager.SessionManager;
import com.oneso.hibernate.sessionmanager.SessionManagerHibernate;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AddressDaoHibernate implements AddressDao {

  private static final Logger logger = LoggerFactory.getLogger(AddressDaoHibernate.class);

  private SessionManagerHibernate sessionManager;

  public AddressDaoHibernate(SessionManagerHibernate sessionManager) {
    this.sessionManager = sessionManager;
  }

  @Override
  public Optional<Address> findById(long id) {
    try {
      return Optional.ofNullable(sessionManager.getCurrentSession().getSession().find(Address.class, id));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
  }

  @Override
  public long insertAddress(Address address) {
    try {
      Session session = sessionManager.getCurrentSession().getSession();
      session.persist(address);
      session.flush();
      return address.getId();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new DaoException(e);
    }
  }

  @Override
  public void updateAddress(Address address) {
    try {
      Session session = sessionManager.getCurrentSession().getSession();
      session.merge(address);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new DaoException(e);
    }
  }

  @Override
  public void insertOrUpdate(Address address) {
    try {
      Session session = sessionManager.getCurrentSession().getSession();
      if(address.getId() > 0) {
        session.merge(address);
      } else {
        session.persist(address);
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
