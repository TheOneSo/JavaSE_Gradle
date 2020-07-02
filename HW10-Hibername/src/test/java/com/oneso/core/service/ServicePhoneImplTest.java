package com.oneso.core.service;

import com.oneso.core.dao.PhoneDao;
import com.oneso.core.model.Phone;
import com.oneso.core.sessionmanager.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@DisplayName("ServicePhoneImpl should do")
@ExtendWith(MockitoExtension.class)
class ServicePhoneImplTest {

  private static final long PHONE_ID = 1L;

  @Mock
  private PhoneDao phoneDao;

  @Mock
  private SessionManager sessionManager;

  private InOrder inOrder;
  private ServicePhone servicePhone;
  private Phone phone;

  @BeforeEach
  void setUp() {
    given(phoneDao.getSessionManager()).willReturn(sessionManager);
    inOrder = inOrder(phoneDao, sessionManager);
    servicePhone = new ServicePhoneImpl(phoneDao);
    phone = new Phone();
    phone.setId(PHONE_ID);
  }

  @Test
  @DisplayName("should save phone")
  void savePhone() {
    assertEquals(PHONE_ID, servicePhone.savePhone(phone));
  }

  @Test
  @DisplayName("should get phone")
  void getPhone() {
    given(phoneDao.findById(PHONE_ID)).willReturn(Optional.of(phone));
    Optional<Phone> current = servicePhone.getPhone(PHONE_ID);

    assertThat(current).isPresent().get().isEqualToComparingFieldByField(phone);
  }

  @Test
  @DisplayName("should correct to save phone and to open and to commit transaction in expected order")
  void shouldCorrectSavePhoneAndOpenAndCommitTranInExpectedOrder() {
    servicePhone.savePhone(phone);

    inOrder.verify(phoneDao, times(1)).getSessionManager();
    inOrder.verify(sessionManager, times(1)).beginSession();
    inOrder.verify(sessionManager, times(1)).commitSession();
    inOrder.verify(sessionManager, never()).rollbackSession();
  }

  @Test
  @DisplayName("should open and to rollback transaction when there is exception in expected order")
  void shouldOpenAndRollbackTranWhenExceptionInExpectedOrder() {
    doThrow(ServiceException.class).when(phoneDao).insertOrUpdate(any());

    assertThrows(ServiceException.class, () -> servicePhone.savePhone(null));

    inOrder.verify(phoneDao, times(1)).getSessionManager();
    inOrder.verify(sessionManager, times(1)).beginSession();
    inOrder.verify(sessionManager, never()).commitSession();
    inOrder.verify(sessionManager, times(1)).rollbackSession();
  }
}
