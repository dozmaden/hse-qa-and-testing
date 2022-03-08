/**
 * Copyright (c) 2009 ISP RAS.
 * 109004, A. Solzhenitsina, 25, Moscow, Russia.
 * All rights reserved.
 *
 * $Id$
 * Created on Dec 22, 2015
 *
 */

package com.ozmaden;

/**
 * @author Victor Kuliamin
 *
 */
public class GCD {
    public int gcd(int x, int y) {
        long t;
        long _x = x;
        long _y = y;

//        if (x < 0) x = -x;
//        if (y < 0) y = -y;

        if (x < 0) {
            _x = -1L * x;
        }
        if (y < 0) {
            _y = -1L * y;
        }

        while (_y != 0) {
            if (_y > _x) {
                _x = _y - _x;
                _y = _y - _x;
                _x = _x + _y;
            }

            if (_y == 0) break;

            t = _y;
            _y = _x % _y;
            _x = t;
        }
        return (int) _x;
    }
}
