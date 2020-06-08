package com.oneso;

import com.oneso.atm.ATM;
import com.oneso.atm.control.ManagerCells;
import com.oneso.atm.money.Money;
import com.oneso.command.AtmExecutor;
import com.oneso.command.AtmStartState;
import com.oneso.listener.EventProducer;
import com.oneso.memento.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DepartmentATM {

  private final List<ATM> atmList = new ArrayList<>();
  private final AtmExecutor atmExecutor = new AtmExecutor();
  private final EventProducer eventProducer = new EventProducer();

  public DepartmentATM() {
    State state = new State();
    atmExecutor.setNext(state);
    state.setNext(eventProducer);
  }

  public void createAtm(String name) {
    atmList.add(new ATM(new ManagerCells(), name));
  }

  public void init() {
    for(ATM temp : atmList) {
      startState();
      eventProducer.addAtmListener(temp.getAtmListener());
      atmExecutor.run(temp);
    }
  }

  public void getBalance() {
    eventProducer.removeAll();
    atmList.forEach(atm -> eventProducer.addAtmListener(atm.getAtmListener()));
    eventProducer.event("Balance");
  }

  public void restoreState() {
    List<ATM> clone = new ArrayList<>();
    atmList.forEach(atm -> clone.add(atm.getState().restoreState()));
    atmList.addAll(clone);
  }

  private void startState() {
    atmExecutor.clearCommands();
    atmExecutor.addExecutor(new AtmStartState(Money.FIVE, new Random().nextInt(10)).getAtmCommand());
    atmExecutor.addExecutor(new AtmStartState(Money.TEN, new Random().nextInt(10)).getAtmCommand());
    atmExecutor.addExecutor(new AtmStartState(Money.ONE_HUNDRED, new Random().nextInt(10)).getAtmCommand());
    atmExecutor.addExecutor(new AtmStartState(Money.FIVE_HUNDRED, new Random().nextInt(10)).getAtmCommand());
  }

  public void pickRandomMoney() {
    atmList.forEach(atm -> atm.getMoney(100));
  }
}
