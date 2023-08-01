package leetc.top;

public class P69_Sqrt {
    public static int mySqrt(int x) {
        if (x == 0) {
            return 0;
        }
        if (x < 3) {
            return 1;
        }
        long ans = 1, l = 1, r = x, m = 0;
        while (l <= r) {
            m = (l + r) / 2;
            if (m * m <= x) {
                ans = m;
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return (int) ans;
    }
}
