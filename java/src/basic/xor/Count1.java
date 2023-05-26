package basic.xor;

public class Count1 {
    public static int count(int n) {
        int count = 0;
        while (n != 0) {
            int rightOne = n & ((~n) + 1);
            count++;
            n ^= rightOne;
        }
        return count;
    }
}
