package com.oneso.hibernate.core.service;

import com.oneso.hibernate.core.model.User;

import java.util.List;
import java.util.Optional;

public interface ServiceUser {

  long saveUser(User user);

  List<User> getUsers();

  Optional<User> getUser(long id);
}
