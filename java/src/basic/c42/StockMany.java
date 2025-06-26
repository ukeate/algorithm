package basic.c42;

/**
 * 股票交易问题II - 无限次交易
 * 
 * 问题描述：
 * 给定一个数组prices，其中prices[i]是股票在第i天的价格
 * 设计一个算法来计算能获得的最大利润，可以进行无限次交易
 * 注意：不能同时参与多笔交易（即，必须在再次购买前出售掉之前的股票）
 * 
 * 算法思路：
 * 贪心策略 - 捕获所有上涨区间的利润
 * 只要第i+1天的价格比第i天高，就在第i天买入，第i+1天卖出
 * 这样可以获得所有可能的利润
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 * 
 * @author 算法学习
 */
public class StockMany {
    
    /**
     * 计算无限次交易的最大利润
     * 
     * @param prices 股票价格数组
     * @return 最大利润
     * 
     * 贪心策略证明：
     * 对于任何一段上涨区间[a, b, c, d]（a < b < c < d），
     * 分段交易：(b-a) + (c-b) + (d-c) = d-a
     * 等于直接从a买入d卖出的利润
     * 因此贪心策略是最优的
     * 
     * 算法实现：
     * 遍历数组，累加所有positive的price[i] - price[i-1]
     */
    public static int max(int[] prices) {
        if (prices == null || prices.length == 0) {
            return 0;
        }
        
        int ans = 0;
        // 遍历所有相邻的价格对
        for (int i = 1; i < prices.length; i++) {
            // 只有当第i天价格高于第i-1天时才进行交易
            // 相当于在第i-1天买入，第i天卖出
            ans += Math.max(prices[i] - prices[i - 1], 0);
        }
        return ans;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：[7,1,5,3,6,4]
        // 分析：1买入5卖出(+4)，3买入6卖出(+3)，总利润=7
        int[] prices1 = {7, 1, 5, 3, 6, 4};
        System.out.println("测试1结果: " + max(prices1)); // 期望输出: 7
        
        // 测试用例2：[1,2,3,4,5]
        // 分析：连续上涨，每天都交易，总利润=4
        int[] prices2 = {1, 2, 3, 4, 5};
        System.out.println("测试2结果: " + max(prices2)); // 期望输出: 4
        
        // 测试用例3：[7,6,4,3,1]
        // 分析：连续下跌，无法获利，总利润=0
        int[] prices3 = {7, 6, 4, 3, 1};
        System.out.println("测试3结果: " + max(prices3)); // 期望输出: 0
    }
}
