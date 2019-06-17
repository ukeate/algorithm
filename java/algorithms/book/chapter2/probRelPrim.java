package chapter2;

import org.junit.Test;

/**
 * Created by outrun on 5/12/16.
 */
public class probRelPrim {

    Gcd gcd = new Gcd();

    public double probRelPrim(int n) {
        int rel = 0, tot = 0;

        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                tot++;
                if (gcd.gcd(i, j) == 1) {
                    rel++;
                }
            }
        }

        return (double) rel / tot;
    }

    @Test
    public void testHere() {
        System.out.println(probRelPrim(100));
    }
}
