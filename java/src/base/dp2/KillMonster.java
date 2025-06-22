package base.dp2;

/**
 * 杀死怪物概率问题
 * 
 * 问题描述：
 * 一个怪物有n点血量。你有k次攻击机会，每次攻击造成随机伤害，
 * 伤害范围为[0, m]。计算k次攻击后怪物死亡（血量 <= 0）的概率。
 * 
 * 解法分析：
 * 1. 暴力递归：对于每次攻击，尝试所有可能的伤害值[0, m]
 * 2. 标准DP：使用2D DP表存储(剩余攻击次数, 当前血量)的结果
 * 3. 优化DP：使用滑动窗口技巧减少时间复杂度
 * 
 * 时间复杂度：
 * - 递归：O((m+1)^k) - 指数级
 * - DP1：O(k * n * m) - 标准DP带枚举
 * - DP2：O(k * n) - 滑动窗口优化
 * 
 * 空间复杂度：O(k * n)
 * 
 * @author Algorithm Practice
 */
public class KillMonster {
    
    /**
     * 递归方法计算杀死怪物的方法数
     * 
     * @param times 剩余攻击次数
     * @param m 每次攻击的最大伤害（伤害范围[0, m]）
     * @param hp 当前怪物血量
     * @return 在给定参数下杀死怪物的方法数
     */
    private static long process(int times, int m, int hp) {
        // base case：没有剩余攻击次数
        if (times == 0) {
            return hp <= 0 ? 1 : 0;  // 怪物死亡 = 1种方法，存活 = 0种方法
        }
        
        // base case：怪物已经死亡
        if (hp <= 0) {
            // 所有剩余攻击都可以造成任意伤害[0, m]，所以有(m+1)^times种方法
            return (long) Math.pow(m + 1, times);
        }
        
        long ways = 0;
        // 尝试当前攻击的所有可能伤害值
        for (int i = 0; i <= m; i++) {
            ways += process(times - 1, m, hp - i);
        }
        return ways;
    }

    /**
     * 使用递归方法计算杀死怪物的概率
     * 
     * @param n 怪物初始血量
     * @param m 每次攻击最大伤害
     * @param k 总攻击次数
     * @return 杀死怪物的概率（0.0到1.0）
     */
    // n点血, 每次掉[0,m], 打k次
    public static double possibility(int n, int m, int k) {
        if (n < 1 || m < 1 || k < 1) {
            return 0;
        }
        // 概率 = 成功方法数 / 总可能方法数
        // 总方法数 = (m+1)^k，因为每次攻击有(m+1)种可能结果
        return (double) process(k, m, n) / Math.pow(m + 1, k);
    }

    /**
     * 动态规划解法 - 标准方法
     * 
     * @param n 怪物初始血量
     * @param m 每次攻击最大伤害
     * @param k 总攻击次数
     * @return 杀死怪物的概率（0.0到1.0）
     */
    public static double dp1(int n, int m, int k) {
        if (n < 1 || m < 1 || k < 1) {
            return 0;
        }
        
        // dp[i][j] = 有i次攻击且怪物有j血量时杀死怪物的方法数
        long[][] dp = new long[k + 1][n + 1];
        dp[0][0] = 1;  // base case：0次攻击，0血量 = 1种方法（已经死亡）
        
        // 填充DP表
        for (int times = 1; times <= k; times++) {
            // 如果怪物有0血量，所有剩余攻击都是自由选择
            dp[times][0] = (long) Math.pow(m + 1, times);
            
            for (int hp = 1; hp <= n; hp++) {
                long ways = 0;
                // 尝试所有可能的伤害值[0, m]
                for (int i = 0; i <= m; i++) {
                    if (hp - i >= 0) {
                        ways += dp[times - 1][hp - i];
                    } else {
                        // 怪物死亡，剩余攻击是自由选择
                        ways += (long) Math.pow(m + 1, times - 1);
                    }
                }
                dp[times][hp] = ways;
            }
        }
        return ((double) dp[k][n] / Math.pow(m + 1, k));
    }

    /**
     * 使用滑动窗口技巧的优化动态规划解法
     * 
     * 关键思路：dp[i][j]可以使用滑动窗口和计算：
     * dp[i][j] = dp[i][j-1] + dp[i-1][j] - dp[i-1][j-1-m] （带边界处理）
     * 
     * 这消除了内层枚举循环，降低了时间复杂度。
     * 
     * @param n 怪物初始血量
     * @param m 每次攻击最大伤害
     * @param k 总攻击次数
     * @return 杀死怪物的概率（0.0到1.0）
     */
    public static double dp2(int n, int m, int k) {
        if (n < 1 || m < 1 || k < 1) {
            return 0;
        }
        
        // dp[i][j] = 有i次攻击且怪物有j血量时杀死怪物的方法数
        long[][] dp = new long[k + 1][n + 1];
        dp[0][0] = 1;  // base case：0次攻击，0血量 = 1种方法
        
        for (int times = 1; times <= k; times++) {
            // 如果怪物有0血量，所有剩余攻击都是自由选择
            dp[times][0] = (long) Math.pow(m + 1, times);
            
            for (int hp = 1; hp <= n; hp++) {
                // 使用滑动窗口技巧高效计算和
                long ways = dp[times][hp - 1] + dp[times - 1][hp];
                
                // 减去不再在窗口内的值
                if (hp - 1 - m >= 0) {
                    ways -= dp[times - 1][hp - 1 - m];
                } else {
                    // 被减去的部分代表怪物已经死亡的情况
                    ways -= Math.pow(m + 1, times - 1);
                }
                
                dp[times][hp] = ways;
            }
        }
        return (double) dp[k][n] / (double) Math.pow(m + 1, k);
    }

    /**
     * 测试方法验证所有方法给出相同结果
     */
    public static void main(String [] args) {
        int times = 1000;
        int maxN = 10;
        int maxM = 10;
        int maxK = 10;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int n = (int) ((maxN + 1) * Math.random());
            int m = (int) ((maxM + 1) * Math.random());
            int k = (int) ((maxK + 1) * Math.random());
            double ans1 = possibility(n, m, k);
            double ans2 = dp1(n, m, k);
            double ans3 = dp2(n, m, k);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2 + "|" + ans3);
                break;
            }
        }
        System.out.println("test end");
    }
}
