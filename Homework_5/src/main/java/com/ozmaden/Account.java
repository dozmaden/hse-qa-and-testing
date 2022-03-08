package com.ozmaden;

public interface Account {

    boolean deposit(int sum);

    boolean withdraw(int sum);

    int getBalance();

    int getMaxCredit();

    boolean isBlocked();

    void block();

    boolean unblock();

    boolean setMaxCredit(int mc);
}
