package com.oneso.listener;

import com.oneso.atm.ATM;
import com.oneso.chain.AtmInChain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class EventProducer extends AtmInChain {

  private static final Logger log = LogManager.getLogger(EventProducer.class);

  private final List<AtmListener> atmListeners = new ArrayList<>();

  public void addAtmListener(AtmListener atmListener) {
    atmListeners.add(atmListener);
  }

  public void removeAtmListener(AtmListener atmListener) {
    atmListeners.remove(atmListener);
  }

  public void removeAll() {
    atmListeners.clear();
  }

  public void event(String info) {
    atmListeners.forEach(atmListener -> atmListener.onEvent(info));
  }

  @Override
  protected void process(ATM atm) {
    log.trace("Information has sent");
    this.event("Balance");
  }
}
