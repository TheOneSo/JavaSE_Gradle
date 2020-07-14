package com.oneso.hibernate.dao;

import com.oneso.AbstractHibernateTest;
import com.oneso.core.dao.PhoneDao;
import com.oneso.core.model.Phone;
import com.oneso.hibernate.sessionmanager.SessionManagerHibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PhoneDaoHibernate should do")
@ExtendWith(MockitoExtension.class)
class PhoneDaoHibernateTest extends AbstractHibernateTest {

  private SessionManagerHibernate sessionManager;
  private PhoneDao phoneDao;

  @BeforeEach
  public void setUp() {
    super.setUp();
    sessionManager = new SessionManagerHibernate(sessionFactory);
    phoneDao = new PhoneDaoHibernate(sessionManager);
  }

  @Test
  @DisplayName("should find and get an phone from DB")
  void findById() {
    Phone phone = new Phone(1, "12345", null);
    save(phone);

    assertThat(phone.getId()).isGreaterThan(0);

    sessionManager.beginSession();
    Optional<Phone> mayBePhone = phoneDao.findById(phone.getId());
    sessionManager.commitSession();

    assertThat(mayBePhone).isPresent().get().isEqualToComparingFieldByField(phone);
  }

  @Test
  @DisplayName("should save an phone into DB")
  void shouldSaveNewPhone() {
    Phone expectedPhone = new Phone(0, "123", null);
    sessionManager.beginSession();
    phoneDao.insertOrUpdate(expectedPhone);
    long id = expectedPhone.getId();
    sessionManager.commitSession();

    assertThat(id).isGreaterThan(0);

    Phone actualPhone = (Phone) load(id, Phone.class);
    assertEquals("123", actualPhone.getName());

    expectedPhone = new Phone(id, "0", null);
    sessionManager.beginSession();
    phoneDao.insertOrUpdate(expectedPhone);
    long newId = expectedPhone.getId();
    sessionManager.commitSession();

    assertThat(newId).isGreaterThan(0).isEqualTo(id);
    actualPhone = (Phone) load(newId, Phone.class);
    assertEquals("0", actualPhone.getName());
  }

  @Test
  void getSessionManager() {
    assertThat(phoneDao.getSessionManager()).isNotNull().isEqualTo(sessionManager);
  }
}
