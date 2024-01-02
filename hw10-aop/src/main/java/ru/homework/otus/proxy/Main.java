package ru.homework.otus.proxy;

import java.lang.reflect.Proxy;

public class Main {
  public static void main(String[] args) {
    TestLoggingInterface testLogging = createLoggingProxy(new TestLoggingProxy());
    testLogging.calculation(6);
    testLogging.params(2, 1);
  }

  private static TestLoggingInterface createLoggingProxy(TestLoggingProxy target) {
    return (TestLoggingInterface) Proxy.newProxyInstance(
      TestLoggingInterface.class.getClassLoader(),
      new Class<?>[]{TestLoggingInterface.class},
      new LogInvocationHandler(target)
    );
  }
}
