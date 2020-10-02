package com.oneso.hibernate.core.service;

import com.oneso.hibernate.cache.HwListener;
import com.oneso.hibernate.cache.MyCache;
import com.oneso.hibernate.core.dao.AddressDao;
import com.oneso.hibernate.core.model.Address;
import com.oneso.hibernate.exceptions.ServiceException;
import com.oneso.hibernate.core.sessionmanager.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ServiceAddressImpl implements ServiceAddress {

  private static final Logger logger = LoggerFactory.getLogger(ServiceAddressImpl.class);

  private final MyCache<Long, Address> cache = new MyCache<>();
  HwListener<Long, Address> listener = new HwListener<Long, Address>() {
    @Override
    public void notify(Long key, Address value, String action) {
      logger.info("Address id: {}, Info: {}, action: {}", key, value, action);
    }
  };

  private final AddressDao addressDao;
  private final boolean useCache;

  public ServiceAddressImpl(AddressDao addressDao, boolean useCache) {
    this.addressDao = addressDao;
    this.useCache = useCache;

    if(useCache) {
      cache.addListener(listener);
    }
  }

  public ServiceAddressImpl(AddressDao addressDao) {
    this(addressDao, false);
  }

  @Override
  public long saveAddress(Address address) {
    try(SessionManager sessionManager = addressDao.getSessionManager()) {
      sessionManager.beginSession();

      try {
        addressDao.insertOrUpdate(address);
        long id = address.getId();
        sessionManager.commitSession();

        if(useCache) {
          cache.put(id, address);
        }
        logger.info("Address create: {}", id);
        return id;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new ServiceException(e);
      }
    }
  }

  @Override
  public Optional<Address> getAddress(long id) {
    if(useCache) {
      Address address = cache.get(id);
      if(address != null) {
        return Optional.of(address);
      }
    }

    try(SessionManager sessionManager = addressDao.getSessionManager()) {
      sessionManager.beginSession();

      try {
        Optional<Address> addressOptional = addressDao.findById(id);
        if(useCache) {
          cache.put(id, addressOptional.orElse(null));
        }

        logger.info("Address: {}", addressOptional.orElse(null));
        return addressOptional;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new ServiceException(e);
      }
    }
  }
}
