package com.oneso.atm;

import com.oneso.listener.AtmListener;
import com.oneso.atm.cells.Cell;
import com.oneso.atm.control.Manager;
import com.oneso.atm.money.Money;
import com.oneso.atm.util.Util;
import com.oneso.memento.State;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ATM {

  private static final Logger log = LogManager.getLogger(ATM.class);

  private final String name;
  private final Manager manager;
  private final AtmListener atmListener;
  private State state;

  public ATM(Manager manager, String name) {
    this.manager = manager;
    this.name = name;
    this.atmListener = this::getBalance;
  }

  public void getMoney(int count) {
    int pickCount = Util.pickValue(count, manager);
    log.debug("ATM[{}] Pick ${}", name, pickCount);
  }

  public void addMoney(Money money) {
    manager.updateCell(money, Cell::putMoney);
    log.debug("ATM[{}] Put ${}", name, money.getValue());
  }

  public void getBalance(String info) {
    log.debug("ATM[{}] Info: {} Count: {}", name, info,
        manager.getCells().stream().map(Cell::getMoney).reduce(0, Integer::sum));
  }

  public AtmListener getAtmListener() {
    return atmListener;
  }

  public String getName() {
    return name;
  }

  public ATM getClone() {
    return new ATM(manager.copy(), name);
  }

  public void setState(State state) {
    this.state = state;
  }

  public State getState() {
    return state;
  }
}
