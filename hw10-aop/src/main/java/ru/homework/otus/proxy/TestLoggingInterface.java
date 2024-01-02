package ru.homework.otus.proxy;

import ru.homework.otus.Log;

public interface TestLoggingInterface {
  @Log
  void calculation(int param);

  @Log
  void params(int one, int two);

}
