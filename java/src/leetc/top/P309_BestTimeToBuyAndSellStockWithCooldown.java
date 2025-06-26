package leetc.top;

/**
 * LeetCode 309. 最佳买卖股票时机含冷冻期 (Best Time to Buy and Sell Stock with Cooldown)
 * 
 * 问题描述：
 * 给定一个整数数组prices，其中第 i 个元素代表了第 i 天的股票价格 。​
 * 设计一个算法计算出最大利润。在满足以下约束条件下，你可以尽可能地完成更多的交易（多次买卖一支股票）:
 * 
 * 卖出股票后，你无法在第二天买入股票 (即冷冻期为 1 天)。
 * 
 * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 * 
 * 示例：
 * 输入: prices = [1,2,3,0,2]
 * 输出: 3 
 * 解释: 对应的交易状态为: [买入, 卖出, 冷冻期, 买入, 卖出]
 * 
 * 解法思路：
 * 动态规划 + 状态机设计：
 * 1. 定义状态：买入状态、卖出状态
 * 2. 状态转移：
 *    - buy[i]：第i天结束后处于买入状态的最大利润
 *    - sell[i]：第i天结束后处于卖出状态的最大利润
 * 3. 转移方程：
 *    - buy[i] = max(buy[i-1], sell[i-2] - prices[i])  // 保持或新买入（考虑冷冻期）
 *    - sell[i] = max(sell[i-1], buy[i-1] + prices[i]) // 保持或卖出
 * 4. 冷冻期处理：买入时要从sell[i-2]转移（跳过冷冻期）
 * 
 * 核心观察：
 * - 卖出后有1天冷冻期，不能立即买入
 * - 需要跟踪买入和卖出两种状态
 * - 最终答案是sell状态（不持有股票）
 * 
 * 提供三种实现：
 * 1. 递归解法（暴力搜索）
 * 2. 动态规划数组版本
 * 3. 空间优化版本（只用几个变量）
 * 
 * LeetCode链接：https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/
 */
public class P309_BestTimeToBuyAndSellStockWithCooldown {
    
    /**
     * 方法1：递归解法（暴力搜索）
     * 
     * 递归函数定义：
     * process1(prices, hasBuy, idx, buyPrice) 表示：
     * - 从第idx天开始
     * - 当前是否持有股票（hasBuy）
     * - 如果持有，买入价格是buyPrice
     * - 返回从idx天开始能获得的最大利润
     * 
     * @param prices 股票价格数组
     * @param hasBuy 当前是否持有股票
     * @param idx 当前天数索引
     * @param buyPrice 买入价格（如果持有股票）
     * @return 从当前状态开始能获得的最大利润
     */
    private static int process1(int[] prices, boolean hasBuy, int idx, int buyPrice) {
        // 递归边界：超出价格数组范围
        if (idx >= prices.length) {
            return 0;
        }
        
        if (hasBuy) {
            // 状态：持有股票，可以选择卖或不卖
            
            // 选择1：不卖，继续持有
            int noSell = process1(prices, true, idx + 1, buyPrice);
            
            // 选择2：卖出，获得利润，进入冷冻期（跳过下一天）
            int sell = prices[idx] - buyPrice + process1(prices, false, idx + 2, 0);
            
            return Math.max(noSell, sell);
        } else {
            // 状态：不持有股票，可以选择买或不买
            
            // 选择1：不买，保持不持有状态
            int noBuy = process1(prices, false, idx + 1, 0);
            
            // 选择2：买入，花费当前价格
            int buy = process1(prices, true, idx + 1, prices[idx]);
            
            return Math.max(noBuy, buy);
        }
    }

    /**
     * 方法1接口：递归解法
     * 
     * @param prices 股票价格数组
     * @return 最大利润
     */
    public int maxProfit1(int[] prices) {
        return process1(prices, false, 0, 0);
    }

    /**
     * 方法2：动态规划数组版本
     * 
     * 状态定义：
     * - buy[i]：第i天结束后处于买入状态（持有股票）的最大利润
     * - sell[i]：第i天结束后处于卖出状态（不持有股票）的最大利润
     * 
     * 状态转移：
     * - buy[i] = max(buy[i-1], sell[i-2] - prices[i])
     *   意思：保持持有 或 从前天卖出状态买入（考虑冷冻期）
     * - sell[i] = max(sell[i-1], buy[i-1] + prices[i])
     *   意思：保持不持有 或 从昨天持有状态卖出
     * 
     * @param prices 股票价格数组
     * @return 最大利润
     */
    public static int maxProfit2(int[] prices) {
        if (prices.length < 2) {
            return 0;  // 少于2天无法交易
        }
        
        int n = prices.length;
        int[] buy = new int[n];   // buy[i]: 第i天结束后持有股票的最大利润
        int[] sell = new int[n];  // sell[i]: 第i天结束后不持有股票的最大利润
        
        // 初始化前两天的状态
        // 第0天：buy[0] = -prices[0], sell[0] = 0（隐含）
        // 第1天的状态
        buy[1] = Math.max(-prices[0], -prices[1]);  // 第0天买入 或 第1天买入
        sell[1] = Math.max(0, prices[1] - prices[0]); // 不交易 或 第0天买第1天卖
        
        // 动态规划填表
        for (int i = 2; i < n; i++) {
            // 第i天卖出状态：保持不持有 或 卖出昨天持有的股票
            sell[i] = Math.max(sell[i - 1], buy[i - 1] + prices[i]);
            
            // 第i天买入状态：保持持有 或 从前天卖出状态买入（冷冻期）
            buy[i] = Math.max(buy[i - 1], sell[i - 2] - prices[i]);
        }
        
        // 最终不持有股票的利润最大
        return sell[n - 1];
    }

    /**
     * 方法3：空间优化版本
     * 
     * 观察：每个状态只依赖前面的几个状态，不需要保存整个数组
     * 只需要维护：
     * - buy1: 当前买入状态的最大利润
     * - sell1: 当前卖出状态的最大利润  
     * - sell2: 前天卖出状态的最大利润（用于冷冻期）
     * 
     * @param prices 股票价格数组
     * @return 最大利润
     */
    public static int maxProfit3(int[] prices) {
        if (prices.length < 2) {
            return 0;  // 少于2天无法交易
        }
        
        // 状态变量（对应第1天结束后的状态）
        int buy1 = Math.max(-prices[0], -prices[1]);      // 买入状态最大利润
        int sell1 = Math.max(0, prices[1] - prices[0]);   // 卖出状态最大利润
        int sell2 = 0;  // 前天卖出状态（对应第-1天，利润为0）
        
        // 滚动更新状态
        for (int i = 2; i < prices.length; i++) {
            int tmp = sell1;  // 保存当前sell1，它将成为下一轮的sell2
            
            // 更新卖出状态：保持 或 卖出
            sell1 = Math.max(sell1, buy1 + prices[i]);
            
            // 更新买入状态：保持 或 买入（从sell2转移，考虑冷冻期）
            buy1 = Math.max(buy1, sell2 - prices[i]);
            
            sell2 = tmp;  // 更新前天卖出状态
        }
        
        // 最终不持有股票的利润最大
        return sell1;
    }
}
