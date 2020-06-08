package com.oneso.atm.control;

import com.oneso.atm.cells.Cell;
import com.oneso.atm.cells.CellMoney;
import com.oneso.atm.money.Money;

import java.util.*;
import java.util.function.Consumer;

public class ManagerCells implements Manager {

  private List<Cell> cells = new ArrayList<>();

  public ManagerCells() {}

  public ManagerCells(List<Cell> cells) {
    this.cells = cells;
  }

  @Override
  public void updateCell(Money money, Consumer<Cell> func) {

    Optional<Cell> cell = cells.stream().filter(c -> c.getType().equals(money)).findFirst();

    if (cell.isPresent()) {
      func.accept(cell.get());
    } else {
      func.accept(createCell(money));
    }
  }

  @Override
  public Cell getCell(Money money) {
    return cells.stream().filter(c -> c.getType().equals(money)).findFirst()
        .orElseThrow(NoSuchElementException::new);
  }

  @Override
  public List<Cell> getCells() {
    return Collections.unmodifiableList(cells);
  }

  @Override
  public Manager copy() {
    List<Cell> copy = new ArrayList<>();
    for(var temp : cells) {
      Cell cell = new CellMoney(temp.getType());
      cell.putMoney(temp.getCount());
      copy.add(cell);
    }
    copy.sort((a, b) -> b.getType().getValue() - a.getType().getValue());

    return new ManagerCells(copy);
  }

  private Cell createCell(Money money) {

    var cellMoney = new CellMoney(money);
    cells.add(cellMoney);
    cells.sort((a, b) -> b.getType().getValue() - a.getType().getValue());

    return cellMoney;
  }
}
