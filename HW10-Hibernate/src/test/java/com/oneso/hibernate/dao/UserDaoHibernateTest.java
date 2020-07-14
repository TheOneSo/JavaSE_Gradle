package com.oneso.hibernate.dao;

import com.oneso.AbstractHibernateTest;
import com.oneso.core.dao.UserDao;
import com.oneso.core.model.User;
import com.oneso.hibernate.sessionmanager.SessionManagerHibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserDaoHibernate should do")
@ExtendWith(MockitoExtension.class)
class UserDaoHibernateTest extends AbstractHibernateTest {

  private SessionManagerHibernate sessionManager;
  private UserDao userDao;

  @BeforeEach
  public void setUp() {
    super.setUp();
    sessionManager = new SessionManagerHibernate(sessionFactory);
    userDao = new UserDaoHibernate(sessionManager);
  }

  @Test
  @DisplayName("should find and get an user from DB")
  void findById() {
    User user = new User(1, "test", null, null);
    save(user);

    assertThat(user.getId()).isGreaterThan(0);

    sessionManager.beginSession();
    Optional<User> mayBeUser = userDao.findById(user.getId());
    sessionManager.commitSession();

    assertEquals("test", mayBeUser.get().getName());
  }

  @Test
  @DisplayName("should save an user into DB")
  void shouldSaveNewAddress() {
    User expectedUser = new User(0, "name", null, null);
    sessionManager.beginSession();
    userDao.insertOrUpdate(expectedUser);
    long id = expectedUser.getId();
    sessionManager.commitSession();

    assertThat(id).isGreaterThan(0);

    User actualUser = (User) load(id, User.class);
    assertEquals("name", actualUser.getName());

    expectedUser = new User(id, "Doesnt name", null, null);
    sessionManager.beginSession();
    userDao.insertOrUpdate(expectedUser);
    long newId = expectedUser.getId();
    sessionManager.commitSession();

    assertThat(newId).isGreaterThan(0).isEqualTo(id);
    actualUser = (User) load(newId, User.class);
    assertEquals("Doesnt name", actualUser.getName());
  }

  @Test
  void getSessionManager() {
    assertThat(userDao.getSessionManager()).isNotNull().isEqualTo(sessionManager);
  }
}
