package ru.homework.otus;

public class Main {

  public static void main(String[] args) {
    ATM atm = new ATM();

    atm.loadBanknotes(100, 10);
    atm.loadBanknotes(50, 20);
    atm.loadBanknotes(20, 30);

    int withdrawAmount = 480;
    int withdrawn = atm.withdrawAmount(withdrawAmount);

    if (withdrawn != -1) {
      System.out.println("Successfully withdrawn: " + withdrawn);
    } else {
      System.out.println("Unable to withdraw the requested amount.");
    }

    System.out.println("Remaining balance in ATM: " + atm.getRemainingBalance());
  }
}
