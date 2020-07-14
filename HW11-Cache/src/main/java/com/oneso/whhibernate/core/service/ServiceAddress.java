package com.oneso.whhibernate.core.service;

import com.oneso.whhibernate.core.model.Address;

import java.util.Optional;

public interface ServiceAddress {

  long saveAddress(Address address);

  Optional<Address> getAddress(long id);
}
