package com.oneso.core.service;

import com.oneso.core.model.Address;

import java.util.Optional;

public interface ServiceAddress {

  long saveAddress(Address address);

  Optional<Address> getAddress(long id);
}
