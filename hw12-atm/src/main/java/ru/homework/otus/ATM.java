package ru.homework.otus;

import java.util.HashMap;
import java.util.Map;

public class ATM {
  private final Map<Integer, BanknoteCell<?>> cells;

  public ATM() {
    this.cells = new HashMap<>();
  }

  public void loadBanknotes(int denomination, int count) {
    BanknoteCell<?> cell = cells.get(denomination);
    if (cell != null) {
      cell.addBanknotes(count);
    } else {
      cells.put(denomination, new BanknoteCell<>(new Banknote(denomination)));
      loadBanknotes(denomination, count);
    }
  }

  public int withdrawAmount(int amount) {
    Map<Integer, Integer> withdrawnBanknotes = new HashMap<>();
    if (withdraw(amount, withdrawnBanknotes)) {
      withdrawnBanknotes.forEach((denomination, count) -> {
        BanknoteCell<?> cell = cells.get(denomination);
        cell.withdrawBanknotes(count);
      });
      return amount;
    }
    return -1; // Невозможно выдать запрошенную сумму
  }

  private boolean withdraw(int amount, Map<Integer, Integer> withdrawnBanknotes) {
    if (amount == 0) {
      return true; // Сумма успешно собрана
    }

    for (BanknoteCell<?> cell : cells.values()) {
      int cellDenomination = cell.getBanknoteDenomination();
      int cellCount = cell.getCount();

      if (cellDenomination <= amount && cellCount > 0) {
        cell.withdrawBanknotes(1); // Попробовать снять одну банкноту
        withdrawnBanknotes.put(cellDenomination, withdrawnBanknotes.getOrDefault(cellDenomination, 0) + 1);

        if (withdraw(amount - cellDenomination, withdrawnBanknotes)) {
          return true;
        }

        // Отменить попытку снять одну банкноту
        cell.addBanknotes(1);
        withdrawnBanknotes.put(cellDenomination, withdrawnBanknotes.get(cellDenomination) - 1);
      }
    }

    return false;
  }

  public int getRemainingBalance() {
    return cells.values().stream()
      .mapToInt(cell -> cell.getCount() * cell.getBanknoteDenomination())
      .sum();
  }
}
