package ru.homework.otus;

public enum BanknoteDenomination {
  DENOMINATION_20(20),
  DENOMINATION_50(50),
  DENOMINATION_100(100);

  private final int value;

  BanknoteDenomination(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
