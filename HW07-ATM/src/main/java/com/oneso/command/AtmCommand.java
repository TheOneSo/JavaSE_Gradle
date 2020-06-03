package com.oneso.command;

import com.oneso.atm.ATM;

@FunctionalInterface
public interface AtmCommand {
  void doStartState(ATM atm);
}
