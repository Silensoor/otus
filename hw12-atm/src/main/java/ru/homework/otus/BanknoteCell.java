package ru.homework.otus;

public class BanknoteCell<T extends Banknote> {
  private int count;
  private final T banknote;

  public BanknoteCell(T banknote) {
    this.banknote = banknote;
    this.count = 0;
  }

  public void addBanknotes(int count) {
    this.count += count;
  }

  public BanknoteDenomination getBanknoteDenomination() {
    return banknote.denomination();
  }

  public int getCount() {
    return count;
  }

  public void withdrawBanknotes(int requestedCount) {
    int withdrawnCount = Math.min(requestedCount, count);
    count -= withdrawnCount;
  }
}
