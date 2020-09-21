package com.oneso.hwhibernate.core.service;

import com.oneso.hwhibernate.core.model.User;

import java.util.Optional;

public interface ServiceUser {

  long saveUser(User user);

  Optional<User> getUser(long id);
}
