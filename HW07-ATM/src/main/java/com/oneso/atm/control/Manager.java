package com.oneso.atm.control;

import com.oneso.atm.cells.Cell;
import com.oneso.atm.money.Money;

import java.util.List;
import java.util.function.Consumer;

public interface Manager {

  void updateCell(Money money, Consumer<Cell> func);

  Cell getCell(Money money);

  List<Cell> getCells();

  Manager copy();
}
