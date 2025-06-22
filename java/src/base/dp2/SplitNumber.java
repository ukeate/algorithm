package base.dp2;

/**
 * 整数分拆问题（Integer Partition）
 * 
 * 问题描述：
 * 给定一个正整数n，求有多少种方法可以将n表示为若干个正整数的和，
 * 且这些正整数必须按升序排列（即分拆中的数必须非递减）。
 * 
 * 例如：n = 4 的分拆方案有：
 * 4 = 4
 * 4 = 1 + 3
 * 4 = 2 + 2
 * 4 = 1 + 1 + 2
 * 4 = 1 + 1 + 1 + 1
 * 总共5种方案
 * 
 * 解法分析：
 * 1. 递归思路：process(pre, rest)表示在前一个数至少为pre的情况下，分拆rest的方法数
 * 2. 动态规划1：直接根据递归改写，三重循环
 * 3. 动态规划2：优化状态转移，利用递推关系
 * 
 * 时间复杂度：O(n³) -> O(n²)
 * 空间复杂度：O(n²)
 */
public class SplitNumber {
    
    /**
     * 暴力递归解法
     * 
     * @param pre 前一个选择的数字（保证升序）
     * @param rest 剩余需要分拆的数
     * @return 分拆rest的方法数
     */
    private static int process(int pre, int rest) {
        // base case：剩余数字为0，找到一种分拆方案
        if (rest == 0) {
            return 1;
        }
        
        // 如果前一个数字大于剩余数字，无法继续分拆
        if (pre > rest) {
            return 0;
        }
        
        int ways = 0;
        
        // 尝试所有可能的下一个数字（从pre开始，保证升序）
        for (int first = pre; first <= rest; first++) {
            ways += process(first, rest - first);
        }
        
        return ways;
    }

    /**
     * 暴力递归解法入口
     * 
     * @param n 要分拆的正整数
     * @return 分拆方案数
     */
    public static int ways(int n) {
        if (n < 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return process(1, n);  // 从1开始分拆n
    }

    /**
     * 动态规划解法1（直接翻译递归）
     * 
     * dp[pre][rest]表示在前一个数至少为pre的情况下，分拆rest的方法数
     * 
     * @param n 要分拆的正整数
     * @return 分拆方案数
     */
    public static int dp1(int n) {
        if (n < 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        
        // dp[pre][rest]表示前一个数为pre，剩余rest的分拆方法数
        int[][] dp = new int[n + 1][n + 1];
        
        // base case：rest为0时，有1种方法（什么都不选）
        for (int pre = 1; pre <= n; pre++) {
            dp[pre][0] = 1;
            dp[pre][pre] = 1;  // 剩余数字等于前一个数字时，只能选这个数字
        }
        
        // 从大到小填充DP表（依赖于更大的pre值）
        for (int pre = n - 1; pre >= 1; pre--) {
            for (int rest = pre + 1; rest <= n; rest++) {
                int ways = 0;
                
                // 枚举所有可能的下一个数字
                for (int first = pre; first <= rest; first++) {
                    ways += dp[first][rest - first];
                }
                
                dp[pre][rest] = ways;
            }
        }
        
        return dp[1][n];
    }

    /**
     * 动态规划解法2（优化状态转移）
     * 
     * 关键优化：
     * dp[pre][rest] = dp[pre+1][rest] + dp[pre][rest-pre]
     * 
     * 解释：
     * - dp[pre+1][rest]：不选择pre，从pre+1开始分拆rest
     * - dp[pre][rest-pre]：选择pre，然后继续分拆rest-pre（下一个数至少还是pre）
     * 
     * @param n 要分拆的正整数
     * @return 分拆方案数
     */
    public static int dp2(int n) {
        if (n < 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        
        int[][] dp = new int[n + 1][n + 1];
        
        // base case初始化
        for (int pre = 1; pre <= n; pre++) {
            dp[pre][0] = 1;
            dp[pre][pre] = 1;
        }
        
        // 利用递推关系填充DP表
        for (int pre = n - 1; pre >= 1; pre--) {
            for (int rest = pre + 1; rest <= n; rest++) {
                // 优化后的状态转移方程
                dp[pre][rest] = dp[pre + 1][rest] + dp[pre][rest - pre];
            }
        }
        
        return dp[1][n];
    }

    public static void main(String[] args) {
        int test = 39;
        System.out.println(ways(test));
        System.out.println(dp1(test));
        System.out.println(dp2(test));
    }
}
