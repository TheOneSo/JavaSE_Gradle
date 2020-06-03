package com.oneso;

public class MainClassATM {

  public static void main(String[] args) {

//    new Test().test();

    var department = new DepartmentATM();
    department.createAtm("#12345");
    department.createAtm("#67890");
    department.createAtm("#09876");
    department.init();


    department.pickRandomMoney();
    department.restoreState();
    department.getBalance();
  }
}
