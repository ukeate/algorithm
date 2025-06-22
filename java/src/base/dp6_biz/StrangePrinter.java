package base.dp6_biz;

/**
 * 奇怪的打印机问题
 * 有一台奇怪的打印机，每次可以在任意位置打印一种字符的连续序列
 * 给定目标字符串，求最少需要多少次打印操作
 * 
 * 这是一个区间DP问题，关键观察：
 * 1. 对于区间[l,r]，可以选择一个分割点k
 * 2. 如果str[l] == str[k]，那么可以在一次打印中同时打印这两个位置
 * 3. 状态转移：dp[l][r] = min(dp[l][k-1] + dp[k][r] - (str[l]==str[k] ? 1 : 0))
 */
public class StrangePrinter {
    /**
     * 方法1：基础递归解法
     * @param str 目标字符串
     * @param l 左边界
     * @param r 右边界
     * @return 区间[l,r]的最少打印次数
     */
    private static int process1(char[] str, int l, int r) {
        if (l == r) {
            return 1; // 单个字符需要1次打印
        }
        
        int ans = r - l + 1; // 最坏情况：每个字符单独打印
        
        // 尝试所有可能的分割点
        for (int k = l + 1; k <= r; k++) {
            // 核心思想：如果str[l] == str[k]，可以在一次打印操作中同时处理
            int cost = process1(str, l, k - 1) + process1(str, k, r);
            if (str[l] == str[k]) {
                cost -= 1; // 可以合并一次打印操作
            }
            ans = Math.min(ans, cost);
        }
        return ans;
    }

    /**
     * 方法1的对外接口
     */
    public static int min(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        return process1(str, 0, str.length - 1);
    }

    //

    /**
     * 方法2：带记忆化的递归解法
     * @param str 目标字符串
     * @param l 左边界
     * @param r 右边界
     * @param dp 记忆化数组
     * @return 最少打印次数
     */
    private static int process2(char[] str, int l, int r, int[][] dp) {
        if (dp[l][r] != 0) {
            return dp[l][r]; // 直接返回已计算的结果
        }
        
        int ans = r - l + 1;
        if (l == r) {
            ans = 1;
        } else {
            // 枚举分割点
            for (int k = l + 1; k <= r; k++) {
                int leftCost = process2(str, l, k - 1, dp);
                int rightCost = process2(str, k, r, dp);
                int totalCost = leftCost + rightCost;
                
                // 关键优化：相同字符可以在一次打印中处理
                if (str[l] == str[k]) {
                    totalCost -= 1;
                }
                ans = Math.min(ans, totalCost);
            }
        }
        
        dp[l][r] = ans;
        return ans;
    }

    /**
     * 方法2：记忆化搜索版本
     */
    public static int min2(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        int n = str.length;
        int[][] dp = new int[n][n]; // 0表示未计算
        return process2(str, 0, n - 1, dp);
    }

    //

    /**
     * 方法3：标准动态规划解法
     * 自底向上填充dp表，避免递归调用的开销
     */
    public static int min3(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        int n = str.length;
        int[][] dp = new int[n][n];
        
        // 初始化：单个字符的情况
        dp[n - 1][n - 1] = 1;
        for (int i = 0; i < n - 1; i++) {
            dp[i][i] = 1;
            // 相邻两个字符的情况
            dp[i][i + 1] = str[i] == str[i + 1] ? 1 : 2;
        }
        
        // 填表：从小区间到大区间
        for (int l = n - 3; l >= 0; l--) {
            for (int r = l + 2; r < n; r++) {
                dp[l][r] = r - l + 1; // 初始化为最坏情况
                
                // 尝试所有分割点
                for (int k = l + 1; k <= r; k++) {
                    int cost = dp[l][k - 1] + dp[k][r];
                    if (str[l] == str[k]) {
                        cost -= 1; // 合并打印操作
                    }
                    dp[l][r] = Math.min(dp[l][r], cost);
                }
            }
        }
        
        return dp[0][n - 1];
    }
}
