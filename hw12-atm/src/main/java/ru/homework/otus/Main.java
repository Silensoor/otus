package ru.homework.otus;

public class Main {

  public static void main(String[] args) {
    ATM atm = new ATMImpl();

    atm.loadBanknotes(BanknoteDenomination.DENOMINATION_100, 10);
    atm.loadBanknotes(BanknoteDenomination.DENOMINATION_50, 20);
    atm.loadBanknotes(BanknoteDenomination.DENOMINATION_20, 30);

    int withdrawAmount = 480;
    try {
      int withdrawn = atm.withdrawAmount(withdrawAmount);
      System.out.println("Successfully withdrawn: " + withdrawn);
    } catch (InsufficientFundsException e) {
      System.out.println(e.getMessage());
    }

    System.out.println("Remaining balance in ATM: " + atm.getRemainingBalance());
  }
}
