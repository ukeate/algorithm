package basic.c42;

/**
 * 股票交易问题I - 只能交易一次
 * 
 * 问题描述：
 * 给定一个数组prices，其中prices[i]是股票在第i天的价格
 * 设计一个算法来计算能获得的最大利润，只能完成一笔交易
 * 注意：必须在购买股票后才能卖出股票
 * 
 * 算法思路：
 * 一次遍历法 - 维护历史最低价格
 * 对于每一天，计算如果在这一天卖出股票能获得的最大利润
 * 利润 = 当前价格 - 历史最低价格
 * 
 * 核心洞察：
 * 要获得最大利润，就要在最低点买入，在最高点卖出
 * 但我们不知道未来的价格，所以采用贪心策略：
 * 维护到目前为止的最低价格，每天都计算在当天卖出的利润
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 * 
 * @author 算法学习
 */
public class StockOnce {
    
    /**
     * 计算只交易一次的最大利润
     * 
     * @param prices 股票价格数组
     * @return 最大利润
     * 
     * 算法步骤：
     * 1. 维护变量min记录到目前为止的最低价格
     * 2. 维护变量ans记录到目前为止的最大利润
     * 3. 对每一天，更新最低价格，并计算当天卖出的利润
     * 4. 用当天利润更新最大利润
     * 
     * 状态含义：
     * min = 第0天到第i天的最低价格
     * ans = 第0天到第i天能获得的最大利润
     */
    public int max(int[] prices) {
        if (prices == null || prices.length == 0) {
            return 0;
        }
        
        int min = prices[0]; // 到目前为止遇到的最低价格
        int ans = 0;         // 到目前为止能获得的最大利润
        
        for (int i = 0; i < prices.length; i++) {
            // 更新历史最低价格
            min = Math.min(min, prices[i]);
            
            // 计算如果在第i天卖出能获得的利润
            // 并更新最大利润
            ans = Math.max(ans, prices[i] - min);
        }
        
        return ans;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        StockOnce solution = new StockOnce();
        
        // 测试用例1：[7,1,5,3,6,4]
        // 分析：在第2天(价格=1)买入，在第5天(价格=6)卖出，利润=5
        int[] prices1 = {7, 1, 5, 3, 6, 4};
        System.out.println("测试1结果: " + solution.max(prices1)); // 期望输出: 5
        
        // 测试用例2：[7,6,4,3,1]
        // 分析：价格一直下跌，无法获利
        int[] prices2 = {7, 6, 4, 3, 1};
        System.out.println("测试2结果: " + solution.max(prices2)); // 期望输出: 0
        
        // 测试用例3：[1,2,3,4,5]
        // 分析：在第1天(价格=1)买入，在第5天(价格=5)卖出，利润=4
        int[] prices3 = {1, 2, 3, 4, 5};
        System.out.println("测试3结果: " + solution.max(prices3)); // 期望输出: 4
        
        // 测试用例4：[2,4,1]
        // 分析：在第1天(价格=2)买入，在第2天(价格=4)卖出，利润=2
        int[] prices4 = {2, 4, 1};
        System.out.println("测试4结果: " + solution.max(prices4)); // 期望输出: 2
    }
}
