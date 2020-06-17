package com.oneso.jdbc;

import com.oneso.jdbc.mapper.EntityClassMetaData;
import com.oneso.jdbc.mapper.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

  private static final Logger logger = LoggerFactory.getLogger(EntityClassMetaDataImpl.class);

  private final Class<T> clazz;

  private Constructor<T> constructor;
  private Field fieldWithId;
  private List<Field> fieldAll;
  private List<Field> fieldsWithoutId;

  public EntityClassMetaDataImpl(Class<T> clazz) {
    this.clazz = clazz;

    if(getIdField() == null) {
      logger.error("This object {} doesn't have annotation Id", clazz.getSimpleName());
      throw new ExceptionInInitializerError("This object doesn't have annotation Id");
    }
  }

  @Override
  public String getName() {
    return clazz.getSimpleName();
  }

  @Override
  public Constructor<T> getConstructor() {
    if(constructor != null) {
      return constructor;
    }

    if(fieldAll == null) {
      getAllFields();
    }

    Class<?>[] params = new Class<?>[fieldAll.size()];
    for(int i = 0; i < params.length; i++) {
      params[i] = fieldAll.get(i).getType();
    }

    try {
      constructor = clazz.getConstructor(params);
      logger.info("Constructor was created");
    } catch (NoSuchMethodException e) {
      logger.error("Cannot create constructor");
      throw new ExceptionInInitializerError("Cannot create constructor");
    }

    return constructor;
  }

  @Override
  public Field getIdField() {
    if(fieldWithId != null) {
      return fieldWithId;
    }

    Field[] fields = clazz.getDeclaredFields();
    for(Field field : fields) {
      if(field.isAnnotationPresent(Id.class)) {
        fieldWithId = field;
      }
    }
    logger.info("Field with id was initialized");
    return fieldWithId;
  }

  @Override
  public List<Field> getAllFields() {
    if(fieldAll != null) {
      return fieldAll;
    }

    fieldAll = List.of(clazz.getDeclaredFields());

    logger.info("Field all was initialized");
    return fieldAll;
  }

  @Override
  public List<Field> getFieldsWithoutId() {
    if(fieldsWithoutId != null) {
      return fieldsWithoutId;
    }

    fieldsWithoutId = Arrays.stream(clazz.getDeclaredFields())
        .filter(field -> !field.isAnnotationPresent(Id.class))
        .collect(Collectors.toList());

    logger.info("Field without id was initialized");
    return fieldsWithoutId;
  }
}
