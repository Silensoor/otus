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

  public int getBanknoteDenomination() {
    return banknote.getDenomination();
  }

  public int getCount() {
    return count;
  }

  public int withdrawBanknotes(int requestedCount) {
    int withdrawnCount = Math.min(requestedCount, count);
    count -= withdrawnCount;
    return withdrawnCount;
  }
}
