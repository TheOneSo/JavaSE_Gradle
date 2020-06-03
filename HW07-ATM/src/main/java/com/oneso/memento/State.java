package com.oneso.memento;

import com.oneso.atm.ATM;
import com.oneso.chain.Chain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;

public class State extends Chain {

  private static final Logger log = LogManager.getLogger(State.class);

  private final Deque<Memento> mementoDeque = new ArrayDeque<>();

  public void saveState(ATM atm) {
    mementoDeque.push(new Memento(atm));
  }

  public ATM restoreState() {
    ATM atm = mementoDeque.pop().getAtm();
    log.trace("ATM[{}] has restored state", atm.getName());
    return atm;
  }

  @Override
  protected void process(ATM atm) {
    State state = new State();
    state.saveState(atm.getClone());
    atm.setState(state);
    log.trace("ATM[{}] has saved state", atm.getName());
  }
}
