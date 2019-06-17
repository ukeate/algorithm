package chapter2;

import org.junit.Test;

/**
 * Created by outrun on 5/11/16.
 */
public class Gcd {

    public int gcd (int a, int b) {

        int rem;
        while(b > 0) {
            rem = a % b;
            a = b;
            b = rem;
        }
        return a;
    }

    @Test
    public void testHere() {
        System.out.println(gcd(9, 3));
    }
}
