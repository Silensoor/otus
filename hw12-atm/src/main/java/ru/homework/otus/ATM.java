package ru.homework.otus;

public interface ATM {
  void loadBanknotes(BanknoteDenomination denomination, int count);
  int withdrawAmount(int amount) throws InsufficientFundsException;
  int getRemainingBalance();
}
