package com.oneso.hwhibernate.core.service;

import com.oneso.hwhibernate.core.model.Phone;

import java.util.Optional;

public interface ServicePhone {

  long savePhone(Phone phone);

  Optional<Phone> getPhone(long id);
}
