package leetc.top;

public class P7_ReverseInteger {
    public static int reverse(int x) {
        boolean neg = ((x >>> 31) & 1) == 1;
        x = neg ? x : -x;
        int m = Integer.MIN_VALUE / 10;
        int o = Integer.MIN_VALUE % 10;
        int ans = 0;
        while (x != 0) {
            if (ans < m || (ans == m && x % 10 < o)) {
                return 0;
            }
            ans = ans * 10 + x % 10;
            x /= 10;
        }
        return neg ? ans : Math.abs(ans);
    }
}
