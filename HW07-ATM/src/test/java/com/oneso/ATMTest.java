package com.oneso;

import com.oneso.atm.ATM;
import com.oneso.atm.cells.Cell;
import com.oneso.atm.control.Manager;
import com.oneso.atm.control.ManagerCells;
import com.oneso.atm.money.Money;
import com.oneso.atm.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("ATM should")
class ATMTest {

    ATM atm;
    Manager manager;

    @BeforeEach
    void setup() {
        manager = new ManagerCells();
        atm = new ATM(manager, "test");
    }

    @Test
    @DisplayName("Should get money")
    void getMoney() {
        atm.addMoney(Money.TEN);
        atm.addMoney(Money.ONE_HUNDRED);
        int expected = 100;
        int actual = Util.pickValue(100, manager);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should add money")
    void addMoney() {
        manager = mock(ManagerCells.class);
        atm = new ATM(manager, "test");
        atm.addMoney(Money.TEN);
        atm.addMoney(Money.FIVE);
        atm.addMoney(Money.ONE_HUNDRED);
        atm.addMoney(Money.FIVE_HUNDRED);


        verify(manager, times(4)).updateCell(any(), any());
    }

    @Test
    @DisplayName("Should get count")
    void getCount() {
        atm.addMoney(Money.ONE_HUNDRED);
        int expected = 100;
        int actual = manager.getCells().stream().map(Cell::getMoney).reduce(0, Integer::sum);

        assertEquals(expected, actual);
    }
}
