package chapter2;

import org.junit.Test;

/**
 * Created by outrun on 5/12/16.
 */
public class pow {

    private boolean isEven (long n ) {
        return n % 2 == 0;
    }

    public long pow (long x, int n) {
        if (n == 0) {
            return  1;
        }
//        if (n == 1) {
//            return x;
//        }
        if (isEven(n)) {
            return pow(x * x, n / 2);
        } else {
//            return pow(x * x, n / 2) * x;
            return pow(x, n - 1) * x;
        }
    }


    @Test
    public void testHere() {
        System.out.println(pow(2, 10));
    }
}
