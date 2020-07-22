package com.oneso.whhibernate.hibernate.sessionmanager;

import com.oneso.whhibernate.core.sessionmanager.SessionManager;
import com.oneso.whhibernate.core.sessionmanager.SessionManagerException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class SessionManagerHibernate implements SessionManager {

  private DatabaseSessionHibernate databaseSession;
  private final SessionFactory sessionFactory;

  public SessionManagerHibernate(SessionFactory sessionFactory) {
    if(sessionFactory == null) {
      throw new SessionManagerException("Session Factory is null");
    }

    this.sessionFactory = sessionFactory;
  }

  @Override
  public void beginSession() {
    try {
      databaseSession = new DatabaseSessionHibernate(sessionFactory.openSession());
    } catch (Exception e) {
      throw new SessionManagerException(e);
    }
  }

  @Override
  public void commitSession() {
    checkSessionAndTransaction();
    try {
      databaseSession.getTransaction().commit();
      databaseSession.getSession().close();
    } catch (Exception e) {
      throw new SessionManagerException(e);
    }
  }

  @Override
  public void rollbackSession() {
    checkSessionAndTransaction();
    try {
      databaseSession.getTransaction().rollback();
      databaseSession.getSession().close();
    } catch (Exception e) {
      throw new SessionManagerException(e);
    }
  }

  @Override
  public void close() {
    if(databaseSession == null) {
      return;
    }

    Session session = databaseSession.getSession();
    if(session == null || !session.isConnected()) {
      return;
    }

    Transaction transaction = databaseSession.getTransaction();
    if(transaction == null || !transaction.isActive()) {
      return;
    }

    try {
      databaseSession.close();
      databaseSession = null;
    } catch (Exception e) {
      throw new SessionManagerException(e);
    }
  }

  @Override
  public DatabaseSessionHibernate getCurrentSession() {
    checkSessionAndTransaction();
    return databaseSession;
  }

  private void checkSessionAndTransaction() {
    if(databaseSession == null) {
      throw new SessionManagerException("DatabaseSession not opened");
    }

    Session session = databaseSession.getSession();
    if(session == null || !session.isConnected()) {
      throw new SessionManagerException("Session not opened");
    }

    Transaction transaction = databaseSession.getTransaction();
    if(transaction == null || !transaction.isActive()) {
      throw new SessionManagerException("Transaction not opened");
    }

  }
}
