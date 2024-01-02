package ru.homework.otus.proxy;

import ru.homework.otus.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class LogInvocationHandler implements InvocationHandler {
  private final Object target;

  LogInvocationHandler(Object target) {
    this.target = target;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if (method.isAnnotationPresent(Log.class)) {
      logMethodInvocation(method, args);
    }
    return method.invoke(target, args);
  }

  private void logMethodInvocation(Method method, Object[] args) {
    System.out.print("executed method: " + method.getName() + ", param: ");
    for (Object arg : args) {
      System.out.print(arg + " ");
    }
    System.out.println();
  }
}
