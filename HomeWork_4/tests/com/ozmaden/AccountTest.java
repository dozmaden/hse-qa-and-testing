package com.ozmaden;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountTest {

    private final boolean FIXED = true;
    private final int BOUND = 1000000;
    private Account account;

    @Before
    public void setUp() throws Exception {
        if (FIXED) {
            account = new FixedAccount();
        } else {
            account = new OldAccount();
        }
    }

    @Test
    public void simple_test() {
        assertTrue(account.deposit(1500));
        assertTrue(account.withdraw(100));
    }

    @Test
    public void simple_block_test() {
        account.block();
        assertFalse(account.unblock());
    }

    /*
     * 2
     * Когда аккаунт не заблокирован пытаемся снять максимум.
     * */
    @Test
    public void balance_not_blocked_withdraw_max() {
        assertFalse(account.isBlocked());
        var balance = account.getBalance();
        var wd = account.withdraw(-account.getMaxCredit() - 1);
        assertFalse(wd);
        assertEquals(balance, account.getBalance());
    }

    /*
     * 3
     * Когда аккаунт не заблокирован пытаемся поставить максимальный кредит
     */
    @Test
    public void setMaxCredit_accountNotBlocked() {
        assertFalse(account.isBlocked());
        var credit = account.getMaxCredit();
        assertFalse(account.setMaxCredit(BOUND));
        assertFalse(account.setMaxCredit(BOUND + 1));
        assertFalse(account.setMaxCredit(-BOUND));
        assertFalse(account.setMaxCredit(-BOUND - 1));
        assertEquals(credit, account.getMaxCredit());
    }

    /*
     * 4
     * Когда аккаунт не заблокирован пытаемся поставить кредит больше, чем граница
     */
    @Test
    public void setMaxCredit_accountNotBlocked_MaxBound() {
        assertFalse(account.isBlocked());
        var credit = account.getMaxCredit();
        assertFalse(account.setMaxCredit(BOUND + 1));
        assertFalse(account.setMaxCredit(-BOUND - 1));
        assertEquals(credit, account.getMaxCredit());
    }

    @Test
    public void setMaxCredit_accountBlocked_MaxBound() {
        account.block();
        var credit = account.getMaxCredit();
        assertFalse(account.setMaxCredit(BOUND + 1));
        assertFalse(account.setMaxCredit(-BOUND - 1));
        assertEquals(credit, account.getMaxCredit());
    }

    /*
     * 5
     * Когда аккаунт заблокирован пытаемся поставить макс кредит меньше чем "граница"
     */
    @Test
    public void setMaxCredit_accountBlocked_SmallerThanBound() {
        account.block();
        for (int i = -BOUND; i < BOUND; i++) {
            assertTrue(account.isBlocked());
            assertTrue(account.setMaxCredit(i));
            assertEquals(account.getMaxCredit(), i);
        }
    }

    /*
     * 6
     * Пытаемся разблокировать аккаунт, когда ставим макс кредит <= депозиту
     */
    @Test
    public void unblock() {
        account.deposit(1000);
        account.block();
        account.setMaxCredit(999);
        assertTrue(account.unblock());
        assertFalse(account.isBlocked());


        account.deposit(100);
        account.block();
        account.setMaxCredit(100);
        assertTrue(account.unblock());
        assertFalse(account.isBlocked());
    }

    /*
     * 7, 8
     * Пытаемся проверить границы возможного депозита
     */
    @Test
    public void deposit() {
        var balance = account.getBalance();
        assertFalse(account.deposit(-BOUND - 1));
        assertFalse(account.deposit(-BOUND));
        assertFalse(account.deposit(BOUND + 1));
        assertEquals(balance, account.getBalance());
    }

    /*
     * 7, 8
     * Пытаемся снять все возможное количетсво денег
     */
    @Test
    public void withdraw() {
        var balance = account.getBalance();
        for (int i = -BOUND - 1; i <= BOUND; i++) {
            assertFalse(account.withdraw(i));
        }
        assertEquals(balance, account.getBalance());
    }

    /*
     * 9
     * Пытаемся положить депозит в заблокированный аккаунт.
     */
    @Test
    public void deposit_blocked_account() {
        var balance = account.getBalance();
        account.block();
        for (int i = -BOUND - 1; i <= BOUND; i++) {
            assertFalse(account.deposit(i));
        }
        assertEquals(balance, account.getBalance());
    }

    /*
     * 9
     * Пытаемся снять с заблокированного аккаунта.
     */

    @Test
    public void withdraw_blocked_account() {
        var balance = account.getBalance();
        account.block();
        for (int i = -BOUND - 1; i <= BOUND; i++) {
            assertFalse(account.withdraw(i));
        }
        assertEquals(balance, account.getBalance());
    }
}
