package base.catalan;

// n个节点，有多少个不同的二叉树
public class DifferentBTNum {
    public static long num1(int n) {
        if (n < 0) {
            return 0;
        }
        if (n < 2) {
            return 1;
        }
        long[] dp = new long[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            for (int leftSize = 0; leftSize < i; leftSize++) {
                dp[i] += dp[leftSize] * dp[i - 1 - leftSize];
            }
        }
        return dp[n];
    }

    //

    private static long gcd(long m, long n) {
        return n == 0 ? m : gcd(n, m % n);
    }

    public static long num2(int n) {
        if (n < 0) {
            return 0;
        }
        if (n < 2) {
            return 1;
        }
        long a = 1;
        long b = 1;
        for (int i = 1, j = n + 1; i <= n; i++, j++) {
            a *= i;
            b *= j;
            long gcd = gcd(a, b);
            a /= gcd;
            b /= gcd;
        }
        return (b / a) / (n + 1);
    }

    public static void main(String[] args) {
        System.out.println("test begin");
        for (int i = 0; i < 15; i++) {
            long ans1 = num1(i);
            long ans2 = num2(i);
            if (ans1 != ans2) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
