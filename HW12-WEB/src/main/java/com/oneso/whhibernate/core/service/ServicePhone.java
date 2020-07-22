package com.oneso.whhibernate.core.service;

import com.oneso.whhibernate.core.model.Phone;

import java.util.Optional;

public interface ServicePhone {

  long savePhone(Phone phone);

  Optional<Phone> getPhone(long id);
}
