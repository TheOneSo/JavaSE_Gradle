package com.oneso.hibernate.core.service;

import com.oneso.hibernate.cache.HwListener;
import com.oneso.hibernate.cache.MyCache;
import com.oneso.hibernate.core.dao.PhoneDao;
import com.oneso.hibernate.core.model.Phone;
import com.oneso.hibernate.exceptions.ServiceException;
import com.oneso.hibernate.core.sessionmanager.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicePhoneImpl implements ServicePhone {

  private static final Logger logger = LoggerFactory.getLogger(ServicePhoneImpl.class);

  private final MyCache<Long, Phone> cache = new MyCache<>();
  HwListener<Long, Phone> listener = new HwListener<Long, Phone>() {
    @Override
    public void notify(Long key, Phone value, String action) {
      logger.info("Phone id: {}, Info: {}, action: {}", key, value, action);
    }
  };

  private final PhoneDao phoneDao;
  private final boolean useCache;

  public ServicePhoneImpl(PhoneDao phoneDao, boolean useCache) {
    this.phoneDao = phoneDao;
    this.useCache = useCache;

    if(useCache) {
      cache.addListener(listener);
    }
  }

  @Autowired
  public ServicePhoneImpl(PhoneDao phoneDao) {
    this(phoneDao, false);
  }

  @Override
  public long savePhone(Phone phone) {
    try(SessionManager sessionManager = phoneDao.getSessionManager()) {
      sessionManager.beginSession();

      try {
        phoneDao.insertOrUpdate(phone);
        long id = phone.getId();
        sessionManager.commitSession();

        if(useCache) {
          cache.put(id, phone);
        }
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
    if(useCache) {
      Phone phone = cache.get(id);
      if(phone != null) {
        return Optional.of(phone);
      }
    }

    try(SessionManager sessionManager = phoneDao.getSessionManager()) {
      sessionManager.beginSession();

      try {
        Optional<Phone> phoneOptional = phoneDao.findById(id);
        if(useCache) {
          cache.put(id, phoneOptional.orElse(null));
        }

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
