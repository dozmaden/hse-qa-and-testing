package com.ozmaden;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountTest {

    private final boolean FIXED = true;
    // лучше не менять значение, т.к. корректность нового класса была уже протестирована в прошлом ДЗ

    private final int BOUND = 1000000;
    private Account account;

    @Before
    public void setUp() throws Exception {
        if (FIXED) {
            account = new FixedAccount();
        } else {
            //account = new OldAccount();
        }
    }

    /*
    Pitest
     */
    @Test
    public void depositBoundaryTest(){
        for (int i = Integer.MIN_VALUE; i < 0; i++) {
            assertFalse(account.deposit(i));
        }

        for (int i = 0; i <= BOUND; i++) {
            assertTrue(account.deposit(i));
        }

        for (int i = BOUND + 1; i < Integer.MAX_VALUE; i++) {
            assertFalse(account.deposit(i));
        }
    }

    /*
    Pitest
     */
    @Test
    public void withdrawBalanceTest() {
        for (int i = 0; i <= BOUND; i += 100) {
            for (int j = 0; j <= 1000; j++) {
                Account account = new FixedAccount();
                account.deposit(j);
                account.deposit(i);
                assertFalse(account.withdraw(i));
            }
        }
    }

    /*
    Pitest
     */
    @Test
    public void withdrawBalanceTest2(){
        assertFalse(account.isBlocked());
        var prev = account.getBalance();
        for (int i = BOUND; i < Integer.MAX_VALUE - BOUND; i += BOUND) {
            assertFalse(account.withdraw(i));
            assertEquals(prev, account.getBalance());
        }
    }

    /*
    Pitest
     */
    @Test
    public void withdrawBalanceTest3(){
        var prev = account.getBalance();
        assertFalse(account.withdraw(-BOUND - 1));
        assertEquals(prev, account.getBalance());
        assertFalse(account.withdraw(BOUND + 1));
        assertEquals(prev, account.getBalance());
    }

    /*
    Pitest
     */
    @Test
    public void withdrawBoundaryTest(){
        for (int i = Integer.MIN_VALUE; i < 0; i++) {
            assertFalse(account.withdraw(i));
        }

        for (int i = 0; i < BOUND + 1; i++) {
            Account acc = new FixedAccount();
            assertTrue(acc.deposit(BOUND - 1));
            assertTrue(acc.deposit(BOUND - 1));
            assertTrue(acc.withdraw(i));
        }

        for (int i = BOUND + 1; i < Integer.MAX_VALUE; i++) {
            assertFalse(account.withdraw(i));
        }
    }

    /*
    Pitest
     */
    @Test
    public void unblockPitest(){
        Account account = new FixedAccount();
        account.deposit(100);
        account.block();
        account.setMaxCredit(100);
        assertTrue(account.unblock());
        assertFalse(account.isBlocked());
    }

    /*
    Pitest
     */
    @Test
    public void setMaxCreditPitest() {
        for (int i = -BOUND; i <= BOUND; i += 1000) {
            Account account = new FixedAccount();
            account.block();
            assertTrue(account.isBlocked());
            assertTrue(account.setMaxCredit(i));
            assertEquals(account.getMaxCredit(), i);
        }
    }

    /*
    Pitest
     */
    @Test
    public void balancePitest(){
        assertEquals(0, account.getBalance());

        for (int i = -BOUND; i < 0; i += 1000){
            assertNotEquals(i, account.getBalance());
        }

        for (int i = 1; i < BOUND; i += 1000){
            assertNotEquals(i, account.getBalance());
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
     * */
    @Test
    public void balance_not_blocked_withdraw_max() {
        assertFalse(account.isBlocked());
        var balance = account.getBalance();
        var wd = account.withdraw(-account.getMaxCredit() - 1);
        assertFalse(wd);
        assertEquals(balance, account.getBalance());
    }

    @Test
    public void withdrawAgain(){
        assertEquals(0, account.getBalance());
        assertTrue(account.deposit(15000));
        assertEquals(15000, account.getBalance());
        assertTrue(account.withdraw(1000));
        assertEquals(14000, account.getBalance());
    }

    @Test
    public void withdraw_blocked_account() {
        var balance = account.getBalance();
        account.block();
        for (int i = -BOUND - 1; i <= BOUND; i++) {
            assertFalse(account.withdraw(i));
        }
        assertEquals(balance, account.getBalance());
    }

    /*
     * 3
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
}
