package basic.c38;

/**
 * 怪兽游戏最小通关费用问题
 * 
 * 问题描述：
 * 给定一个怪兽数组，每个怪兽有两个属性：武力值d[i]和贿赂费用p[i]
 * 玩家初始能力为0，可以选择：
 * 1. 如果能力值 >= 怪兽武力值，可以直接击败（能力值不变）
 * 2. 可以花费p[i]贿赂怪兽，然后能力值增加d[i]
 * 
 * 求通关所有怪兽所需的最小费用
 * 
 * 算法思路：
 * 方法1：递归暴力枚举所有可能性 - O(2^N)
 * 方法2：以能力值为状态的动态规划 - O(N*sum(d))
 * 方法3：以花费为状态的动态规划 - O(N*sum(p))
 * 
 * @author 算法学习
 */
public class MoneyMonster {
    
    /**
     * 方法1：递归暴力解法
     * 对每个怪兽尝试所有可能的选择
     * 
     * @param d 怪兽武力值数组
     * @param p 贿赂费用数组
     * @param ability 当前玩家能力值
     * @param idx 当前处理的怪兽索引
     * @return 从当前状态开始通关所需的最小费用
     * 
     * 时间复杂度：O(2^N)
     * 空间复杂度：O(N) - 递归栈深度
     */
    private static long process(int[] d, int[] p, int ability, int idx) {
        // 递归边界：所有怪兽都处理完毕
        if (idx == d.length) {
            return 0;
        }
        
        // 如果能力值不足以击败当前怪兽，必须贿赂
        if (ability < d[idx]) {
            return p[idx] + process(d, p, ability + d[idx], idx + 1);
        } else {
            // 能力值足够，可以选择贿赂或直接击败
            // 贿赂：花费p[idx]，能力值增加d[idx]
            // 直接击败：不花费，能力值不变
            return Math.min(p[idx] + process(d, p, ability + d[idx], idx + 1),
                    process(d, p, ability, idx + 1));
        }
    }

    /**
     * 方法1：递归暴力解法的入口
     * 
     * @param d 怪兽武力值数组
     * @param p 贿赂费用数组
     * @return 通关最小费用
     */
    public static long min1(int[] d, int[] p) {
        return process(d, p, 0, 0);
    }

    /**
     * 方法2：以能力值为状态的动态规划优化
     * dp[i][j] 表示从第i个怪兽开始，当前能力值为j时的最小通关费用
     * 
     * @param d 怪兽武力值数组
     * @param p 贿赂费用数组
     * @return 通关最小费用
     * 
     * 时间复杂度：O(N * sum(d))
     * 空间复杂度：O(N * sum(d))
     */
    public static long min2(int[] d, int[] p) {
        // 计算所有怪兽武力值之和作为能力值的上界
        int sum = 0;
        for (int num : d) {
            sum += num;
        }
        
        // dp[cur][hp] = 从第cur个怪兽开始，当前能力值为hp的最小通关费用
        long[][] dp = new long[d.length + 1][sum + 1];
        
        // 从后往前填表
        for (int cur = d.length - 1; cur >= 0; cur--) {
            for (int hp = 0; hp <= sum; hp++) {
                // 防止数组越界
                if (hp + d[cur] > sum) {
                    continue;
                }
                
                if (hp < d[cur]) {
                    // 能力值不足，必须贿赂
                    dp[cur][hp] = p[cur] + dp[cur + 1][hp + d[cur]];
                } else {
                    // 能力值足够，选择最优策略
                    dp[cur][hp] = Math.min(p[cur] + dp[cur + 1][hp + d[cur]], 
                                          dp[cur + 1][hp]);
                }
            }
        }
        return dp[0][0];
    }

    /**
     * 方法3：以花费为状态的动态规划优化
     * dp[i][j] 表示考虑前i个怪兽，花费j元钱能获得的最大能力值
     * 
     * @param d 怪兽武力值数组
     * @param p 贿赂费用数组
     * @return 通关最小费用
     * 
     * 时间复杂度：O(N * sum(p))
     * 空间复杂度：O(N * sum(p))
     */
    public static long min3(int[] d, int[] p) {
        // 计算所有贿赂费用之和作为花费的上界
        int sum = 0;
        for (int num : p) {
            sum += num;
        }
        
        // dp[i][j] = 考虑前i个怪兽，花费j元能获得的最大能力值
        // -1表示该状态无法达到
        int[][] dp = new int[d.length][sum + 1];
        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j <= sum; j++) {
                dp[i][j] = -1;
            }
        }
        
        // 初始状态：第0个怪兽，花费p[0]获得能力值d[0]
        dp[0][p[0]] = d[0];
        
        // 状态转移
        for (int i = 1; i < d.length; i++) {
            for (int j = 0; j <= sum; j++) {
                // 选择贿赂当前怪兽
                if (j >= p[i] && dp[i - 1][j - p[i]] != -1) {
                    dp[i][j] = dp[i - 1][j - p[i]] + d[i];
                }
                // 选择直接击败当前怪兽（如果能力值足够）
                if (dp[i - 1][j] >= d[i]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j]);
                }
            }
        }
        
        // 找到能通关的最小花费
        int ans = 0;
        for (int j = 0; j <= sum; j++) {
            if (dp[d.length - 1][j] != -1) {
                ans = j;
                break;
            }
        }
        return ans;
    }

    /**
     * 生成随机测试数据
     * 
     * @param maxLen 数组最大长度
     * @param maxVal 数值最大值
     * @return 包含武力值数组和费用数组的二维数组
     */
    private static int[][] randomArr(int maxLen, int maxVal) {
        int size = (int) (maxLen * Math.random()) + 1;
        int[][] arrs = new int[2][size];
        for (int i = 0; i < size; i++) {
            arrs[0][i] = (int) (maxVal * Math.random()) + 1; // 武力值
            arrs[1][i] = (int) (maxVal * Math.random()) + 1; // 贿赂费用
        }
        return arrs;
    }

    /**
     * 测试方法：验证三种算法的正确性
     */
    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 10;
        int maxVal = 20;
        System.out.println("test begin");
        
        // 随机测试验证算法正确性
        for (int i = 0; i < times; i++) {
            int[][] arrs = randomArr(maxLen, maxVal);
            int[] d = arrs[0]; // 怪兽武力值数组
            int[] p = arrs[1]; // 贿赂费用数组
            
            long ans1 = min1(d, p); // 递归暴力解法
            long ans2 = min2(d, p); // DP方法2
            long ans3 = min3(d, p); // DP方法3
            
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
