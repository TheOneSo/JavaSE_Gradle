package com.oneso.core.service;

import com.oneso.core.dao.UserDao;
import com.oneso.core.model.User;
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

@DisplayName("ServiceUserImpl should do")
@ExtendWith(MockitoExtension.class)
class ServiceUserImplTest {

  private static final long USER_ID = 1L;

  @Mock
  private UserDao userDao;

  @Mock
  private SessionManager sessionManager;

  private InOrder inOrder;
  private ServiceUser serviceUser;
  private User user;

  @BeforeEach
  void setUp() {
    given(userDao.getSessionManager()).willReturn(sessionManager);
    inOrder = inOrder(userDao, sessionManager);
    serviceUser = new ServiceUserImpl(userDao);
    user = new User();
    user.setId(USER_ID);
  }

  @Test
  @DisplayName("should save user")
  void saveUser() {
    assertEquals(USER_ID, serviceUser.saveUser(user));
  }

  @Test
  @DisplayName("should get user")
  void getUser() {
    given(userDao.findById(USER_ID)).willReturn(Optional.of(user));
    Optional<User> current = serviceUser.getUser(USER_ID);

    assertThat(current).isPresent().get().isEqualToComparingFieldByField(user);
  }

  @Test
  @DisplayName("should correct to save user and to open and to commit transaction in expected order")
  void shouldCorrectSaveUserAndOpenAndCommitTranInExpectedOrder() {
    serviceUser.saveUser(user);

    inOrder.verify(userDao, times(1)).getSessionManager();
    inOrder.verify(sessionManager, times(1)).beginSession();
    inOrder.verify(sessionManager, times(1)).commitSession();
    inOrder.verify(sessionManager, never()).rollbackSession();
  }

  @Test
  @DisplayName("should open and to rollback transaction when there is exception in expected order")
  void shouldOpenAndRollbackTranWhenExceptionInExpectedOrder() {
    doThrow(ServiceException.class).when(userDao).insertOrUpdate(any());

    assertThrows(ServiceException.class, () -> serviceUser.saveUser(null));

    inOrder.verify(userDao, times(1)).getSessionManager();
    inOrder.verify(sessionManager, times(1)).beginSession();
    inOrder.verify(sessionManager, never()).commitSession();
    inOrder.verify(sessionManager, times(1)).rollbackSession();
  }
}
