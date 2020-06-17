package com.oneso.jdbc;

import com.oneso.core.model.User;
import com.oneso.jdbc.mapper.EntityClassMetaData;
import com.oneso.jdbc.mapper.EntitySQLMetaData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Entity SQL MetaData should do")
class EntitySQLMetaDataImplTest {

  private EntityClassMetaData<User> entityClassMetaData;
  private EntitySQLMetaData entitySQLMetaData;

  @BeforeEach
  void setUp() {
    entityClassMetaData = mock(EntityClassMetaData.class);
    entitySQLMetaData = new EntitySQLMetaDataImpl<>(entityClassMetaData);
  }

  @Test
  @DisplayName("should create sql with select")
  void getSelectAllSql() {
    String sql = "select * from User";
    when(entityClassMetaData.getName()).thenReturn("User");

    assertEquals(sql, entitySQLMetaData.getSelectAllSql());
  }

  @Test
  @DisplayName("should create sql with selectById")
  void getSelectByIdSql() {
    String sql = "select id, name, age from test where id = ?";
    when(entityClassMetaData.getName()).thenReturn("test");
    when(entityClassMetaData.getFieldsWithoutId()).thenReturn(List.of(User.class.getFields()));

    assertEquals(sql, entitySQLMetaData.getSelectByIdSql());
  }

  @Test
  @DisplayName("should create sql with insert")
  void getInsertSql() {
    String sql = "insert into test(id, name, age) values (?, ?, ?)";
    when(entityClassMetaData.getName()).thenReturn("test");
    when(entityClassMetaData.getAllFields()).thenReturn(List.of(User.class.getFields()));

    assertEquals(sql, entitySQLMetaData.getInsertSql());
  }

  @Test
  @DisplayName("should create sql with update")
  void getUpdateSql() {
    String sql = "update test set id = ? set name = ? set age = ? where id = ?";
    when(entityClassMetaData.getName()).thenReturn("test");
    when(entityClassMetaData.getFieldsWithoutId()).thenReturn(List.of(User.class.getFields()));
    when(entityClassMetaData.getIdField()).thenReturn(User.class.getFields()[0]);

    assertEquals(sql, entitySQLMetaData.getUpdateSql());
  }
}
