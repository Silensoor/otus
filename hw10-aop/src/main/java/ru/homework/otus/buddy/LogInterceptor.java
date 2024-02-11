package ru.homework.otus.buddy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.lang.reflect.Method;

public class LogInterceptor {
  @RuntimeType
  public static Object intercept(@AllArguments Object[] args, @Origin Method method) throws Throwable {
    System.out.print("executed method: " + method.getName() + ", param: ");
    for (Object arg : args) {
      System.out.print(arg + " ");
    }
    System.out.println();
    return null;
  }
}
