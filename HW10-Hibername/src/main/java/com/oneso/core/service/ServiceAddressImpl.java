package com.oneso.core.service;

import com.oneso.core.dao.AddressDao;
import com.oneso.core.model.Address;
import com.oneso.core.sessionmanager.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ServiceAddressImpl implements ServiceAddress {

  private static final Logger logger = LoggerFactory.getLogger(ServiceAddressImpl.class);

  private final AddressDao addressDao;

  public ServiceAddressImpl(AddressDao addressDao) {
    this.addressDao = addressDao;
  }

  @Override
  public long saveAddress(Address address) {
    try(SessionManager sessionManager = addressDao.getSessionManager()) {
      sessionManager.beginSession();

      try {
        addressDao.insertOrUpdate(address);
        long id = address.getId();
        sessionManager.commitSession();
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
    try(SessionManager sessionManager = addressDao.getSessionManager()) {
      sessionManager.beginSession();

      try {
        Optional<Address> addressOptional = addressDao.findById(id);
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
