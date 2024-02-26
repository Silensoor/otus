package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Id;

import javax.swing.*;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

  private final DbExecutor dbExecutor;
  private final EntitySQLMetaData entitySQLMetaData;
  private final EntityClassMetaData<?> entityClassMetaData;

  public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<?> entityClassMetaData) {
    this.dbExecutor = dbExecutor;
    this.entitySQLMetaData = entitySQLMetaData;
    this.entityClassMetaData = entityClassMetaData;
  }

  @Override
  public Optional<T> findById(Connection connection, long id) {
    String sql = entitySQLMetaData.getSelectByIdSql();
    return dbExecutor.executeSelect(connection, sql, List.of(id), rs -> {
      try {
        if (rs.next()) {
          Constructor<T> constructor = (Constructor<T>) entityClassMetaData.getConstructor();
          T instance = constructor.newInstance();

          for (Field field : entityClassMetaData.getAllFields()) {
            field.setAccessible(true);
            Object value = rs.getObject(field.isAnnotationPresent(Id.class) ? "id" : field.getName());
            field.set(instance, value);
          }

          return instance;
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return null;
    });
  }

  @Override
  public List<T> findAll(Connection connection) {
    String sql = entitySQLMetaData.getSelectAllSql();
    return dbExecutor.executeSelect(connection, sql, Collections.emptyList(), rs -> {
      List<T> result = new ArrayList<>();
      try {
        while (rs.next()) {
          Constructor<T> constructor = (Constructor<T>) entityClassMetaData.getConstructor();
          T instance = constructor.newInstance();

          for (Field field : entityClassMetaData.getAllFields()) {
            field.setAccessible(true);
            Object value = rs.getObject(field.isAnnotationPresent(Id.class) ? "id" : field.getName());
            field.set(instance, value);
          }
          result.add(instance);
        }
        return result;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }).orElseGet(ArrayList::new);
  }

  @Override
  public long insert(Connection connection, T client) {

    String sql = entitySQLMetaData.getInsertSql();

    List<Object> params = entityClassMetaData.getFieldsWithoutId().stream()
      .map(field -> {
        try {
          field.setAccessible(true);
          return field.get(client);
        } catch (IllegalAccessException e) {
          throw new RuntimeException("Ошибка доступа к полю " + field.getName(), e);
        }
      })
      .collect(Collectors.toList());

    return dbExecutor.executeStatement(connection, sql, params);
  }

  @Override
  public void update(Connection connection, T client) {
    String sql = entitySQLMetaData.getUpdateSql(); // Получаем SQL запрос на обновление

    List<Object> params = entityClassMetaData.getFieldsWithoutId().stream()
      .map(field -> {
        try {
          field.setAccessible(true); // Делаем приватные поля доступными
          return field.get(client);
        } catch (IllegalAccessException e) {
          throw new RuntimeException("Ошибка доступа к полю " + field.getName(), e);
        }
      })
      .collect(Collectors.toList());

    try {
      Field idField = entityClassMetaData.getIdField();
      idField.setAccessible(true);
      Object idValue = idField.get(client);
      params.add(idValue);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Ошибка доступа к полю Id", e);
    }

    dbExecutor.executeStatement(connection, sql, params);
  }


}
