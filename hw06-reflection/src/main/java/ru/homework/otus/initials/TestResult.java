package ru.homework.otus.initials;

public class TestResult {
  private int totalTests = 0;
  private int successfulTests = 0;
  private int failedTests = 0;

  public void incrementTotalTests() {
    totalTests++;
  }

  public void incrementSuccessfulTests() {
    successfulTests++;
  }

  public void incrementFailedTests() {
    failedTests++;
  }

  public void printResult() {
    System.out.println("Total tests: " + totalTests);
    System.out.println("Successful tests: " + successfulTests);
    System.out.println("Failed tests: " + failedTests);
  }
}
