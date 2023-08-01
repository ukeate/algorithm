package leetc.top;

public class P62_UniquePaths {
    private static long gcd(long m, long n) {
        return n == 0 ? m : gcd(n, m % n);
    }

    public static int uniquePaths(int m, int n) {
        // C(all, part)
        int part = n - 1, all = m + n - 2;
        long o1 = 1, o2 = 1;
        for (int i = part + 1, j = 1; i <= all || j <= all - part; i++, j++) {
            o1 *= i;
            o2 *= j;
            long gcd = gcd(o1, o2);
            o1 /= gcd;
            o2 /= gcd;
        }
        return (int) o1;
    }
}
