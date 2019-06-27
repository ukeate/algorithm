package chapter1;

import org.junit.Test;

/**
 * Created by outrun on 4/10/16.
 */
public class Ex1_5 {

    private int num = 16;

    private int binaryOneCount(int num) {
        int oneCount = 0;
        while (num >= 1) {
            if (num % 2 == 1) {
                oneCount++;
            }
            num = num / 2;
        }
        return oneCount;
    }

    @Test
    public void binaryOneCountExec() {

        System.out.println(binaryOneCount(num));
    }
}
