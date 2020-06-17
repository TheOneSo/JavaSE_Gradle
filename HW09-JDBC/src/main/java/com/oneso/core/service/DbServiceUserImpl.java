package com.oneso.core.service;

import com.oneso.jdbc.JdbcMapperImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.oneso.core.model.User;

import java.util.Optional;

public class DbServiceUserImpl implements DBServiceUser {
  private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

  private final JdbcMapperImpl<User> jdbcMapper;

  public DbServiceUserImpl(JdbcMapperImpl<User> jdbcMapper) {
    this.jdbcMapper = jdbcMapper;
  }

  @Override
  public long saveUser(User user) {
    try (var sessionManager = jdbcMapper.getSessionManager()) {
      sessionManager.beginSession();
      try {
        jdbcMapper.insert(user);
        sessionManager.commitSession();

        logger.info("created user: {}", user.id);
        return user.id;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }

  @Override
  public Optional<User> getUser(long id) {
    try (var sessionManager = jdbcMapper.getSessionManager()) {
      sessionManager.beginSession();
      try {
        Optional<User> userOptional = Optional.of(jdbcMapper.findById(id, User.class));

        logger.info("user: {}", userOptional.orElse(null));
        return userOptional;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
      }
      return Optional.empty();
    }
  }
}
