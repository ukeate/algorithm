package leetc.top;

/**
 * LeetCode 121. 买卖股票的最佳时机 (Best Time to Buy and Sell Stock)
 * 
 * 问题描述：
 * 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
 * 你只能选择某一天买入这只股票，并选择在未来的某一天卖出该股票。
 * 设计一个算法来计算你所能获取的最大利润。
 * 
 * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
 * 
 * 限制条件：
 * - 最多只能完成一笔交易（即买入和卖出一支股票一次）
 * - 必须先买入后卖出
 * 
 * 解法思路：
 * 一次遍历 + 动态最小值：
 * 1. 维护到当前为止的最低价格（最佳买入点）
 * 2. 对每一天，计算如果今天卖出能获得的利润
 * 3. 更新最大利润
 * 4. 更新最低价格
 * 
 * 核心思想：
 * - 贪心策略：总是在最低点买入
 * - 对于每一天，都尝试在这一天卖出
 * - 利润 = 当前价格 - 历史最低价格
 * 
 * 算法正确性：
 * - 最优解必定是在某个最低点买入，某个后续最高点卖出
 * - 通过维护历史最低价，保证了买入价格最优
 * - 通过遍历所有卖出点，保证了找到最优卖出时机
 * 
 * 时间复杂度：O(n) - 只需遍历数组一次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/best-time-to-buy-and-sell-stock/
 */
public class P121_BestTimeToBuyAndSellStock {
    
    /**
     * 计算买卖股票的最大利润（只能交易一次）
     * 
     * @param prices 股票价格数组，prices[i]表示第i天的价格
     * @return 最大利润，如果无法盈利返回0
     */
    public static int maxProfit(int[] prices) {
        // 边界情况处理
        if (prices == null || prices.length == 0) {
            return 0;
        }
        
        int min = prices[0];  // 到目前为止的最低价格（最佳买入价）
        int ans = 0;          // 最大利润
        
        // 遍历每一天的价格
        for (int i = 0; i < prices.length; i++) {
            // 更新历史最低价格
            min = Math.min(min, prices[i]);
            
            // 计算如果今天卖出的利润，并更新最大利润
            // 利润 = 今天的价格 - 历史最低价格
            ans = Math.max(ans, prices[i] - min);
        }
        
        return ans;
    }
}
