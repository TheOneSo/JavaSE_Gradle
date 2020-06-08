package com.oneso.chain;

import com.oneso.atm.ATM;

public abstract class AtmInChain {

  private AtmInChain next;

  public void setNext(AtmInChain next) {
    this.next = next;
  }

  public AtmInChain getNext() {
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
