package com.oneso.hwhibernate.core.dao;

import com.oneso.hwhibernate.core.model.Address;

import java.util.Optional;

public interface AddressDao extends Dao {

  Optional<Address> findById(long id);

  long insertAddress(Address address);

  void updateAddress(Address address);

  void insertOrUpdate(Address address);
}
