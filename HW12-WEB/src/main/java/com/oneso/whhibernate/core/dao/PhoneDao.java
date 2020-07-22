package com.oneso.whhibernate.core.dao;

import com.oneso.whhibernate.core.model.Phone;

import java.util.Optional;

public interface PhoneDao extends Dao {

  Optional<Phone> findById(long id);

  long insertPhone(Phone phone);

  void updatePhone(Phone phone);

  void insertOrUpdate(Phone phone);
}
