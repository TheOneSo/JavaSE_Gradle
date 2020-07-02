package com.oneso.jdbc.dao;

import com.oneso.core.dao.UserDao;
import com.oneso.core.model.UserJdbc;
import com.oneso.core.sessionmanager.SessionManager;
import com.oneso.jdbc.mapper.JdbcMapper;

import java.util.Optional;

public class UserDaoJdbc implements UserDao {

  private final JdbcMapper<UserJdbc> jdbcMapper;
  private final SessionManager sessionManager;

  public UserDaoJdbc(JdbcMapper<UserJdbc> jdbcMapper, SessionManager sessionManager) {
    this.jdbcMapper = jdbcMapper;
    this.sessionManager = sessionManager;
  }

  @Override
  public Optional<UserJdbc> findById(long id) {
    return Optional.of(jdbcMapper.findById(id, UserJdbc.class));
  }

  @Override
  public long insertUser(UserJdbc userJdbc) {
    return jdbcMapper.insert(userJdbc);
  }

  @Override
  public void updateUser(UserJdbc userJdbc) {
    jdbcMapper.update(userJdbc);
  }

  @Override
  public void insertOrUpdate(UserJdbc userJdbc) {
    jdbcMapper.insertOrUpdate(userJdbc);
  }

  @Override
  public SessionManager getSessionManager() {
    return sessionManager;
  }
}
