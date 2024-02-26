package ru.otus.jdbc.mapper.impl;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.crm.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

  private final Class<T> tClass;

  public EntityClassMetaDataImpl(Class<T> tClass) {
    this.tClass = tClass;
  }

  @Override
  public String getName() {
    return tClass.getSimpleName();
  }

  @Override
  public Constructor<T> getConstructor() {
    try {
      return tClass.getConstructor();
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Field getIdField() {
    return Arrays.stream(tClass.getDeclaredFields())
      .filter(field -> field.isAnnotationPresent(Id.class))
      .findFirst()
      .orElseThrow(() -> new RuntimeException("No find ID by class " + getName()));
  }

  @Override
  public List<Field> getAllFields() {
    return Arrays.asList(tClass.getDeclaredFields());
  }

  @Override
  public List<Field> getFieldsWithoutId() {
    return getAllFields().stream()
      .filter(field -> !field.isAnnotationPresent(Id.class))
      .collect(Collectors.toList());
  }
}
