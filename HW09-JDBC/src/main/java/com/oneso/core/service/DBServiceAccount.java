package com.oneso.core.service;

import com.oneso.core.model.Account;

import java.util.Optional;

public interface DBServiceAccount {

  long saveAccount(Account user);

  Optional<Account> getAccount(long id);
}
