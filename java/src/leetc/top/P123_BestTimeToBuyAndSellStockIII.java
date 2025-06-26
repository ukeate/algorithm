package leetc.top;

/**
 * LeetCode 123. 买卖股票的最佳时机 III (Best Time to Buy and Sell Stock III)
 * 
 * 问题描述：
 * 给定一个数组，它的第 i 个元素是一支给定的股票在第 i 天的价格。
 * 设计一个算法来计算你所能获取的最大利润。你最多可以完成两笔交易。
 * 
 * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 * 
 * 解法思路：
 * 状态机动态规划：
 * 1. 定义状态变量：
 *    - buyRest: 第二次买入后的最大利润
 *    - sellRest: 第一次卖出后的最大利润  
 *    - min: 到目前为止的最低价格（用于第一次交易）
 *    - ans: 第二次卖出后的最大利润（最终答案）
 * 
 * 2. 状态转移：
 *    - ans = max(ans, buyRest + prices[i])     // 第二次卖出
 *    - min = min(min, prices[i])              // 更新最低价格
 *    - sellRest = max(sellRest, prices[i] - min)  // 第一次卖出
 *    - buyRest = max(buyRest, sellRest - prices[i])  // 第二次买入
 * 
 * 核心思想：
 * - 将两次交易分解为四个状态：第一次买入、第一次卖出、第二次买入、第二次卖出
 * - 每个状态都维护到目前为止的最优值
 * - 第二次交易基于第一次交易的结果
 * 
 * 状态含义：
 * - min: 第一次买入的最佳价格
 * - sellRest: 完成第一次交易的最大利润
 * - buyRest: 第二次买入后的净值（sellRest - 买入价）
 * - ans: 完成两次交易的最大利润
 * 
 * 时间复杂度：O(n) - 只需遍历数组一次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/
 */
public class P123_BestTimeToBuyAndSellStockIII {
    public static int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0;
        }
        int ans = 0;
        int buyRest = -prices[0];
        int sellRest = 0;
        int min = prices[0];
        for (int i = 1; i < prices.length; i++) {
            ans = Math.max(ans, buyRest + prices[i]);
            min = Math.min(min, prices[i]);
            sellRest = Math.max(sellRest, prices[i] - min);
            buyRest = Math.max(buyRest, sellRest - prices[i]);
        }
        return ans;
    }
}
