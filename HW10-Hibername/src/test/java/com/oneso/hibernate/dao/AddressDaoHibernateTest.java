package com.oneso.hibernate.dao;

import com.oneso.AbstractHibernateTest;
import com.oneso.core.dao.AddressDao;
import com.oneso.core.model.Address;
import com.oneso.hibernate.sessionmanager.SessionManagerHibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AddressDaoHibernate should do")
@ExtendWith(MockitoExtension.class)
class AddressDaoHibernateTest extends AbstractHibernateTest {

  private SessionManagerHibernate sessionManager;
  private AddressDao addressDao;

  @BeforeEach
  public void setUp() {
    super.setUp();
    sessionManager = new SessionManagerHibernate(sessionFactory);
    addressDao = new AddressDaoHibernate(sessionManager);
  }

  @Test
  @DisplayName("should find and get an address from DB")
  void findById() {
    Address address = new Address(1, "test", null);
    save(address);

    assertThat(address.getId()).isGreaterThan(0);

    sessionManager.beginSession();
    Optional<Address> mayBeAddress = addressDao.findById(address.getId());
    sessionManager.commitSession();

    assertThat(mayBeAddress).isPresent().get().isEqualToComparingFieldByField(address);
  }

  @Test
  @DisplayName("should save an address into DB")
  void shouldSaveNewAddress() {
    Address expectedAddress = new Address(0, "street", null);
    sessionManager.beginSession();
    addressDao.insertOrUpdate(expectedAddress);
    long id = expectedAddress.getId();
    sessionManager.commitSession();

    assertThat(id).isGreaterThan(0);

    Address actualAddress = (Address) load(id, Address.class);
    assertThat(actualAddress).isNotNull().hasFieldOrPropertyWithValue("street", actualAddress.getStreet());

    expectedAddress = new Address(id, "Doesnt street", null);
    sessionManager.beginSession();
    addressDao.insertOrUpdate(expectedAddress);
    long newId = expectedAddress.getId();
    sessionManager.commitSession();

    assertThat(newId).isGreaterThan(0).isEqualTo(id);
    actualAddress = (Address) load(newId, Address.class);
    assertThat(actualAddress).isNotNull().hasFieldOrPropertyWithValue("street", expectedAddress.getStreet());
    assertEquals("Doesnt street", actualAddress.getStreet());
  }

  @Test
  @DisplayName("should get a current session")
  void getSessionManager() {
    assertThat(addressDao.getSessionManager()).isNotNull().isEqualTo(sessionManager);
  }
}
