package com.oneso.core.service;

import com.oneso.core.dao.AddressDao;
import com.oneso.core.model.Address;
import com.oneso.core.sessionmanager.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

@DisplayName("ServiceAddressImpl should do")
@ExtendWith(MockitoExtension.class)
class ServiceAddressImplTest {

  private static final long ADDRESS_ID = 1L;

  @Mock
  private AddressDao addressDao;

  @Mock
  private SessionManager sessionManager;

  private InOrder inOrder;
  private ServiceAddress serviceAddress;
  private Address address;

  @BeforeEach
  void setUp() {
    given(addressDao.getSessionManager()).willReturn(sessionManager);
    inOrder = inOrder(addressDao, sessionManager);
    serviceAddress = new ServiceAddressImpl(addressDao);
    address = new Address();
    address.setId(ADDRESS_ID);
  }

  @Test
  @DisplayName("should save address")
  void saveAddress() {
    assertEquals(ADDRESS_ID, serviceAddress.saveAddress(address));
  }

  @Test
  @DisplayName("should get address")
  void getAddress() {
    given(addressDao.findById(ADDRESS_ID)).willReturn(Optional.of(address));
    Optional<Address> current = serviceAddress.getAddress(ADDRESS_ID);

    assertThat(current).isPresent().get().isEqualToComparingFieldByField(address);
  }

  @Test
  @DisplayName("should correct to save address and to open and to commit transaction in expected order")
  void shouldCorrectSaveAddressAndOpenAndCommitTranInExpectedOrder() {
    serviceAddress.saveAddress(address);

    inOrder.verify(addressDao, times(1)).getSessionManager();
    inOrder.verify(sessionManager, times(1)).beginSession();
    inOrder.verify(sessionManager, times(1)).commitSession();
    inOrder.verify(sessionManager, never()).rollbackSession();
  }

  @Test
  @DisplayName("should open and to rollback transaction when there is exception in expected order")
  void shouldOpenAndRollbackTranWhenExceptionInExpectedOrder() {
    doThrow(ServiceException.class).when(addressDao).insertOrUpdate(any());

    assertThrows(ServiceException.class, () -> serviceAddress.saveAddress(null));

    inOrder.verify(addressDao, times(1)).getSessionManager();
    inOrder.verify(sessionManager, times(1)).beginSession();
    inOrder.verify(sessionManager, never()).commitSession();
    inOrder.verify(sessionManager, times(1)).rollbackSession();
  }
}
