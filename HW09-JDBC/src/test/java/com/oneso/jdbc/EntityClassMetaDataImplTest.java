package com.oneso.jdbc;

import com.oneso.core.model.UserJdbc;
import com.oneso.jdbc.mapper.EntityClassMetaData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Entity Class MetaData should do")
class EntityClassMetaDataImplTest {

  private EntityClassMetaData<UserJdbc> entityClassMetaData;

  @BeforeEach
  void setUp() {
    entityClassMetaData = new EntityClassMetaDataImpl<>(UserJdbc.class);
  }

  @Test
  @DisplayName("should return name of class")
  void getName() {
    assertEquals(UserJdbc.class.getSimpleName(), entityClassMetaData.getName());
  }

  @Test
  @DisplayName("should create constructor")
  void getConstructor() {
    assertNotNull(entityClassMetaData.getConstructor());
  }

  @Test
  @DisplayName("should get field with annotation id")
  void getIdField() {
    assertEquals("id", entityClassMetaData.getIdField().getName());
  }

  @Test
  @DisplayName("should get all field of class")
  void getAllFields() {
    List<Field> fieldList = entityClassMetaData.getAllFields();

    assertNotNull(fieldList);
    assertFalse(fieldList.isEmpty());
    assertEquals("id", fieldList.get(0).getName());
  }

  @Test
  @DisplayName("should get all field without id")
  void getFieldsWithoutId() {
    List<Field> fieldList = entityClassMetaData.getFieldsWithoutId();

    assertNotNull(fieldList);
    assertFalse(fieldList.isEmpty());
    assertEquals("name", fieldList.get(0).getName());
  }
}
