package chapter1;

import org.junit.Test;

/**
 * Created by outrun on 4/10/16.
 */
public class Ex1_6 {

    private char[] str = new char[]{'a', 'l', 'i', 'b', 'a', 'b', 'a'};

    private int count = 0;

    private void swap(char[] str, int a, int b) {
        if (a == b) return;
        char tmp = str[a];
        str[a] = str[b];
        str[b] = tmp;
    }

    private boolean isSwap(char[] str, int low, int high) {
        boolean flag = true;
        for (int i = low; i < high; i++) {
            if (str[i] == str[high]) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private void permute(char[] str, int low, int high) {
        if (low == high) {
            System.out.println(new String(str));
            count++;
            return;
        } else {
            for (int i = low; i <= high; i++) {
                if (isSwap(str, low, i)) {
                    swap(str, low, i);
                    permute(str, low + 1, high);
                    swap(str, low, i);
                }
            }
        }
    }

    @Test
    public void permute() {
        permute(str, 0, 6);
        System.out.println(count);
    }
}
