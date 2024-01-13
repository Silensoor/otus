package ru.otus.processor.homework;

import java.time.LocalTime;

public class TestDateTimeProvider implements DateTimeProvider {
  private final LocalTime fixedTime;

  public TestDateTimeProvider(LocalTime fixedTime) {
    this.fixedTime = fixedTime;
  }

  @Override
  public LocalTime getCurrentTime() {
    return fixedTime;
  }
}
