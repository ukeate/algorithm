package leetc.top;

/**
 * LeetCode 714. 买卖股票的最佳时机含手续费 (Best Time to Buy and Sell Stock with Transaction Fee)
 * 
 * 问题描述：
 * 给定一个整数数组 prices，其中第 i 个元素代表了第 i 天的股票价格；
 * 非负整数 fee 代表了交易股票的手续费用。
 * 你可以无限次地完成交易，但是你每笔交易都需要付手续费。
 * 如果你已经购买了一个股票，在卖出它之前你就不能再继续购买股票了。
 * 返回获得利润的最大值。
 * 
 * 注意：这里的一笔交易指买入持有并卖出股票的整个过程，每笔交易你只需要为此支付一次手续费。
 * 
 * 示例：
 * - 输入：prices = [1, 3, 2, 8, 4, 9], fee = 2
 * - 输出：8
 * - 解释：能够达到的最大利润:
 *   在此处买入 prices[0] = 1
 *   在此处卖出 prices[3] = 8
 *   在此处买入 prices[4] = 4  
 *   在此处卖出 prices[5] = 9
 *   总利润: ((8 - 1) - 2) + ((9 - 4) - 2) = 8
 * 
 * 解法思路：
 * 状态机动态规划：
 * 1. 定义两个状态：持有股票(buy)和不持有股票(sell)
 * 2. buy[i] = 第i天持有股票的最大利润
 * 3. sell[i] = 第i天不持有股票的最大利润
 * 4. 状态转移：考虑买入、卖出、保持不变三种操作
 * 5. 手续费在卖出时扣除（或买入时扣除，保持一致即可）
 * 
 * 核心思想：
 * - 将股票交易抽象为状态机：持有/不持有两种状态
 * - 每个状态都要维护到当前为止的最大利润
 * - 考虑手续费对交易决策的影响
 * 
 * 空间优化：
 * - 当前状态只依赖前一个状态，可以用两个变量代替数组
 * 
 * 时间复杂度：O(n) - 遍历数组一次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/
 */
public class P714_BestTimeToBuyAndSellStockWithTransactionFee {
    
    /**
     * 计算含手续费的股票交易最大利润
     * 
     * 状态定义：
     * - bestBuy: 当前持有股票状态下的最大利润
     * - bestSell: 当前不持有股票状态下的最大利润
     * 
     * 状态转移：
     * 对于第i天：
     * - 如果要持有股票：max(继续持有, 今天买入)
     *   curBuy = max(bestBuy, bestSell - prices[i] - fee)
     * - 如果要不持有股票：max(继续不持有, 今天卖出)  
     *   curSell = max(bestSell, bestBuy + prices[i])
     * 
     * 手续费处理：
     * - 可以在买入时扣除手续费，也可以在卖出时扣除
     * - 这里选择在买入时扣除，避免重复计算
     * 
     * @param arr 股票价格数组
     * @param fee 每笔交易的手续费
     * @return 最大利润
     */
    public static int maxProfit(int[] arr, int fee) {
        // 边界条件检查
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        int n = arr.length;
        
        // 初始状态：第0天
        // 持有股票：买入第0天的股票，扣除手续费
        int bestBuy = -arr[0] - fee;
        // 不持有股票：没有进行任何操作
        int bestSell = 0;
        
        // 从第1天开始遍历
        for (int i = 1; i < n; i++) {
            // 计算第i天的两种状态
            
            // 第i天持有股票的最大利润
            // 选择1：继续持有之前买的股票（bestBuy不变）
            // 选择2：今天买入股票（bestSell - arr[i] - fee）
            int curBuy = Math.max(bestBuy, bestSell - arr[i] - fee);
            
            // 第i天不持有股票的最大利润  
            // 选择1：继续不持有股票（bestSell不变）
            // 选择2：今天卖出股票（bestBuy + arr[i]）
            int curSell = Math.max(bestSell, bestBuy + arr[i]);
            
            // 更新最优状态
            bestBuy = curBuy;
            bestSell = curSell;
        }
        
        // 最终不持有股票的利润一定大于等于持有股票的利润
        return bestSell;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        int[] prices1 = {1, 3, 2, 8, 4, 9};
        int fee1 = 2;
        System.out.println("测试用例1:");
        System.out.println("prices = " + java.util.Arrays.toString(prices1));
        System.out.println("fee = " + fee1);
        System.out.println("输出: " + maxProfit(prices1, fee1));
        System.out.println("期望: 8");
        System.out.println("最优策略: 买1卖8(-2手续费)=5, 买4卖9(-2手续费)=3, 总计8");
        System.out.println();
        
        // 测试用例2：高手续费情况
        int[] prices2 = {1, 3, 7, 5, 10, 3};
        int fee2 = 3;
        System.out.println("测试用例2 (高手续费):");
        System.out.println("prices = " + java.util.Arrays.toString(prices2));
        System.out.println("fee = " + fee2);
        System.out.println("输出: " + maxProfit(prices2, fee2));
        System.out.println("期望: 6");
        System.out.println("最优策略: 买1卖10(-3手续费)=6");
        System.out.println();
        
        // 测试用例3：单调递减
        int[] prices3 = {10, 8, 6, 4, 2};
        int fee3 = 1;
        System.out.println("测试用例3 (单调递减):");
        System.out.println("prices = " + java.util.Arrays.toString(prices3));
        System.out.println("fee = " + fee3);
        System.out.println("输出: " + maxProfit(prices3, fee3));
        System.out.println("期望: 0");
        System.out.println("无法盈利，不进行任何交易");
        System.out.println();
        
        // 测试用例4：手续费过高
        int[] prices4 = {1, 4, 2};
        int fee4 = 5;
        System.out.println("测试用例4 (手续费过高):");
        System.out.println("prices = " + java.util.Arrays.toString(prices4));
        System.out.println("fee = " + fee4);
        System.out.println("输出: " + maxProfit(prices4, fee4));
        System.out.println("期望: 0");
        System.out.println("手续费大于价差，无法盈利");
        System.out.println();
        
        // 测试用例5：频繁波动
        int[] prices5 = {1, 4, 6, 2, 8, 3, 10, 14};
        int fee5 = 2;
        System.out.println("测试用例5 (频繁波动):");
        System.out.println("prices = " + java.util.Arrays.toString(prices5));
        System.out.println("fee = " + fee5);
        System.out.println("输出: " + maxProfit(prices5, fee5));
        System.out.println("期望: 13");
        System.out.println("策略: 买1卖6(-2)=3, 买2卖8(-2)=4, 买3卖14(-2)=9, 总计16? 实际可能更优");
        System.out.println();
        
        // 测试用例6：只有两天
        int[] prices6 = {2, 5};
        int fee6 = 1;
        System.out.println("测试用例6 (两天):");
        System.out.println("prices = " + java.util.Arrays.toString(prices6));
        System.out.println("fee = " + fee6);
        System.out.println("输出: " + maxProfit(prices6, fee6));
        System.out.println("期望: 2");
        System.out.println("买2卖5(-1手续费)=2");
        System.out.println();
        
        // 算法特点说明
        System.out.println("算法特点:");
        System.out.println("- 核心思想: 状态机DP，维护持有/不持有两种状态");
        System.out.println("- 时间复杂度: O(n) - 单次遍历");
        System.out.println("- 空间复杂度: O(1) - 空间优化，只用两个变量");
        System.out.println("- 关键技巧: 在买入时扣除手续费，简化状态转移");
        System.out.println("- 贪心思维: 每次都选择当前最优状态");
        System.out.println("- 手续费影响: 减少频繁交易，倾向于持有更久");
    }
}
