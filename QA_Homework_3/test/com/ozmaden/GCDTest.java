package com.ozmaden;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * Deniz Ozmaden BPI196
 * */
class GCDTest {

    private GCD gcd;

    public int getGcd(int x, int y) {
        return gcd.gcd(x, y);
    }

    public void setGcd(GCD gcd) {
        this.gcd = gcd;
    }

    @BeforeEach
    void setUp() {
        setGcd(new GCD());
    }

    /*
     * Testing positive numbers
     * */
    @Test
    void gcdPositive() {
        assertEquals(3, getGcd(3, 6));
        assertEquals(8, getGcd(8, 64));
    }

    /*
     * Testing negative numbers
     * */
    @Test
    void gcdNegative() {
        assertEquals(3, getGcd(-3, 6));
        assertEquals(4, getGcd(60, -4));
        assertEquals(6, getGcd(-6, -12));
    }

    /*
     * Testing zero cases
     * */
    @Test
    void gcdZero() {
        assertEquals(2, getGcd(2, 0));
        assertEquals(2, getGcd(0, -2));
        assertEquals(0, getGcd(0, 0));
    }

    /*
     * Testing cases where GCD is 1
     * */
    @Test
    void gcdOne() {
        assertEquals(1, getGcd(2, 5));
        assertEquals(1, getGcd(-3, 10));
    }

    /*
     * Testing cases where GCD arguments are equal
     * */
    @Test
    void gcdEqual() {
        assertEquals(5, getGcd(5, 5));
        assertEquals(0, getGcd(0, 0));
        assertEquals(5, getGcd(-5, -5));
    }

    /*
     * Testing cases where GCD is the min argument
     * */
    @Test
    void gcdMinAbsolute() {
        assertEquals(5, getGcd(5, 10));
        assertEquals(2, getGcd(-2, 4));
    }

    /*
     * Testing cases where GCD arguments are INT.MAX_VALUE and INT.MIN_VALUE
     * */
    @Test
    void gcdMaxMin() {
        assertEquals(Integer.MAX_VALUE, getGcd(Integer.MAX_VALUE, Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, getGcd(Integer.MIN_VALUE, Integer.MIN_VALUE));

        assertEquals(Integer.MAX_VALUE, getGcd(Integer.MIN_VALUE + 1, Integer.MAX_VALUE));
        assertEquals(1, getGcd(Integer.MIN_VALUE, Integer.MAX_VALUE));
    }
}
