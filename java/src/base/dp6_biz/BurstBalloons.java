package base.dp6_biz;

// https://leetcode.com/problems/burst-balloons/
// 打爆气球，附近3个乘积得分
public class BurstBalloons {
    private static int process1(int[] arr, int l, int r) {
        if (l == r) {
            return arr[l - 1] * arr[l] * arr[r + 1];
        }
        int max = Math.max(arr[l - 1] * arr[l] * arr[r + 1] + process1(arr, l + 1, r),
                arr[l - 1] * arr[r] * arr[r + 1] + process1(arr, l, r - 1));
        for (int i = l + 1; i < r; i++) {
            // i最后打爆
            max = Math.max(max, arr[l - 1] * arr[i] * arr[r + 1]
                    + process1(arr, l, i - 1) + process1(arr, i + 1, r));
        }
        return max;
    }

    public static int max1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0];
        }
        int n = arr.length;
        int[] help = new int[n + 2];
        help[0] = 1;
        help[n + 1] = 1;
        for (int i = 0; i < n; i++) {
            help[i + 1] = arr[i];
        }
        return process1(help, 1, n);
    }

    //

    public static int max2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0];
        }
        int n = arr.length;
        int[] help = new int[n + 2];
        help[0] = 1;
        help[n + 1] = 1;
        for (int i = 0; i < n; i++) {
            help[i + 1] = arr[i];
        }
        int[][] dp = new int[n + 2][n + 2];
        for (int i = 1; i <= n; i++) {
            dp[i][i] = help[i - 1] * help[i] * help[i + 1];
        }
        for (int l = n; l >= 1; l--) {
            for (int r = l + 1; r <= n; r++) {
                int ans = Math.max(help[l - 1] * help[l] * help[r + 1] + dp[l + 1][r],
                        help[l - 1] * help[r] * help[r + 1] + dp[l][r - 1]);
                for (int i = l + 1; i < r; i++) {
                    ans = Math.max(ans, help[l - 1] * help[i] * help[r + 1] + dp[l][i - 1] + dp[i + 1][r]);
                }
                dp[l][r] = ans;
            }
        }
        return dp[1][n];
    }

}
