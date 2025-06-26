package basic.c38;

import java.util.Scanner;

/**
 * NowCoder平台怪兽游戏问题
 * 题目链接：https://www.nowcoder.com/questionTerminal/93bcd2190da34099b98dfc9a9fb77984
 * 
 * 问题描述：
 * 有两组物品，第一组可以无限使用，第二组只能使用一次
 * 求凑成指定金额的方案数
 * 
 * 输入：
 * - n1: 第一组物品数量
 * - n2: 第二组物品数量  
 * - m: 目标金额
 * - arr1[]: 第一组物品价值（可重复使用）
 * - arr2[]: 第二组物品价值（只能使用一次）
 * 
 * 输出：组合方案数（mod 1000000007）
 * 
 * 算法思路：
 * 1. getDp1：计算第一组物品的完全背包DP
 * 2. getDp2：计算第二组物品的0-1背包DP
 * 3. 枚举第一组使用的金额，计算总方案数
 * 
 * 时间复杂度：O(n1*m + n2*m + m)
 * 空间复杂度：O(n1*m + n2*m)
 * 
 * @author 算法学习
 */
public class MoneyMonsterNowCoder {
    
    /**
     * 主函数：处理输入输出
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner((System.in));
        int n1 = sc.nextInt(); // 第一组物品数量
        int n2 = sc.nextInt(); // 第二组物品数量
        int m = sc.nextInt();  // 目标金额
        
        // 读取第一组物品价值
        int[] arr1 = new int[n1];
        int[] arr2 = new int[n2];
        for (int i = 0; i < n1; i++) {
            arr1[i] = sc.nextInt();
        }
        
        // 读取第二组物品价值
        for (int i = 0; i < n2; i++) {
            arr2[i] = sc.nextInt();
        }
        
        System.out.println(ways(arr1, arr2, m));
        sc.close();
    }

    /** 模数常量，用于防止溢出 */
    public static final long mod = 1000000007;

    /**
     * 计算第一组物品的完全背包DP
     * 每个物品可以使用无限次
     * 
     * @param arr 物品价值数组
     * @param money 目标金额
     * @return dp[i][j] 表示前i个物品凑成金额j的方案数
     * 
     * 状态转移方程：dp[i][j] = dp[i-1][j] + dp[i][j-arr[i]]
     */
    public static long[][] getDp1(int[] arr, int money) {
        long[][] dp = new long[arr.length][money + 1];
        
        // 初始化：凑成0元的方案数都是1（什么都不选）
        for (int i = 0; i < arr.length; i++) {
            dp[i][0] = 1;
        }
        
        // 第一行：只用第一个物品凑成各种金额的方案数
        for (int j = 1; arr[0] * j <= money; j++) {
            dp[0][arr[0] * j] = 1;
        }
        
        // 状态转移：完全背包
        for (int i = 1; i < arr.length; i++) {
            for (int j = 1; j <= money; j++) {
                // 不选当前物品 + 选当前物品（可以选多次）
                dp[i][j] = (dp[i - 1][j] + (j - arr[i] >= 0 ? dp[i][j - arr[i]] : 0)) % mod;
            }
        }
        return dp;
    }

    /**
     * 计算第二组物品的0-1背包DP
     * 每个物品最多使用一次
     * 
     * @param arr 物品价值数组
     * @param money 目标金额
     * @return dp[i][j] 表示前i个物品凑成金额j的方案数
     * 
     * 状态转移方程：dp[i][j] = dp[i-1][j] + dp[i-1][j-arr[i]]
     */
    public static long[][] getDp2(int[] arr, int money) {
        long[][] dp = new long[arr.length][money + 1];
        
        // 初始化：凑成0元的方案数都是1
        for (int i = 0; i < arr.length; i++) {
            dp[i][0] = 1;
        }
        
        // 第一行：只用第一个物品
        if (arr[0] <= money) {
            dp[0][arr[0]] = 1;
        }
        
        // 状态转移：0-1背包
        for (int i = 1; i < arr.length; i++) {
            for (int j = 1; j <= money; j++) {
                // 不选当前物品 + 选当前物品（只能选一次）
                dp[i][j] = (dp[i - 1][j] + (j - arr[i] >= 0 ? dp[i - 1][j - arr[i]] : 0)) % mod;
            }
        }
        return dp;
    }

    /**
     * 计算总的组合方案数
     * 
     * @param arr1 第一组物品（可重复使用）
     * @param arr2 第二组物品（只能使用一次）
     * @param money 目标金额
     * @return 组合方案总数
     * 
     * 核心思想：
     * 枚举第一组物品使用的金额i（0到money），
     * 第二组物品使用金额(money-i)，
     * 总方案数 = sum(dp1[末行][i] * dp2[末行][money-i])
     */
    public static int ways(int[] arr1, int[] arr2, int money) {
        // 分别计算两组物品的DP表
        long[][] dp1 = getDp1(arr1, money); // 完全背包
        long[][] dp2 = getDp2(arr2, money); // 0-1背包
        
        long res = 0;
        // 枚举第一组使用的金额
        for (int i = 0; i <= money; i++) {
            // 第一组用i元，第二组用(money-i)元的方案数乘积
            res = (res + dp1[dp1.length - 1][i] * dp2[dp2.length - 1][money - i]) % mod;
        }
        return (int) res;
    }
}
