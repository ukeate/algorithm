package basic.c46;
// https://leetcode.cn/problems/k-inverse-pairs-array/
// [1,n]个数字，求有多少种排列有k个逆序对
public class KInversePairs {
    public static int ways1(int n, int k) {
        if (n < 1 || k < 0) {
            return 0;
        }
        int[][] dp = new int[n + 1][k + 1];
        dp[0][0] = 1;
        int mod = 1000000007;
        for (int i = 1; i <= n; i++) {
            dp[i][0] = 1;
            // 最大一位向前插入产生逆序对
            for (int j = 1; j <= k; j++) {
                // i > j时, dp[i-1][0.. j]
                // j >= i时, dp[i-1][j-i+1..j]
                for (int r = Math.max(0, j - i + 1); r <= j; r++) {
                    dp[i][j] += dp[i - 1][r];
                    dp[i][j] %= mod;
                }
            }
        }
        return dp[n][k];
    }

    //

    public static int ways2(int n, int k) {
        if (n < 1 || k < 0) {
            return 0;
        }
        int[][] dp = new int[n + 1][k + 1];
        dp[0][0] = 1;
        int mod = 1000000007;
        for (int i = 1; i <= n; i++) {
            dp[i][0] = 1;
            for (int j = 1; j <= k; j++) {
                dp[i][j] = (dp[i][j - 1] + dp[i - 1][j]) % mod;
                if (j >= i) {
                    dp[i][j] = (dp[i][j] - dp[i - 1][j - i] + mod) % mod;
                }
            }
        }
        return dp[n][k];
    }

    public static int ways3(int n, int k) {
        if (n < 1 || k < 0) {
            return 0;
        }
        if (k == 0) {
            return 1;
        }
        int[][] dp = new int[n + 1][k + 1];
        dp[1][0] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i][0] = 1;
        }
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j <= k; j++) {
                dp[i][j] = dp[i][j - 1] + dp[i - 1][j] - (i <= j ? dp[i - 1][j - i] : 0);
            }
        }
        return dp[n][k];
    }


    public static void main(String[] args) {
        int n = 9;
        int k = 15;
        System.out.println(ways1(n, k));
        System.out.println(ways2(n, k));
        System.out.println(ways3(n, k));
    }
}
