package chapter10;

import org.junit.Test;

/**
 * Created by outrun on 6/6/16.
 */
public class Fig10_63 {

    private static long witness (long a, long i, long n) {
        if (i == 0) return 1;

        long x = witness(a, i / 2, n);
        if (x == 0) return 0;

        long y = (x * x) % n;
        if (y == 1 && x != 1 && x != n - 1) return 0;

        if (i % 2 != 0) {
            y = (a * y) % n;
        }

        return y;
    }

    public static final int TRIALS = 5;

    public static boolean isPrime(long n) {
        Random r = new Random();

        for (int counter = 0; counter < TRIALS; counter++) {
            if (witness(r.randomLong(2, n - 2), n - 1, n ) != 1) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void testHere() {
        System.out.println(isPrime(7));
    }
}
