package ru.homework.otus.initials;

import ru.homework.otus.annotations.After;
import ru.homework.otus.annotations.Before;
import ru.homework.otus.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SimpleTestFramework {
    public static void main(String[] args) {
    runTests(MyTestClass.class);
  }
  public static void runTests(Class<?> testClass) {
    Method[] methods = testClass.getDeclaredMethods();
    List<Method> beforeMethods = new ArrayList<>();
    List<Method> testMethods = new ArrayList<>();
    List<Method> afterMethods = new ArrayList<>();
    TestResult testResult = new TestResult();

    for (Method method : methods) {
      if (method.isAnnotationPresent(Before.class)) {
        beforeMethods.add(method);
      } else if (method.isAnnotationPresent(Test.class)) {
        testMethods.add(method);
      } else if (method.isAnnotationPresent(After.class)) {
        afterMethods.add(method);
      }
    }

    for (Method testMethod : testMethods) {
      try {
        Object testInstance = testClass.getDeclaredConstructor().newInstance();
        invokeMethods(testInstance, beforeMethods);
        invokeMethod(testInstance, testMethod);
        invokeMethods(testInstance, afterMethods);
        testResult.incrementSuccessfulTests();
      } catch (Exception e) {
        testResult.incrementFailedTests();
      } finally {
        testResult.incrementTotalTests();
      }
    }

    testResult.printResult();
  }

  private static void invokeMethods(Object instance, List<Method> methods) throws Exception {
    for (Method method : methods) {
      invokeMethod(instance, method);
    }
  }

  private static void invokeMethod(Object instance, Method method) throws Exception {
    method.invoke(instance);
  }

}
