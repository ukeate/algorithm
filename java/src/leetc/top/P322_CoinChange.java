package leetc.top;

/**
 * LeetCode 322. 零钱兑换 (Coin Change)
 * 
 * 问题描述：
 * 给你一个整数数组 coins，表示不同面额的硬币；以及一个整数 amount，表示总金额。
 * 计算并返回可以凑成总金额所需的最少硬币个数。如果没有任何一种硬币组合能组成总金额，返回 -1。
 * 你可以认为每种硬币的数量是无限的。
 * 
 * 示例：
 * 输入：coins = [1, 3, 4], amount = 6
 * 输出：2
 * 解释：6 = 3 + 3
 * 
 * 输入：coins = [2], amount = 3
 * 输出：-1
 * 解释：无法用面额为2的硬币组成金额3
 * 
 * 解法思路：
 * 动态规划（完全背包问题变形）：
 * 
 * 1. 状态定义：
 *    dp[i] 表示组成金额 i 所需的最少硬币数量
 * 
 * 2. 状态转移：
 *    dp[i] = min(dp[i], dp[i - coin] + 1)
 *    对于每个硬币 coin，如果 i >= coin，则可以考虑使用这个硬币
 * 
 * 3. 初始化：
 *    dp[0] = 0（金额为0时不需要硬币）
 *    其他位置初始化为无穷大或无效值
 * 
 * 4. 遍历顺序：
 *    外层循环遍历金额，内层循环遍历硬币
 *    （也可以外层遍历硬币，内层遍历金额）
 * 
 * 核心思想：
 * - 完全背包：每个硬币可以使用无限次
 * - 最优子结构：组成金额i的最优解包含组成金额i-coin的最优解
 * - 无后效性：当前状态不依赖于未来状态
 * 
 * 关键优化：
 * - 提前排序硬币数组（可选）
 * - 使用滚动数组优化空间（这里使用一维数组）
 * 
 * 时间复杂度：O(amount × coins.length) - 双重循环
 * 空间复杂度：O(amount) - DP数组空间
 * 
 * LeetCode链接：https://leetcode.com/problems/coin-change/
 */
public class P322_CoinChange {
    
    /**
     * 计算组成目标金额的最少硬币数量
     * 
     * 动态规划算法：
     * 1. 初始化dp数组，dp[i]表示组成金额i的最少硬币数
     * 2. 对每个金额i，尝试使用每种硬币
     * 3. 更新dp[i] = min(dp[i], dp[i-coin] + 1)
     * 4. 返回dp[amount]
     * 
     * @param coins 硬币面额数组
     * @param amount 目标金额
     * @return 最少硬币数量，无法组成返回-1
     */
    public int coinChange(int[] coins, int amount) {
        if (amount == 0) {
            return 0; // 金额为0，不需要硬币
        }
        if (coins == null || coins.length == 0) {
            return -1; // 没有硬币，无法组成任何金额
        }
        
        // dp[i] 表示组成金额i所需的最少硬币数
        int[] dp = new int[amount + 1];
        
        // 初始化：除了dp[0]=0，其他位置设为无效值
        for (int i = 1; i <= amount; i++) {
            dp[i] = amount + 1; // 使用amount+1表示无效（不可能的硬币数量）
        }
        
        // 动态规划：对每个金额计算最少硬币数
        for (int i = 1; i <= amount; i++) {
            // 尝试使用每种硬币
            for (int coin : coins) {
                if (i >= coin) {
                    // 可以使用当前硬币，更新最优解
                    // dp[i-coin] + 1 表示使用当前硬币后的总硬币数
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        // 如果dp[amount]仍为初始值，说明无法组成目标金额
        return dp[amount] > amount ? -1 : dp[amount];
    }
}
