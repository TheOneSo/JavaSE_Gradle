package com.oneso.hibernate.core.service;

import com.oneso.hibernate.cache.HwCache;
import com.oneso.hibernate.cache.HwListener;
import com.oneso.hibernate.core.dao.PhoneDao;
import com.oneso.hibernate.core.model.Phone;
import com.oneso.hibernate.exceptions.ServiceException;
import com.oneso.hibernate.core.sessionmanager.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicePhoneImpl implements ServicePhone {

  private static final Logger logger = LoggerFactory.getLogger(ServicePhoneImpl.class);

  HwListener<Long, Phone> listener = new HwListener<Long, Phone>() {
    @Override
    public void notify(Long key, Phone value, String action) {
      logger.info("Phone id: {}, Info: {}, action: {}", key, value, action);
    }
  };

  private final PhoneDao phoneDao;
  @Qualifier("phoneCache")
  private final HwCache<Long, Phone> cache;

  public ServicePhoneImpl(PhoneDao phoneDao, HwCache<Long, Phone> cache) {
    this.phoneDao = phoneDao;
    this.cache = cache;

    cache.addListener(listener);
  }

  @Override
  public long savePhone(Phone phone) {
    try(SessionManager sessionManager = phoneDao.getSessionManager()) {
      sessionManager.beginSession();

      try {
        phoneDao.insertOrUpdate(phone);
        long id = phone.getId();
        sessionManager.commitSession();

        cache.put(id, phone);
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
    Phone phoneCache = cache.get(id);
    if(phoneCache != null) {
      return Optional.of(phoneCache);
    }

    try(SessionManager sessionManager = phoneDao.getSessionManager()) {
      sessionManager.beginSession();

      try {
        Optional<Phone> phoneOptional = phoneDao.findById(id);
        cache.put(id, phoneOptional.orElse(null));

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
