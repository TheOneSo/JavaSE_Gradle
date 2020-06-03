package com.oneso.command;

import com.oneso.atm.ATM;
import com.oneso.atm.money.Money;

import java.util.Collections;
import java.util.Map;

public class AtmStartState {

  private final Map<Money, Integer> money;
  private final AtmCommand atmCommand = this::update;

  public AtmStartState(Map<Money, Integer> money) {
    this.money = money;
  }

  public AtmStartState(Money money, int count) {
    this.money = Collections.singletonMap(money, count);
  }

  private void update(ATM atm) {
    for (var temp : money.entrySet()) {
      for(int i = 0; i < temp.getValue(); i++) {
        atm.addMoney(temp.getKey());
      }
    }
  }

  public AtmCommand getAtmCommand() {
    return atmCommand;
  }
}
