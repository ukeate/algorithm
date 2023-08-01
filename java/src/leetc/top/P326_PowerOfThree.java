package leetc.top;

public class P326_PowerOfThree {
    // int内最大3的幂模
    public static boolean isPowerOfThree(int n) {
        return n > 0 && 1162261467 % n == 0;
    }
}
