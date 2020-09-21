package com.oneso;

import com.oneso.hwhibernate.core.dao.UserDao;
import com.oneso.hwhibernate.core.model.Address;
import com.oneso.hwhibernate.core.model.Phone;
import com.oneso.hwhibernate.core.model.User;
import com.oneso.hwhibernate.core.service.ServiceUserImpl;
import com.oneso.hwhibernate.hibernate.HibernateUtils;
import com.oneso.hwhibernate.hibernate.dao.UserDaoHibernate;
import com.oneso.hwhibernate.hibernate.sessionmanager.SessionManagerHibernate;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TestCacheSpeed should")
class TestCacheSpeed {

  private User user;
  private UserDao userDao;

  @BeforeEach
  void setUp() {
    SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate-test.cfg.xml",
        User.class, Address.class, Phone.class);

    SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
    userDao = new UserDaoHibernate(sessionManager);
    user = createUser();
  }

  @Test
  @DisplayName("Should test a speed of getting user")
  void shouldTestSpeedOfGetUser() {
    // without cache
    long startTime = System.currentTimeMillis();
    ServiceUserImpl dbServiceUser = new ServiceUserImpl(userDao, false);
    long id = dbServiceUser.saveUser(user);
    dbServiceUser.getUser(id);

    long stopTime = System.currentTimeMillis();
    long withoutCache = stopTime - startTime;

    // with cache
    startTime = System.currentTimeMillis();
    dbServiceUser = new ServiceUserImpl(userDao, true);
    id = dbServiceUser.saveUser(user);
    dbServiceUser.getUser(id);

    stopTime = System.currentTimeMillis();
    long withCache = stopTime - startTime;

    assertTrue(withoutCache > withCache);
  }

  private User createUser() {
    User user = new User();
    Address address = new Address(0, "street #123", user);
    Phone phone = new Phone(0, "phone #1", user);
    user.setName("John");
    user.setAddress(address);
    user.setPhones(List.of(phone));
    return user;
  }
}
