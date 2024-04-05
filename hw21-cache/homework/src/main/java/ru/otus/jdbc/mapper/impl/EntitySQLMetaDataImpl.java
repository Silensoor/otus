package ru.otus.jdbc.mapper.impl;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

  private final EntityClassMetaData<?> entityClassMetaData;

  public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
    this.entityClassMetaData = entityClassMetaData;
  }

  @Override
  public String getSelectAllSql() {
    return "select * from " + entityClassMetaData.getName();
  }

  @Override
  public String getSelectByIdSql() {
    return "select * from " + entityClassMetaData.getName() + " where id = ?";
  }

  @Override
  public String getInsertSql() {
    var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
    String columns = fieldsWithoutId.stream()
      .map(Field::getName)
      .collect(Collectors.joining(", "));
    String values = fieldsWithoutId.stream()
      .map(field -> "?")
      .collect(Collectors.joining(", "));
    return "insert into " + entityClassMetaData.getName() + " (" + columns + ") values (" + values + ")";
  }

  @Override
  public String getUpdateSql() {
    var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
    String columns = fieldsWithoutId.stream()
      .map(field -> field.getName() + " = ?")
      .collect(Collectors.joining(", "));
    return "UPDATE " + entityClassMetaData.getName() + " SET " + columns + " WHERE " + entityClassMetaData.getIdField().getName() + " = ?";
  }
}
