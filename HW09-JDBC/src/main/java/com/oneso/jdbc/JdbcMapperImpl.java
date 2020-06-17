package com.oneso.jdbc;

import com.oneso.core.sessionmanager.SessionManager;
import com.oneso.jdbc.mapper.EntityClassMetaData;
import com.oneso.jdbc.mapper.EntitySQLMetaData;
import com.oneso.jdbc.mapper.JdbcMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class JdbcMapperImpl<T> implements JdbcMapper<T> {

  private static final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);

  private final SessionManager sessionManager;
  private final DbExecutor<T> dbExecutor;
  private final EntityClassMetaData<T> entityClassMetaData;
  private final EntitySQLMetaData entitySQLMetaData;

  public JdbcMapperImpl(SessionManager sessionManager, DbExecutor<T> dbExecutor,
                        EntityClassMetaData<T> entityClassMetaData, EntitySQLMetaData entitySQLMetaData) {
    this.sessionManager = sessionManager;
    this.dbExecutor = dbExecutor;
    this.entityClassMetaData = entityClassMetaData;
    this.entitySQLMetaData = entitySQLMetaData;
  }

  @Override
  public void insert(T objectData) {
    String sql = entitySQLMetaData.getInsertSql();
    List<Object> params = new ArrayList<>();

    try {
      for (Field field : entityClassMetaData.getAllFields()) {
        params.add(field.get(objectData));
      }

      dbExecutor.executeInsert(getConnection(), sql, params);
      logger.info("Insert was executed");
    } catch (SQLException | IllegalAccessException e) {
      logger.error("Method insert has had a problem");
      throw new MapperException("Method insert has had a problem", e);
    }
  }

  @Override
  public void update(T objectData) {
    String sql = entitySQLMetaData.getUpdateSql();
    List<Object> params = new ArrayList<>();

    try {
      for (Field field : entityClassMetaData.getFieldsWithoutId()) {
        params.add(field.get(objectData));
      }

      params.add(entityClassMetaData.getIdField().get(objectData));

      dbExecutor.executeInsert(getConnection(), sql, params);
      logger.info("Update was executed");
    } catch (SQLException | IllegalAccessException e) {
      logger.error("Method update has had a problem");
      throw new MapperException("Method update has had a problem", e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void insertOrUpdate(T objectData) {
    long id;
    try {
      id = entityClassMetaData.getIdField().getLong(objectData);
    } catch (IllegalAccessException e) {
      logger.error("Couldn't get id");
      throw new MapperException("Couldn't get id", e);
    }

    T obj = findById(id, (Class<T>) objectData.getClass());

    if(obj != null) {
      update(objectData);
    } else {
      insert(objectData);
    }
  }

  @Override
  public T findById(long id, Class<T> clazz) {
    String sql = entitySQLMetaData.getSelectByIdSql();
    Constructor<T> constructor = entityClassMetaData.getConstructor();
    List<Field> fields = entityClassMetaData.getFieldsWithoutId();
    Object[] params = new Object[fields.size() + 1];
    params[0] = id;


    try {
      dbExecutor.executeSelect(getConnection(), sql, id, resultSet -> {
            try {
              while (resultSet.next()) {
                for(int i = 1; i < params.length; i++) {
                  params[i] = resultSet.getObject(fields.get(i - 1).getName());
                }
              }
            } catch (SQLException e) {
              logger.error("Couldn't get a data from DB");
              throw new MapperException("Couldn't get a data from DB", e);
            }

        return null;
      });
    } catch (SQLException e) {
      logger.error("Couldn't execute sql");
      throw new MapperException("Couldn't execute sql", e);
    }

    try {
      return constructor.newInstance(params);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      logger.error("Method findById has had a problem");
      throw new MapperException("Method findById has had a problem", e);
    }
  }

  public SessionManager getSessionManager() {
    return sessionManager;
  }

  private Connection getConnection() {
    return sessionManager.getCurrentSession().getConnection();
  }
}
