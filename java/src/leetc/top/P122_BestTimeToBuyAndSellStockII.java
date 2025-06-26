package leetc.top;

/**
 * LeetCode 122. 买卖股票的最佳时机 II (Best Time to Buy and Sell Stock II)
 * 
 * 问题描述：
 * 给定一个数组 prices ，其中 prices[i] 是一支给定股票第 i 天的价格。
 * 设计一个算法来计算你所能获取的最大利润。你可以尽可能地完成更多的交易（多次买卖一支股票）。
 * 
 * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 * 
 * 解法思路：
 * 贪心算法 - 抓住所有上涨机会：
 * 1. 关键观察：最优策略是抓住每一次价格上涨的机会
 * 2. 如果明天价格比今天高，今天买入明天卖出
 * 3. 如果明天价格比今天低或相等，今天不操作
 * 4. 累加所有正的价格差值
 * 
 * 贪心策略正确性证明：
 * - 任何复杂的交易策略都可以分解为连续的单日涨幅
 * - 例如：第1天买入第3天卖出的利润 = (第2天-第1天) + (第3天-第2天)
 * - 因此抓住所有单日涨幅就能获得最大利润
 * 
 * 核心思想：
 * - 把连续上涨期间的总涨幅分解为每日涨幅
 * - 每个正的日涨幅都对应一次买卖操作
 * - 负的或零的日涨幅对应不操作
 * 
 * 算法优势：
 * - 简单直观，容易理解
 * - 时间和空间复杂度都很优秀
 * - 自然处理了买卖时机的选择
 * 
 * 时间复杂度：O(n) - 只需遍历数组一次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/
 */
public class P122_BestTimeToBuyAndSellStockII {
    
    /**
     * 计算买卖股票的最大利润（可以多次交易）
     * 
     * @param prices 股票价格数组，prices[i]表示第i天的价格
     * @return 最大利润
     */
    public static int maxProfit(int[] prices) {
        // 边界情况处理
        if (prices == null || prices.length == 0) {
            return 0;
        }
        
        int ans = 0; // 总利润
        
        // 遍历每一天，计算与前一天的价格差
        for (int i = 1; i < prices.length; i++) {
            // 只有当今天价格比昨天高时才进行交易
            // 相当于昨天买入今天卖出，获得正收益
            // Math.max(prices[i] - prices[i - 1], 0) 确保只累加正收益
            ans += Math.max(prices[i] - prices[i - 1], 0);
        }
        
        return ans;
    }
}
