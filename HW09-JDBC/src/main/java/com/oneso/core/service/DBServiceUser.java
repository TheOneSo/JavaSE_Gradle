package com.oneso.core.service;

import com.oneso.core.model.UserJdbc;

import java.util.Optional;

public interface DBServiceUser {

  long saveUser(UserJdbc userJdbc);

  Optional<UserJdbc> getUser(long id);
}
