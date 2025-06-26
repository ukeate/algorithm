package basic.c42;

/**
 * 股票交易问题IV - 最多K次交易
 * 
 * 问题描述：
 * 给定一个整数数组prices，其中prices[i]是股票在第i天的价格
 * 设计一个算法来计算能获得的最大利润，最多可以完成k笔交易
 * 注意：一次交易包括买入和卖出，不能同时参与多笔交易
 * 
 * 关键优化：
 * 1. 当k ≥ n/2时，相当于可以进行无限次交易（贪心策略）
 * 2. 使用斜率优化减少重复计算
 * 3. 空间优化：二维DP压缩为一维DP
 * 
 * 算法思路：
 * 方法1：二维DP - O(nk)时间，O(nk)空间
 * 方法2：一维DP优化 - O(nk)时间，O(n)空间
 * 
 * 状态定义：dp[i][j] = 前i天最多j次交易的最大收益
 * 
 * @author 算法学习
 */
public class StockK {
    
    /**
     * 无限次交易的贪心策略
     * 当k足够大时，可以捕获所有上涨区间的利润
     * 
     * @param prices 股票价格数组
     * @return 无限次交易的最大收益
     * 
     * 贪心思路：
     * 只要第二天价格比今天高就买入并第二天卖出
     * 这样可以获得所有上涨趋势的利润
     */
    private static int all(int[] prices) {
        int ans = 0;
        for (int i = 1; i < prices.length; i++) {
            // 累加所有正收益
            ans += Math.max(prices[i] - prices[i - 1], 0);
        }
        return ans;
    }

    /**
     * 方法1：二维DP解法
     * 
     * @param prices 股票价格数组
     * @param k 最多交易次数
     * @return 最大收益
     * 
     * 状态转移方程：
     * dp[i][j] = max(dp[i-1][j], max(dp[t][j-1] - prices[t]) + prices[i])
     * 其中t为所有可能的买入时间点
     * 
     * 优化：使用变量t记录最优买入点，避免重复计算
     * 
     * 时间复杂度：O(nk)
     * 空间复杂度：O(nk)
     */
    public static int max1(int[] prices, int k) {
        if (prices == null || prices.length == 0) {
            return 0;
        }
        
        int n = prices.length;
        
        // 优化：当k足够大时，使用贪心策略
        if (k >= n / 2) {
            return all(prices);
        }
        
        // dp[i][j] = 前i天最多j次交易的最大收益
        int[][] dp = new int[n][k + 1];
        int ans = 0;
        
        // 枚举交易次数
        for (int j = 1; j <= k; j++) {
            // t表示在当前交易次数下的最优买入状态
            // 初始值：第0天买入的收益
            int t = dp[0][j - 1] - prices[0];
            
            // 枚举天数
            for (int i = 1; i < n; i++) {
                // 斜率优化：更新最优买入状态
                // t = max(dp[0][j-1] - prices[0], dp[1][j-1] - prices[1], ..., dp[i][j-1] - prices[i])
                t = Math.max(t, dp[i][j - 1] - prices[i]);
                
                // 状态转移：第i天最多j次交易的最大收益
                // 两种选择：不卖出（保持前一天的收益）或卖出（最优买入+当前价格）
                dp[i][j] = Math.max(dp[i - 1][j], t + prices[i]);
                ans = Math.max(ans, dp[i][j]);
            }
        }
        return ans;
    }

    /**
     * 方法2：一维DP空间优化
     * 将二维DP压缩为一维DP，节省空间
     * 
     * @param prices 股票价格数组
     * @param k 最多交易次数
     * @return 最大收益
     * 
     * 优化思路：
     * 观察到dp[i][j]只依赖于dp[i-1][j]和dp[t][j-1]
     * 可以重复使用同一个一维数组，按交易次数外层循环
     * 
     * 时间复杂度：O(nk)
     * 空间复杂度：O(n)
     */
    public static int max2(int[] prices, int k) {
        if (prices == null || prices.length == 0) {
            return 0;
        }
        
        int n = prices.length;
        
        // 优化：当k足够大时，使用贪心策略
        if (k >= n / 2) {
            return all(prices);
        }
        
        // 一维DP数组，dp[i]表示第i天的最大收益
        int[] dp = new int[n];
        int ans = 0;
        
        // 枚举交易次数
        for (int j = 1; j <= k; j++) {
            int pre = dp[0]; // 保存dp[0][j-1]
            int t = pre - prices[0]; // 最优买入状态
            
            // 枚举天数
            for (int i = 1; i < n; i++) {
                pre = dp[i]; // 保存dp[i][j-1]（下次循环使用）
                
                // 更新最优买入状态
                t = Math.max(t, pre - prices[i]);
                
                // 状态转移：不卖出 vs 卖出
                dp[i] = Math.max(dp[i - 1], t + prices[i]);
                ans = Math.max(ans, dp[i]);
            }
        }
        return ans;
    }

    /**
     * 测试方法：验证两种算法的正确性
     */
    public static void main(String[] args) {
        int[] arr = {4, 1, 231, 21, 12, 312, 312, 3, 5, 2, 423, 43, 145};
        int k = 3;
        
        System.out.println("二维DP结果: " + max1(arr, k));
        System.out.println("一维DP结果: " + max2(arr, k));
        
        // 测试k很大的情况
        int k_large = 100;
        System.out.println("k很大时结果: " + max1(arr, k_large));
    }
}
