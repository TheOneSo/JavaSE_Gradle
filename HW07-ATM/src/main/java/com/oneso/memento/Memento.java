package com.oneso.memento;

import com.oneso.atm.ATM;

public class Memento {

  private final ATM atm;

  public Memento(ATM atm) {
    this.atm = atm.getClone();
  }

  public ATM getAtm() {
    return atm;
  }
}
