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

  @Override
  public String toString() {
    return "TestResult{" +
      "totalTests=" + totalTests +
      ", successfulTests=" + successfulTests +
      ", failedTests=" + failedTests +
      '}';
  }
}
