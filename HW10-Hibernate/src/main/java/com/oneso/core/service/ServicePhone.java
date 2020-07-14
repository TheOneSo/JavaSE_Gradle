package com.oneso.core.service;

import com.oneso.core.model.Phone;

import java.util.Optional;

public interface ServicePhone {

  long savePhone(Phone phone);

  Optional<Phone> getPhone(long id);
}
