package com.oneso.jdbc.dao;

import com.oneso.core.dao.UserDao;
import com.oneso.core.model.User;
import com.oneso.core.sessionmanager.SessionManager;
import com.oneso.jdbc.mapper.JdbcMapper;

import java.util.Optional;

public class UserDaoJdbc implements UserDao {

  private final JdbcMapper<User> jdbcMapper;
  private final SessionManager sessionManager;

  public UserDaoJdbc(JdbcMapper<User> jdbcMapper, SessionManager sessionManager) {
    this.jdbcMapper = jdbcMapper;
    this.sessionManager = sessionManager;
  }

  @Override
  public Optional<User> findById(long id) {
    return Optional.of(jdbcMapper.findById(id, User.class));
  }

  @Override
  public long insertUser(User user) {
    return jdbcMapper.insert(user);
  }

  @Override
  public void updateUser(User user) {
    jdbcMapper.update(user);
  }

  @Override
  public void insertOrUpdate(User user) {
    jdbcMapper.insertOrUpdate(user);
  }

  @Override
  public SessionManager getSessionManager() {
    return sessionManager;
  }
}
