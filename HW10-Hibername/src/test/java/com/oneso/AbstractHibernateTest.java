package com.oneso;

import com.oneso.core.model.Address;
import com.oneso.core.model.Phone;
import com.oneso.core.model.User;
import com.oneso.hibernate.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class AbstractHibernateTest {
  private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate-test.cfg.xml";

  protected SessionFactory sessionFactory;

  @BeforeEach
  public void setUp() {
    sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML_FILE_RESOURCE, User.class, Address.class, Phone.class);
  }

  @AfterEach
  void tearDown() {
    sessionFactory.close();
  }

  protected void save(Object obj) {
    try (Session session = sessionFactory.openSession()) {
      save(session, obj);
    }
  }

  protected void save(Session session, Object obj) {
    session.beginTransaction();
    session.save(obj);
    session.getTransaction().commit();
  }

  protected Object load(long id, Class<?> clazz) {
    try (Session session = sessionFactory.openSession()) {
      return session.find(clazz, id);
    }
  }

  protected EntityStatistics getUserStatistics() {
    Statistics stats = sessionFactory.getStatistics();
    return stats.getEntityStatistics(User.class.getName());
  }
}
