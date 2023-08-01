package base.dp6_biz;

// https://leetcode.com/problems/strange-printer/
public class StrangePrinter {
    private static int process1(char[] str, int l, int r) {
        if (l == r) {
            return 1;
        }
        int ans = r - l + 1;
        for (int k = l + 1; k <= r; k++) {
            ans = Math.min(ans, process1(str, l, k - 1) + process1(str, k, r) - (str[l] == str[k] ? 1 : 0));
        }
        return ans;
    }

    public static int min(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        return process1(str, 0, str.length - 1);
    }

    //

    private static int process2(char[] str, int l, int r, int[][] dp) {
        if (dp[l][r] != 0) {
            return dp[l][r];
        }
        int ans = r - l + 1;
        if (l == r) {
            ans = 1;
        } else {
            for (int k = l + 1; k <= r; k++) {
                ans = Math.min(ans, process2(str, l, k - 1, dp) + process2(str, k, r, dp) - (str[l] == str[k] ? 1 : 0));
            }
        }
        dp[l][r] = ans;
        return ans;
    }

    public static int min2(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        int n = str.length;
        int[][] dp = new int[n][n];
        return process2(str, 0, n - 1, dp);
    }

    //

    public static int min3(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        int n = str.length;
        int[][] dp = new int[n][n];
        dp[n - 1][n - 1] = 1;
        for (int i = 0; i < n - 1; i++) {
            dp[i][i] = 1;
            dp[i][i + 1] = str[i] == str[i + 1] ? 1 : 2;
        }
        for (int l = n - 3; l >= 0; l--) {
            for (int r = l + 2; r < n; r++) {
                dp[l][r] = r - l + 1;
                for (int k = l + 1; k <= r; k++) {
                    dp[l][r] = Math.min(dp[l][r], dp[l][k - 1] + dp[k][r] - (str[l] == str[k] ? 1 : 0));
                }
            }
        }
        return dp[0][n - 1];
    }
}
