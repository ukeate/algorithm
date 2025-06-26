package leetc.top;

/**
 * LeetCode 1000. 合并石头的最小成本 (Minimum Cost to Merge Stones)
 * 
 * 问题描述：
 * 有 N 堆石头排成一排，第 i 堆中有 stones[i] 块石头。
 * 每次移动（move）需要将连续的 K 堆石头合并为一堆，而这个移动的成本为这 K 堆石头的总数。
 * 找出把所有石头合并成一堆的最低成本。如果不可能，返回 -1。
 * 
 * 解法思路：
 * 区间动态规划算法：
 * 1. 首先判断是否可能合并：只有当 (n-1) % (k-1) == 0 时才可能合并成一堆
 * 2. 状态定义：dp[l][r][p] = 将区间[l,r]的石头合并成p堆的最小成本
 * 3. 状态转移：
 *    - 如果p > 1：dp[l][r][p] = min(dp[l][mid][1] + dp[mid+1][r][p-1])
 *    - 如果p == 1：dp[l][r][1] = dp[l][r][k] + sum(stones[l...r])
 * 4. 边界条件：dp[i][i][1] = 0（单个石头堆成本为0）
 * 
 * 关键观察：
 * - 每次合并k堆变成1堆，总数减少k-1
 * - 要从n堆变成1堆，需要减少n-1堆
 * - 所以需要 (n-1)/(k-1) 次合并，必须是整数
 * 
 * 优化技巧：
 * - 使用前缀和快速计算区间和
 * - 记忆化搜索避免重复计算
 * - 合理的分割点步长为k-1
 * 
 * 时间复杂度：O(n³ * k) - n为石头堆数
 * 空间复杂度：O(n² * k) - DP表空间
 * 
 * LeetCode链接：https://leetcode.com/problems/minimum-cost-to-merge-stones/
 */
public class P1000_MinimumCostToMergeStones {
    
    /**
     * 区间动态规划递归函数
     * 
     * @param l 左边界
     * @param r 右边界
     * @param p 目标堆数
     * @param arr 石头数组
     * @param k 每次合并的堆数
     * @param presum 前缀和数组
     * @param dp 记忆化数组
     * @return 最小成本，-1表示不可能
     */
    private static int process1(int l, int r, int p, int[] arr, int k, int[] presum, int[][][] dp) {
        // 如果已经计算过，直接返回结果
        if (dp[l][r][p] != 0) {
            return dp[l][r][p];
        }
        
        // 边界条件：单个石头堆
        if (l == r) {
            return p == 1 ? 0 : -1;
        }
        
        if (p == 1) {
            // 合并成1堆：先合并成k堆，再合并成1堆
            int next = process1(l, r, k, arr, k, presum, dp);
            if (next == -1) {
                dp[l][r][p] = -1;
                return -1;
            } else {
                // 成本 = 合并成k堆的成本 + 当前区间所有石头的总和
                dp[l][r][p] = next + presum[r + 1] - presum[l];
                return dp[l][r][p];
            }
        } else {
            // 合并成p堆（p > 1）：枚举分割点
            int ans = Integer.MAX_VALUE;
            
            // 分割点步长为k-1，这样左侧才能合并成1堆
            for (int mid = l; mid < r; mid += k - 1) {
                int next1 = process1(l, mid, 1, arr, k, presum, dp);          // 左边合并成1堆
                int next2 = process1(mid + 1, r, p - 1, arr, k, presum, dp); // 右边合并成p-1堆
                
                if (next1 != -1 && next2 != -1) {
                    ans = Math.min(ans, next1 + next2);
                } else {
                    dp[l][r][p] = -1;
                    return -1;
                }
            }
            dp[l][r][p] = ans;
            return ans;
        }
    }

    /**
     * 合并石头的最小成本主方法
     * 
     * @param stones 石头数组，stones[i]表示第i堆石头的数量
     * @param k 每次可以合并的堆数
     * @return 最小成本，如果无法合并返回-1
     */
    public static int mergeStones1(int[] stones, int k) {
        int n = stones.length;
        
        // 判断是否可能合并成一堆
        // 每次合并k堆变1堆，减少k-1堆
        // 要从n堆变成1堆，需要减少n-1堆
        // 所以需要(n-1)次"减少1堆"的操作，每次操作减少k-1堆
        if ((n - 1) % (k - 1) > 0) {
            return -1;
        }
        
        // 构建前缀和数组，便于快速计算区间和
        int[] presum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            presum[i + 1] = presum[i] + stones[i];
        }
        
        // DP表：dp[l][r][p] = 将[l,r]区间合并成p堆的最小成本
        int[][][] dp = new int[n][n][k + 1];
        
        // 开始递归求解：将整个数组合并成1堆的最小成本
        return process1(0, n - 1, 1, stones, k, presum, dp);
    }
}
