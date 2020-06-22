package com.oneso.core.service;

import com.oneso.core.model.Account;
import com.oneso.jdbc.JdbcMapperImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DbServiceAccountImpl implements DBServiceAccount {
  private static final Logger logger = LoggerFactory.getLogger(DbServiceAccountImpl.class);

  private final JdbcMapperImpl<Account> jdbcMapper;

  public DbServiceAccountImpl(JdbcMapperImpl<Account> jdbcMapper) {
    this.jdbcMapper = jdbcMapper;
  }

  @Override
  public long saveAccount(Account acc) {
    try (var sessionManager = jdbcMapper.getSessionManager()) {
      sessionManager.beginSession();
      try {
        jdbcMapper.insert(acc);
        sessionManager.commitSession();

        logger.info("created account: {}", acc.getId());
        return acc.getId();
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }

  @Override
  public Optional<Account> getAccount(long id) {
    try (var sessionManager = jdbcMapper.getSessionManager()) {
      sessionManager.beginSession();
      try {
        Optional<Account> userOptional = Optional.of(jdbcMapper.findById(id, Account.class));

        logger.info("account: {}", userOptional.orElse(null));
        return userOptional;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
      }
      return Optional.empty();
    }
  }
}
