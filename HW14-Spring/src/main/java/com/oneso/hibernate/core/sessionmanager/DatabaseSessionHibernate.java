package com.oneso.hibernate.core.sessionmanager;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseSessionHibernate implements DatabaseSession {

  private final Session session;
  private final Transaction transaction;

  public DatabaseSessionHibernate(Session session) {
    this.session = session;
    this.transaction = session.beginTransaction();
  }

  public Session getSession() {
    return session;
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public void close() {
    if(transaction.isActive()) {
      transaction.commit();
    }
    session.close();
  }
}
