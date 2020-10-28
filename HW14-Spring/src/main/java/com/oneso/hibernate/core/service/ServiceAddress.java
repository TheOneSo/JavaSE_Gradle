package com.oneso.hibernate.core.service;

import com.oneso.hibernate.core.model.Address;

import java.util.Optional;

public interface ServiceAddress {

  long saveAddress(Address address);

  Optional<Address> getAddress(long id);
}
