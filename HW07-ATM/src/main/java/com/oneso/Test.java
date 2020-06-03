package com.oneso;

import com.oneso.atm.ATM;
import com.oneso.atm.control.ManagerCells;
import com.oneso.atm.money.Money;
import com.oneso.command.AtmExecutor;
import com.oneso.command.AtmStartState;
import com.oneso.listener.EventProducer;
import com.oneso.memento.State;

public class Test {

  public void test() {
    var atm1 = new ATM(new ManagerCells(), "#12345");
    var atm2 = new ATM(new ManagerCells(), "#67890");
    var atm3 = new ATM(new ManagerCells(), "#09876");

    // Команды для создания начального состояния
    AtmExecutor executor = new AtmExecutor();
    executor.addExecutor(new AtmStartState(Money.FIVE, 10).getAtmCommand());
    executor.addExecutor(new AtmStartState(Money.TEN, 5).getAtmCommand());
    executor.addExecutor(new AtmStartState(Money.ONE_HUNDRED, 3).getAtmCommand());
    executor.addExecutor(new AtmStartState(Money.FIVE_HUNDRED, 1).getAtmCommand());

    // Сохранение текущего состояния ATM
    State state = new State();

    // Подписка
    EventProducer eventProducer = new EventProducer();
    eventProducer.addAtmListener(atm1.getAtmListener());
    eventProducer.addAtmListener(atm2.getAtmListener());
    eventProducer.addAtmListener(atm3.getAtmListener());

    // Настройка порядка
    executor.setNext(state);
    state.setNext(eventProducer);

    // Запуск на выполнение
    executor.run(atm1);
    executor.clearCommands();

    // Устанавливаем начальное состояние 2 автомата
    executor.addExecutor(new AtmStartState(Money.FIVE, 5).getAtmCommand());
    executor.addExecutor(new AtmStartState(Money.TEN, 10).getAtmCommand());
    executor.addExecutor(new AtmStartState(Money.ONE_HUNDRED, 10).getAtmCommand());
    executor.addExecutor(new AtmStartState(Money.FIVE_HUNDRED, 5).getAtmCommand());
    executor.run(atm2);
    executor.clearCommands();

    // Начальное состояние 3 автомата
    executor.addExecutor(new AtmStartState(Money.FIVE, 15).getAtmCommand());
    executor.addExecutor(new AtmStartState(Money.TEN, 5).getAtmCommand());
    executor.addExecutor(new AtmStartState(Money.ONE_HUNDRED, 5).getAtmCommand());
    executor.addExecutor(new AtmStartState(Money.FIVE_HUNDRED, 10).getAtmCommand());
    executor.run(atm3);
    executor.clearCommands();

    atm1.getMoney(100);
    atm2.getMoney(500);
    atm3.getMoney(1000);
    atm1.getBalance();
    atm2.getBalance();
    atm3.getBalance();

    atm2 = atm2.getState().restoreState();
    atm1.getBalance();
    atm2.getBalance();
    atm3.getBalance();

    ATM test = new ATM(new ManagerCells(), "TEST");
    AtmExecutor testExecutor = new AtmExecutor();
    testExecutor.setATM(test);
    testExecutor.addExecutor(new AtmStartState(Money.FIVE, 10).getAtmCommand());
    testExecutor.addExecutor(new AtmStartState(Money.TEN, 5).getAtmCommand());
    testExecutor.execute();
    test.getBalance();

    State stateTest = new State();
    stateTest.saveState(test.getClone());
    test.getMoney(50);
    test.getBalance();

    test = stateTest.restoreState();
    test.getBalance();
  }
}
