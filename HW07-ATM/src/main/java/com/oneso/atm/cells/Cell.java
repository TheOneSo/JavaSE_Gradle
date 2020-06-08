package com.oneso.atm.cells;

import com.oneso.atm.money.Money;

public interface Cell {

  Money getType();

  void putMoney();

  void putMoney(int count);

  int pickMoney();

  int getMoney();

  int getCount();
}
