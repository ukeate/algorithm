package basic.c38;

/**
 * 货币组合问题
 * 
 * 问题描述：
 * 有两种类型的货币：
 * 1. 普通币：可以无限使用
 * 2. 纪念币：每种最多使用一个
 * 给定目标金额，求有多少种组合方式
 * 
 * 算法思路：
 * 1. 对普通币使用完全背包DP
 * 2. 对纪念币使用0-1背包DP
 * 3. 枚举普通币使用的金额，计算总方案数
 * 
 * 时间复杂度：O(n1*money + n2*money + money)
 * 空间复杂度：O(n1*money + n2*money)
 * 
 * @author 算法学习
 */
public class MoneyWays {
    
    /**
     * 计算普通币的完全背包DP
     * 普通币可以无限使用
     * 
     * @param arr 普通币面值数组
     * @param money 目标金额
     * @return dp[i][j] 表示前i种普通币凑成金额j的方案数
     *         如果数组为空返回null
     * 
     * 状态转移方程：dp[i][j] = dp[i-1][j] + dp[i][j-arr[i]]
     */
    private static int[][] getDp1(int[] arr, int money) {
        if (arr == null || arr.length == 0) {
            return null;
        }
        
        int[][] dp = new int[arr.length][money + 1];
        
        // 初始化：凑成0元的方案数都是1（什么都不选）
        for (int i = 0; i < arr.length; i++) {
            dp[i][0] = 1;
        }
        
        // 第一行：只用第一种普通币凑成各种金额
        for (int j = 1; arr[0] * j <= money; j++) {
            dp[0][arr[0] * j] = 1;
        }
        
        // 完全背包状态转移
        for (int i = 1; i < arr.length; i++) {
            for (int j = 1; j <= money; j++) {
                // 不使用当前面值的币
                dp[i][j] = dp[i - 1][j];
                // 使用当前面值的币（可以使用多次）
                dp[i][j] += j - arr[i] >= 0 ? dp[i][j - arr[i]] : 0;
            }
        }
        return dp;
    }

    /**
     * 计算纪念币的0-1背包DP
     * 每种纪念币最多使用一个
     * 
     * @param arr 纪念币面值数组
     * @param money 目标金额
     * @return dp[i][j] 表示前i种纪念币凑成金额j的方案数
     *         如果数组为空返回null
     * 
     * 状态转移方程：dp[i][j] = dp[i-1][j] + dp[i-1][j-arr[i]]
     */
    private static int[][] getDp2(int[] arr, int money) {
        if (arr == null || arr.length == 0) {
            return null;
        }
        
        int[][] dp = new int[arr.length][money + 1];
        
        // 初始化：凑成0元的方案数都是1
        for (int i = 0; i < arr.length; i++) {
            dp[i][0] = 1;
        }
        
        // 第一行：只考虑第一种纪念币
        if (arr[0] <= money) {
            dp[0][arr[0]] = 1;
        }
        
        // 0-1背包状态转移
        for (int i = 1; i < arr.length; i++) {
            for (int j = 1; j <= money; j++) {
                // 不使用当前纪念币
                dp[i][j] = dp[i - 1][j];
                // 使用当前纪念币（只能使用一次）
                dp[i][j] += j - arr[i] >= 0 ? dp[i - 1][j - arr[i]] : 0;
            }
        }
        return dp;
    }

    /**
     * 计算总的组合方案数
     * 
     * @param arr1 普通币面值数组（可重复使用）
     * @param arr2 纪念币面值数组（只能使用一次）
     * @param money 目标金额
     * @return 组合方案总数
     * 
     * 算法核心：
     * 枚举普通币使用的金额i（0到money），
     * 纪念币使用金额(money-i)，
     * 总方案数 = sum(普通币方案[i] * 纪念币方案[money-i])
     */
    public static int ways(int[] arr1, int[] arr2, int money) {
        // 特殊情况处理
        if (money < 0) {
            return 0;
        }
        if ((arr1 == null || arr1.length == 0) && (arr2 == null || arr2.length == 0)) {
            return money == 0 ? 1 : 0;
        }
        
        // 分别计算两种币的DP表
        int[][] dp1 = getDp1(arr1, money); // 普通币完全背包
        int[][] dp2 = getDp2(arr2, money); // 纪念币0-1背包
        
        // 只有一种币的情况
        if (dp1 == null) {
            return dp2[dp2.length - 1][money];
        }
        if (dp2 == null) {
            return dp1[dp1.length - 1][money];
        }
        
        // 枚举普通币使用的金额，计算总方案数
        int res = 0;
        for (int i = 0; i <= money; i++) {
            // 普通币用i元，纪念币用(money-i)元的方案数乘积
            res += dp1[dp1.length - 1][i] * dp2[dp2.length - 1][money - i];
        }
        return res;
    }
}
