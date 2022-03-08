import java.util.*; 

public class BinomialCoefficient {

    /*@ spec_public @*/ private long answer;

    //@ requires n > 0;
    //@ requires k > 0;
    //@ requires n >= k;
    //@ ensures answer >= 0;
    public long binomialCoeff(int n, int k)
    {
        long[] C = new long[k + 1];
        C[0] = 1;

        int i = 1;
        //@ loop_invariant i >= 1;
        while (i <= n){
            int j = Math.min(i, k);
            //@ loop_invariant j <= i;
            //@ loop_invariant j <= k;
            while(j > 0) {
                C[j] = C[j] + C[j - 1];
                j--;
            }
            i++;
        }

        if (C[k] < 0) {
            this.answer = 0;
            throw new ArithmeticException("Overflow!");
        } else {
            this.answer = C[k];
        }
        return this.answer;
    }
}
