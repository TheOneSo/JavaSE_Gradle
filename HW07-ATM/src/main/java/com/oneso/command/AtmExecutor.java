package com.oneso.command;

import com.oneso.atm.ATM;
import com.oneso.chain.AtmInChain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class AtmExecutor extends AtmInChain {

  private static final Logger log = LogManager.getLogger(AtmExecutor.class);

  private final List<AtmCommand> atmCommands = new ArrayList<>();
  private ATM atm;

  public void setATM(ATM atm) {
    this.atm = atm;
  }

  public void addExecutor(AtmCommand atmCommand) {
    atmCommands.add(atmCommand);
  }

  public void execute() {
    atmCommands.forEach(c -> c.doCommand(atm));
  }

  public void clearCommands() {
    atmCommands.clear();
  }

  @Override
  public void process(ATM atm) {
    this.setATM(atm);
    log.trace("ATM[{}] has set ATM", atm.getName());
    this.execute();
    log.trace("ATM[{}] has executed everything command", atm.getName());
  }
}
