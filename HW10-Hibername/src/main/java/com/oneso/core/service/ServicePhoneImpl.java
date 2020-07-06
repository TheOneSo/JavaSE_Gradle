package com.oneso.core.service;

import com.oneso.core.dao.PhoneDao;
import com.oneso.core.model.Phone;
import com.oneso.core.sessionmanager.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ServicePhoneImpl implements ServicePhone {

  private static final Logger logger = LoggerFactory.getLogger(ServicePhoneImpl.class);

  private final PhoneDao phoneDao;

  public ServicePhoneImpl(PhoneDao phoneDao) {
    this.phoneDao = phoneDao;
  }

  @Override
  public long savePhone(Phone phone) {
    try(SessionManager sessionManager = phoneDao.getSessionManager()) {
      sessionManager.beginSession();

      try {
        phoneDao.insertOrUpdate(phone);
        long id = phone.getId();
        sessionManager.commitSession();
        logger.info("Phone create: {}", id);
        return id;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new ServiceException(e);
      }
    }
  }

  @Override
  public Optional<Phone> getPhone(long id) {
    try(SessionManager sessionManager = phoneDao.getSessionManager()) {
      sessionManager.beginSession();

      try {
        Optional<Phone> phoneOptional = phoneDao.findById(id);
        logger.info("Phone: {}", phoneOptional.orElse(null));
        return phoneOptional;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new ServiceException(e);
      }
    }
  }
}
