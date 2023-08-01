package leetc.followup;

// 求不能取相邻数的情况下，最大累加和
public class MaxSum {
    public static int maxSum(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int n = arr.length;
        if (n == 1) {
            return arr[0];
        }
        if (n == 2) {
            return Math.max(arr[0], arr[1]);
        }
        int[] dp = new int[n];
        dp[0] = arr[0];
        dp[1] = Math.max(arr[0], arr[1]);
        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(Math.max(dp[i - 1], arr[i]), arr[i] + dp[i - 2]);
        }
        return dp[n - 1];
    }
}
