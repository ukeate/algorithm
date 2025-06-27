package leetc.top;

/**
 * LeetCode 188. 买卖股票的最佳时机 IV (Best Time to Buy and Sell Stock IV)
 * 
 * 问题描述：
 * 给定一个整数数组 prices ，它的第 i 个元素 prices[i] 是一支给定的股票在第 i 天的价格。
 * 设计一个算法来计算你所能获取的最大利润。你最多可以完成 k 笔交易。
 * 
 * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 * 
 * 示例：
 * 输入：k = 2, prices = [2,4,1,5,10,6,7,9,4,9]
 * 输出：13
 * 解释：买入然后卖出第 i 天和第 j 天的股票，其中 i < j，最大利润 = 13
 * 
 * 解法思路：
 * 本类提供了两种不同的解法：
 * 
 * 方法1：基础动态规划
 * - 状态定义：dp[i][j] 表示在第i天完成j次交易后的最大利润
 * - 状态转移：对于第i天，考虑所有可能的买入时机
 * - 时间复杂度：O(n²k)，空间复杂度：O(nk)
 * 
 * 方法2：优化动态规划
 * - 核心优化：使用best变量记录最优买入时机，避免重复计算
 * - 状态转移：dp[i][j] = max(dp[i-1][j], arr[i] + best)
 * - 时间复杂度：O(nk)，空间复杂度：O(nk)
 * 
 * 特殊情况处理：
 * - 当k >= n/2时，相当于无限次交易，贪心策略即可
 * - 即每次价格上涨就买卖，累加所有正收益
 * 
 * 核心思想：
 * - 动态规划：通过状态转移找到最优解
 * - 优化策略：记录最优买入时机，减少计算量
 * - 贪心处理：当交易次数足够多时，转化为简单的贪心问题
 * 
 * 时间复杂度：O(nk) - n为天数，k为交易次数
 * 空间复杂度：O(nk) - DP数组的存储空间
 * 
 * LeetCode链接：https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/
 */
public class P188_BestTimeToBuyAndSellStockIV {
    
    /**
     * 当交易次数足够多时的贪心算法
     * 
     * 算法思路：
     * - 当k >= n/2时，可以进行无限次交易
     * - 采用贪心策略：每次价格上涨就进行一次买卖
     * - 累加所有正收益即为最大利润
     * 
     * @param prices 股票价格数组
     * @return 最大利润
     */
    private static int allTrans(int[] prices) {
        int ans = 0;
        // 遍历相邻天数，累加所有正收益
        for (int i = 1; i < prices.length; i++) {
            ans += Math.max(prices[i] - prices[i - 1], 0);
        }
        return ans;
    }

    /**
     * 方法1：基础动态规划解法
     * 
     * 算法思路：
     * 1. dp[i][j]表示第i天完成j次交易的最大利润
     * 2. 对于第i天，枚举所有可能的买入时机p
     * 3. 状态转移：dp[i][j] = max(dp[i-1][j], max(dp[p][j-1] + arr[i] - arr[p]))
     * 
     * @param k 最大交易次数
     * @param arr 股票价格数组
     * @return 最大利润
     */
    public static int maxProfit1(int k, int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int n = arr.length;
        
        // 当交易次数足够多时，转化为贪心问题
        if (k >= n / 2) {
            return allTrans(arr);
        }
        
        // dp[i][j] = 第i天完成j次交易后的最大利润
        int[][] dp = new int[n][k + 1];
        
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= k; j++) {
                // 第i天不交易，继承前一天的状态
                dp[i][j] = dp[i - 1][j];
                
                // 第i天卖出，枚举所有可能的买入时机
                for (int p = 0; p <= i; p++) {
                    // 在第p天买入，第i天卖出，完成第j次交易
                    dp[i][j] = Math.max(dp[p][j - 1] + arr[i] - arr[p], dp[i][j]);
                }
            }
        }
        return dp[n - 1][k];
    }

    /**
     * 方法2：优化动态规划解法
     * 
     * 算法思路：
     * 1. 使用best变量记录最优的买入时机，避免重复计算
     * 2. best = max(dp[i][j-1] - arr[i])，表示完成j-1次交易后在第i天买入的最大余额
     * 3. 状态转移：dp[i][j] = max(dp[i-1][j], arr[i] + best)
     * 
     * 优化要点：
     * - 避免了内层循环枚举买入时机
     * - 时间复杂度从O(n²k)优化到O(nk)
     * 
     * @param k 最大交易次数
     * @param arr 股票价格数组
     * @return 最大利润
     */
    public static int maxProfit2(int k, int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int n = arr.length;
        
        // 当交易次数足够多时，转化为贪心问题
        if (k >= n / 2) {
            return allTrans(arr);
        }
        
        int[][] dp = new int[n][k + 1];
        int ans = 0;
        
        // 对每个交易次数j进行计算
        for (int j = 1; j <= k; j++) {
            // best表示完成j-1次交易后买入股票的最优余额
            int best = dp[0][j - 1] - arr[0];
            
            for (int i = 1; i < n; i++) {
                // 更新最优买入时机：完成j-1次交易后在第i天买入
                best = Math.max(best, dp[i][j - 1] - arr[i]);
                
                // 状态转移：第i天不交易 vs 第i天卖出
                dp[i][j] = Math.max(dp[i - 1][j], arr[i] + best);
                
                // 更新全局最大利润
                ans = Math.max(dp[i][j], ans);
            }
        }
        return ans;
    }
}
