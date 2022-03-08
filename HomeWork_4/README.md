### Озмаден Дениз БПИ-196

#### Ошибка 1:

1. До исправления:

```
    public boolean setMaxCredit(int mc) {
        if (mc < -bound || mc > bound)
            return false;
        else
            maxCredit = -mc;

        return true;
    }
```

2. При заблокированном аккаунте и входном аргументе меньше BOUND по абсолютному значению.
3. Требуемое значение: false. Полученное значение: true.
4. Правильный код:
    
```
public boolean setMaxCredit(int mc) {
        if (isBlocked()) {
            if (mc < -bound || mc > bound) {
                return false;
            }
            this.mc = mc;
            return true;
       } else return false;
   }
```

### Ошибка 2:


1. До исправления:

```
    public boolean withdraw(int sum) {
        if (blocked)
            return false;
        else if (sum < 0 || sum > bound)
            return false;
        else if (balance <= maxCredit + sum)
            return false;
        else {
            balance -= sum;
            return true;
        }
    }
```

2.  Account acc = new FixedAccount(); 
    acc.balance = - ac.getMaxCredit() + 500;
    acc.withdraw(500);
3. Требуемое значение: true. Полученное значение: false.
4. Правильный код:

```
    public boolean withdraw(int sum) {
        if (isBlocked()) {
            return false;
        } else if (sum < 0 || sum > bound) {
            return false;
        } else if (balance < mc + sum) {
            return false;
        } else {
            balance -= sum;
            return true;
        }
    }
```

### Ошибка 3:

1. До исправления:

```
    public boolean deposit(int sum) {
        if (blocked)
            return false;
        else if (sum < 0 || sum > bound)
            return false;
        else {
            balance += sum;
            return true;
        }
    }
```

2. Например если внесено 100, а потом внесено ещё раз (BOUND - 90)
3. Требуемое значение: false. Полученное значение: true.
4. Правильный код:

```
    public boolean deposit(int sum) {
        if (blocked) {
            return false;
        } else if (sum < 0 || sum > bound) {
            return false;
        } else if (balance + sum > bound) {
            return false;
        } else {
            balance += sum;
            return true;
        }
    }
```