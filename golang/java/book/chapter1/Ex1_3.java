package chapter1;

import org.junit.Test;

/**
 * Created by outrun on 4/8/16.
 */
public class Ex1_3 {

    private double num = 1234.4321;

    private void printDigit(double num) {
        if (num > 10) {
             printDigit(num / 10);
        }
        System.out.println(num % 10);
    }

    private void printDecimal(double num) {

        num = num * 10;
        System.out.println(num);
        num %= 1;
        if (num > 0) {
             printDecimal(num);
        }
    }

    @Test
    public void printOut() {
//         printDigit(num);
//        printDecimal(num);
        System.out.println(num%1);
    }
}
