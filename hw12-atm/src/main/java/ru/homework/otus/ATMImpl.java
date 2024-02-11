package ru.homework.otus;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ATMImpl implements ATM {
  private final Map<BanknoteDenomination, BanknoteCell<?>> cells;

  public ATMImpl() {
    this.cells = new HashMap<>();
  }

  @Override
  public void loadBanknotes(BanknoteDenomination denomination, int count) {
    BanknoteCell<?> cell = cells.get(denomination);
    if (cell != null) {
      cell.addBanknotes(count);
    } else {
      cells.put(denomination, new BanknoteCell<>(new Banknote(denomination)));
      loadBanknotes(denomination, count);
    }
  }

  @Override
  public int withdrawAmount(int amount) throws InsufficientFundsException {
    Map<BanknoteDenomination, Integer> withdrawnBanknotes = new HashMap<>();
    if (withdraw(amount, withdrawnBanknotes)) {
      withdrawnBanknotes.forEach((denomination, count) -> {
        BanknoteCell<?> cell = cells.get(denomination);
        cell.withdrawBanknotes(count);
      });
      return amount;
    }
    throw new InsufficientFundsException("Insufficient funds to withdraw the requested amount");
  }


  @Override
  public int getRemainingBalance() {
    return cells.values().stream()
      .mapToInt(cell -> cell.getCount() * cell.getBanknoteDenomination().getValue())
      .sum();
  }

  private boolean withdraw(int amount, Map<BanknoteDenomination, Integer> withdrawnBanknotes) {

    Map<BanknoteDenomination, BanknoteCell<?>> availableCells = new TreeMap<>((s1, s2) -> s2.getValue() - s1.getValue());
    availableCells.putAll(cells);
    Map<BanknoteDenomination, BanknoteCell<?>> availableCellsSort = new TreeMap<>(Comparator.comparingInt(BanknoteDenomination::getValue));
    availableCellsSort.putAll(cells);
    //если с больших наминалов не прошла оплата, пробуем с мелких, лучше придумать не удалось.
    if (updateCheckAmount(amount, availableCells, withdrawnBanknotes) == 0) {
      return true;
    } else {
      withdrawnBanknotes.clear();
      return updateCheckAmount(amount, availableCellsSort, withdrawnBanknotes) == 0;
    }
  }

  private int updateCheckAmount(int amount, Map<BanknoteDenomination, BanknoteCell<?>> availableCells,
                                Map<BanknoteDenomination, Integer> withdrawnBanknotes) {
    int checkAmount = amount;
    for (Map.Entry<BanknoteDenomination, BanknoteCell<?>> entry : availableCells.entrySet()) {
      BanknoteDenomination denomination = entry.getKey();
      BanknoteCell<?> cell = entry.getValue();

      int cellCount = cell.getCount();
      int requestedNotes = checkAmount / denomination.getValue();
      int withdrawnNotes = Math.min(requestedNotes, cellCount);

      if (withdrawnNotes > 0) {
        checkAmount -= withdrawnNotes * denomination.getValue();
        withdrawnBanknotes.put(denomination, withdrawnNotes);
      }
    }
    return checkAmount;
  }
}
