package com.oneso.hibernate.core.service;

import com.oneso.hibernate.cache.HwCache;
import com.oneso.hibernate.cache.HwListener;
import com.oneso.hibernate.core.dao.AddressDao;
import com.oneso.hibernate.core.model.Address;
import com.oneso.hibernate.exceptions.ServiceException;
import com.oneso.hibernate.core.sessionmanager.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceAddressImpl implements ServiceAddress {

  private static final Logger logger = LoggerFactory.getLogger(ServiceAddressImpl.class);

  HwListener<Long, Address> listener = new HwListener<Long, Address>() {
    @Override
    public void notify(Long key, Address value, String action) {
      logger.info("Address id: {}, Info: {}, action: {}", key, value, action);
    }
  };

  private final AddressDao addressDao;
  @Qualifier("addressCache")
  private final HwCache<Long, Address> cache;

  public ServiceAddressImpl(AddressDao addressDao, HwCache<Long, Address> cache) {
    this.addressDao = addressDao;
    this.cache = cache;

    cache.addListener(listener);
  }

  @Override
  public long saveAddress(Address address) {
    try(SessionManager sessionManager = addressDao.getSessionManager()) {
      sessionManager.beginSession();

      try {
        addressDao.insertOrUpdate(address);
        long id = address.getId();
        sessionManager.commitSession();

        cache.put(id, address);
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
    Address addressCache = cache.get(id);
    if(addressCache != null) {
      return Optional.of(addressCache);
    }

    try(SessionManager sessionManager = addressDao.getSessionManager()) {
      sessionManager.beginSession();

      try {
        Optional<Address> addressOptional = addressDao.findById(id);
        cache.put(id, addressOptional.orElse(null));

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
