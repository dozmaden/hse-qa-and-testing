package com.ozmaden;

public class FixedAccount implements Account {
    private final int bound = 1000000;
    private boolean blocked = false;
    private int balance = 0;
    private int mc = 1000;

    public boolean deposit(int sum) {
        if (blocked) {
            return false;
        } else if (sum < 0 || sum > bound) {
            return false;
        } else {
            balance += sum;
            return true;
        }
    }

    public boolean withdraw(int sum) {
        if (isBlocked()) {
            return false;
        } else if (sum < 0 || sum > bound) {
            return false;
        } else if (balance <= mc + sum) {
            return false;
        } else {
            balance -= sum;
            return true;
        }
    }

    public void block() {
        blocked = true;
    }

    public boolean unblock() {
        if (balance < mc) {
            return false;
        } else {
            blocked = false;
        }
        return true;
    }

    public boolean setMaxCredit(int mc) {
        if (isBlocked()) {
            if (mc < -bound || mc > bound) {
                return false;
            }
            this.mc = mc;
            return true;
        } else return false;
    }

    public int getBalance() {
        return balance;
    }

    public int getMaxCredit() {
        return mc;
    }

    public boolean isBlocked() {
        return blocked;
    }
}
