package com.oneso.core.service;

import com.oneso.core.model.User;

import java.util.Optional;

public interface ServiceUser {

  long saveUser(User user);

  Optional<User> getUser(long id);
}
