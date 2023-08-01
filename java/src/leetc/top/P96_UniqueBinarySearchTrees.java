package leetc.top;

public class P96_UniqueBinarySearchTrees {

    private static long gcd(long m, long n) {
        return n == 0 ? m : gcd(n, m % n);
    }

    // 卡特兰数
    public static int numTrees(int n) {
        if (n < 0) {
            return 0;
        }
        if (n < 2) {
            return 1;
        }
        long a = 1, b = 1;
        for (int i = 1, j = n + 1; i <= n; i++, j++) {
            a *= i;
            b *= j;
            long gcd = gcd(a, b);
            a /= gcd;
            b /= gcd;
        }
        return (int) ((b / a) / (n + 1));
    }
}
