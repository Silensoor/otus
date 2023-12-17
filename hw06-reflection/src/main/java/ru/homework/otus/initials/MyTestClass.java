package ru.homework.otus.initials;

import ru.homework.otus.annotations.After;
import ru.homework.otus.annotations.Before;
import ru.homework.otus.annotations.Test;

class MyTestClass {
  @Before
  public void setUp() {
    System.out.println("Before test method");
  }

  @Test
  public void test1() {
    System.out.println("Test method 1");
  }

  @Test
  public void test2() {
    System.out.println("Test method 2");
    throw new RuntimeException("Test failed");
  }

  @After
  public void tearDown() {
    System.out.println("After test method");
  }
}
