package leetc.top;

public class P188_BestTimeToBuyAndSellStockIV {
    private static int allTrans(int[] prices) {
        int ans = 0;
        for (int i = 1; i < prices.length; i++) {
            ans += Math.max(prices[i] - prices[i - 1], 0);
        }
        return ans;
    }

    public static int maxProfit1(int k, int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int n = arr.length;
        if (k >= n / 2) {
            return allTrans(arr);
        }
        // i时间j次交易卖出后余额
        int[][] dp = new int[n][k + 1];
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= k; j++) {
                dp[i][j] = dp[i - 1][j];
                for (int p = 0; p <= i; p++) {
                    dp[i][j] = Math.max(dp[p][j - 1] + arr[i] - arr[p], dp[i][j]);
                }
            }
        }
        return dp[n - 1][k];
    }

    //

    public static int maxProfit2(int k, int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int n = arr.length;
        if (k >= n / 2) {
            return allTrans(arr);
        }
        int[][] dp = new int[n][k + 1];
        int ans = 0;
        for (int j = 1; j <= k; j++) {
            int best = dp[0][j - 1] - arr[0];
            for (int i = 1; i < n; i++) {
                best = Math.max(best, dp[i][j - 1] - arr[i]);
                dp[i][j] = Math.max(dp[i - 1][j], arr[i] + best);
                ans = Math.max(dp[i][j], ans);
            }
        }
        return ans;
    }
}
