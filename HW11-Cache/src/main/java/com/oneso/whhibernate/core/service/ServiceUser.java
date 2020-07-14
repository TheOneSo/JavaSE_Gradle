package com.oneso.whhibernate.core.service;

import com.oneso.whhibernate.core.model.User;

import java.util.Optional;

public interface ServiceUser {

  long saveUser(User user);

  Optional<User> getUser(long id);
}
