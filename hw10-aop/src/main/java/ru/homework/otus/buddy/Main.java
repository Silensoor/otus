package ru.homework.otus.buddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import ru.homework.otus.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Main {
  public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
    Class<? extends TestLogging> proxyClass = new ByteBuddy()
      .subclass(TestLogging.class)
      .method(ElementMatchers.isAnnotatedWith(Log.class))
      .intercept(MethodDelegation.to(LogInterceptor.class))
      .make()
      .load(TestLogging.class.getClassLoader())
      .getLoaded();

    // Создание экземпляра прокси-класса с использованием рекомендованного метода
    Constructor<? extends TestLogging> constructor = proxyClass.getDeclaredConstructor();
    TestLogging proxy = constructor.newInstance();

    // Вызов метода через прокси
    proxy.calculation(6);
    proxy.params(2,5);
  }
}
