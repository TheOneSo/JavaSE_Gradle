package com.oneso.chain;

import com.oneso.atm.ATM;

public abstract class Chain {

  private Chain next;

  public void setNext(Chain next) {
    this.next = next;
  }

  public Chain getNext() {
    return next;
  }

  public void run(ATM atm) {
    process(atm);
    if(getNext() != null) {
      getNext().run(atm);
    }
  }

  protected abstract void process(ATM atm);
}
