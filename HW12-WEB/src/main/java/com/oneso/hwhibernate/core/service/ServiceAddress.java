package com.oneso.hwhibernate.core.service;

import com.oneso.hwhibernate.core.model.Address;

import java.util.Optional;

public interface ServiceAddress {

  long saveAddress(Address address);

  Optional<Address> getAddress(long id);
}
