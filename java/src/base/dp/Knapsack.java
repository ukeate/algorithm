package base.dp;

/**
 * 0-1背包问题
 * 
 * 问题描述：
 * 给定两个长度都为N的数组weights和values，weights[i]和values[i]分别代表第i个物品的重量和价值。
 * 给定一个正数bag，表示背包能装重量不超过bag的物品。
 * 返回你能装下最多价值的物品。
 * 
 * 解法分析：
 * 1. 暴力递归：对于每个物品，有选择和不选择两种决策，递归尝试所有可能性
 * 2. 动态规划：使用二维数组dp[i][rest]表示从第i个物品开始，背包剩余容量为rest时能获得的最大价值
 * 
 * 时间复杂度：O(N * bag)
 * 空间复杂度：O(N * bag)
 */
public class Knapsack {
    
    /**
     * 暴力递归解法
     * 
     * @param w 物品重量数组
     * @param v 物品价值数组
     * @param idx 当前考虑的物品索引
     * @param rest 背包剩余容量
     * @return 能获得的最大价值，如果无法装下返回-1
     */
    private static int process(int[] w, int[] v, int idx, int rest) {
        // 如果剩余容量为负数，说明无法装下
        if (rest < 0) {
            return -1;
        }
        
        // base case：所有物品都考虑完了
        if (idx == w.length) {
            return 0;
        }
        
        // 不选择当前物品
        int p1 = process(w, v, idx + 1, rest);
        
        // 选择当前物品
        int p2 = 0;
        int next = process(w, v, idx + 1, rest - w[idx]);
        if (next != -1) {  // 如果能装下当前物品
            p2 = v[idx] + next;
        }
        
        return Math.max(p1, p2);
    }
    
    /**
     * 暴力递归解法入口
     * 
     * @param w 物品重量数组
     * @param v 物品价值数组
     * @param bag 背包容量
     * @return 能获得的最大价值
     */
    public static int maxVal(int[] w, int[] v, int bag) {
        if (w == null || v == null || w.length != v.length || w.length == 0) {
            return 0;
        }
        return process(w, v, 0, bag);
    }

    /**
     * 动态规划解法
     * 
     * dp[i][rest]表示从第i个物品开始考虑，背包剩余容量为rest时能获得的最大价值
     * 
     * @param w 物品重量数组
     * @param v 物品价值数组
     * @param bag 背包容量
     * @return 能获得的最大价值
     */
    public static int dp(int[] w, int[] v, int bag) {
        if (w == null || v == null || w.length != v.length || w.length == 0) {
            return 0;
        }
        
        int n = w.length;
        // dp[i][rest]表示从第i个物品开始，剩余容量为rest时的最大价值
        int[][] dp = new int[n + 1][bag + 1];
        
        // base case：没有物品可选择时，价值为0（dp数组默认初始化为0）
        
        // 从最后一个物品开始填充dp表
        for (int i = n - 1; i >= 0; i--) {
            for (int rest = 0; rest <= bag; rest++) {
                // 不选择当前物品
                int p1 = dp[i + 1][rest];
                
                // 选择当前物品（如果能装下）
                int p2 = 0;
                int next = rest - w[i] < 0 ? -1 : dp[i + 1][rest - w[i]];
                if (next != -1) {
                    p2 = v[i] + next;
                }
                
                dp[i][rest] = Math.max(p1, p2);
            }
        }
        
        return dp[0][bag];
    }

    public static void main(String[] args) {
        int[] w = {3, 2, 4, 7, 3, 1, 7};
        int[] v = {5, 6, 3, 19, 12, 4, 2};
        int bag = 15;
        System.out.println(maxVal(w, v, bag));
        System.out.println(dp(w, v, bag));
    }
}
