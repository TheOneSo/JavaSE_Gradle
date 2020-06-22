package com.oneso.jdbc.mapper;

public interface EntitySQLMetaData {
  String getSelectAllSql();

  String getSelectByIdSql();

  String getInsertSql();

  String getUpdateSql();
}
