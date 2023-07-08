package basic.c42;

// 交易最多k次，求最大收益
public class StockK {
    private static int all(int[] prices) {
        int ans = 0;
        for (int i = 1; i < prices.length; i++) {
            ans += Math.max(prices[i] - prices[i - 1], 0);
        }
        return ans;
    }

    public static int max1(int[] prices, int k) {
        if (prices == null || prices.length == 0) {
            return 0;
        }
        int n = prices.length;
        if (k >= n / 2) {
            return all(prices);
        }
        // [已过时间,最多交易次数]最大收益
        int[][] dp = new int[n][k + 1];
        int ans = 0;
        for (int j = 1; j <= k; j++) {
            int t = dp[0][j - 1] - prices[0];
            for (int i = 1; i < n; i++) {
                // 斜率优化：卖出情况, 求j-1次收益最大值时, 枚举求max的公共部分
                t = Math.max(t, dp[i][j - 1] - prices[i]);
                // 卖出最大值：不卖出,卖出
                dp[i][j] = Math.max(dp[i - 1][j], t + prices[i]);
                ans = Math.max(ans, dp[i][j]);
            }
        }
        return ans;
    }

    public static int max2(int[] prices, int k) {
        if (prices == null || prices.length == 0) {
            return 0;
        }
        int n = prices.length;
        if (k >= n / 2) {
            return all(prices);
        }
        int[] dp = new int[n];
        int ans = 0;
        for (int j = 1; j <= k; j++) {
            int pre = dp[0];
            int t = pre - prices[0];
            for (int i = 1; i < n; i++) {
                pre = dp[i];
                t = Math.max(t, pre - prices[i]);
                dp[i] = Math.max(dp[i - 1], t + prices[i]);
                ans = Math.max(ans, dp[i]);
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] arr = {4, 1, 231, 21, 12, 312, 312, 3, 5, 2, 423, 43, 145};
        int k = 3;
        System.out.println(max1(arr, k));
        System.out.println(max2(arr, k));
    }
}
