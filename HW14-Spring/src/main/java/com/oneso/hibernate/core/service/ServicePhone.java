package com.oneso.hibernate.core.service;

import com.oneso.hibernate.core.model.Phone;

import java.util.Optional;

public interface ServicePhone {

  long savePhone(Phone phone);

  Optional<Phone> getPhone(long id);
}
