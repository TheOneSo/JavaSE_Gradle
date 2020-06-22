package com.oneso.jdbc;

import com.oneso.jdbc.mapper.EntityClassMetaData;
import com.oneso.jdbc.mapper.EntitySQLMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

  private static final Logger logger = LoggerFactory.getLogger(EntitySQLMetaDataImpl.class);
  private final EntityClassMetaData<T> entityClassMetaData;

  public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
    this.entityClassMetaData = entityClassMetaData;
  }

  @Override
  public String getSelectAllSql() {
    return "select * from " + entityClassMetaData.getName();
  }

  @Override
  public String getSelectByIdSql() {
    StringBuilder sql = new StringBuilder();
    List<Field> fields = entityClassMetaData.getFieldsWithoutId();
    String names = fields.stream().map(Field::getName).collect(Collectors.joining(", "));
    sql.append("select ")
        .append(names)
        .append(" from ")
        .append(entityClassMetaData.getName())
        .append(" where id = ?");

    logger.info("Sql 'selectById' was created");
    return sql.toString();
  }

  @Override
  public String getInsertSql() {
    StringBuilder sql = new StringBuilder();
    List<Field> fields = entityClassMetaData.getAllFields();
    String strNames = fields.stream().map(Field::getName).collect(Collectors.joining(", "));
    String args = fields.stream().map(field -> "?").collect(Collectors.joining(", "));

    sql.append("insert into ")
        .append(entityClassMetaData.getName())
        .append("(")
        .append(strNames)
        .append(") values (")
        .append(args)
        .append(")");

    logger.info("Sql 'insert' was created");
    return sql.toString();
  }

  @Override
  public String getUpdateSql() {
    StringBuilder sql = new StringBuilder();
    List<Field> fields = entityClassMetaData.getFieldsWithoutId();

    sql.append("update ")
        .append(entityClassMetaData.getName());

    for (Field field : fields) {
      sql.append(" set ")
          .append(field.getName())
          .append(" = ?");
    }

    sql.append(" where ")
        .append(entityClassMetaData.getIdField().getName())
        .append(" = ?");

    logger.info("Sql 'update' was created");
    return sql.toString();
  }
}
