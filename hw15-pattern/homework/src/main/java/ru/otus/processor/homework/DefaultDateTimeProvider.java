package ru.otus.processor.homework;

import java.time.LocalTime;

public class DefaultDateTimeProvider implements DateTimeProvider {
  @Override
  public LocalTime getCurrentTime() {
    return LocalTime.now();
  }
}
