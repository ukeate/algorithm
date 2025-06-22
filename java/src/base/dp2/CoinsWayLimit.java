package base.dp2;

import java.util.HashMap;
import java.util.Map;

/**
 * 有限数量硬币组合问题
 * 
 * 问题描述：
 * 给定一个硬币数组（可能有重复）和一个目标金额，
 * 找出组成目标金额的方法数。每个硬币只能使用数组中出现的次数。
 * 
 * 解法分析：
 * 1. 暴力递归：尝试每种硬币使用次数的所有可能组合
 * 2. 标准DP：转换为唯一硬币及其数量，使用2D DP
 * 3. 优化DP：使用滑动窗口技巧减少时间复杂度
 * 
 * 时间复杂度：
 * - 递归：O(k^n) 其中k是平均硬币数量
 * - DP1：O(硬币种类 * 金额 * 平均数量)
 * - DP2：O(硬币种类 * 金额) 带滑动窗口优化
 * 
 * 空间复杂度：O(硬币种类 * 金额)
 * 
 * @author Algorithm Practice
 */
// 可重复，限张数, 求方法数
public class CoinsWayLimit {
    
    /**
     * 用于存储唯一硬币及其数量的辅助类
     */
    private static class Info {
        public int[] coins;  // 唯一硬币面值
        public int[] nums;   // 每种硬币的数量
        
        public Info(int[] c, int[] n) {
            coins = c;
            nums = n;
        }
    }

    /**
     * 将硬币数组转换为唯一硬币及其数量
     * 
     * @param arr 原始硬币数组（可能有重复）
     * @return 包含唯一硬币及其数量的Info对象
     */
    private static Info getInfo(int[] arr) {
        // 统计每种硬币面值的频次
        HashMap<Integer, Integer> counts = new HashMap<>();
        for (int val : arr) {
            if (!counts.containsKey(val)) {
                counts.put(val, 1);
            } else {
                counts.put(val, counts.get(val) + 1);
            }
        }
        
        // 转换为数组
        int n = counts.size();
        int[] coins = new int[n];
        int[] nums = new int[n];
        int idx = 0;
        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            coins[idx] = entry.getKey();
            nums[idx++] = entry.getValue();
        }
        return new Info(coins, nums);
    }

    /**
     * 递归方法计算组成目标金额的方法数
     * 
     * @param coins 唯一硬币面值数组
     * @param nums 硬币数量数组
     * @param idx 当前硬币类型索引
     * @param rest 剩余需要组成的金额
     * @return 组成剩余金额的方法数
     */
    private static int process(int[] coins, int[] nums, int idx, int rest) {
        // base case：处理完所有硬币类型
        if (idx == coins.length) {
            return rest == 0 ? 1 : 0;  // 恰好组成目标金额则为1种方法，否则为0
        }
        
        int ways = 0;
        // 尝试使用0到nums[idx]个当前类型的硬币
        for (int num = 0; num * coins[idx] <= rest && num <= nums[idx]; num++) {
            ways += process(coins, nums, idx + 1, rest - (num * coins[idx]));
        }
        return ways;
    }

    /**
     * 使用递归方法计算组成目标金额的方法数
     * 
     * @param arr 硬币数组（可能包含重复）
     * @param aim 目标金额
     * @return 组成目标金额的方法数
     */
    public static int ways(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return 0;
        }
        Info info = getInfo(arr);
        return process(info.coins, info.nums, 0, aim);
    }

    /**
     * 动态规划解法 - 标准方法
     * 
     * @param arr 硬币数组（可能包含重复）
     * @param aim 目标金额
     * @return 组成目标金额的方法数
     */
    public static int dp1(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return 0;
        }

        Info info = getInfo(arr);
        int[] coins = info.coins;
        int[] nums = info.nums;
        int n = coins.length;
        
        // dp[i][j] = 使用索引i及之后的硬币组成金额j的方法数
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 1;  // base case：组成0金额有1种方法
        
        // 自底向上填充DP表
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int rest = 0; rest <= aim; rest++) {
                int ways = 0;
                // 尝试使用0到nums[idx]个当前类型的硬币
                for (int num = 0; num * coins[idx] <= rest && num <= nums[idx]; num++) {
                    ways += dp[idx + 1][rest - num * coins[idx]];
                }
                dp[idx][rest] = ways;
            }
        }
        return dp[0][aim];
    }

    /**
     * 使用滑动窗口技巧的优化动态规划解法
     * 
     * 关键思路：dp[i][j]可以通过dp[i][j-coin]和dp[i+1][j]计算
     * 使用滑动窗口仔细处理硬币数量限制。
     * 
     * @param arr 硬币数组（可能包含重复）
     * @param aim 目标金额
     * @return 组成目标金额的方法数
     */
    public static int dp2(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return 0;
        }
        Info info = getInfo(arr);
        int[] coins = info.coins;
        int[] nums = info.nums;
        int n = coins.length;
        
        // dp[i][j] = 使用索引i及之后的硬币组成金额j的方法数
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 1;  // base case：组成0金额有1种方法
        
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int rest = 0; rest <= aim; rest++) {
                // 从不使用当前硬币类型的方法数开始
                int ways = dp[idx + 1][rest];
                
                // 加上使用当前硬币类型的方法数（如果可能）
                if (rest - coins[idx] >= 0) {
                    ways += dp[idx][rest - coins[idx]];
                }
                
                // 减去过度计数的方法数（使用超过nums[idx]个硬币）
                if (rest - coins[idx] * (nums[idx] + 1) >= 0) {
                    ways -= dp[idx + 1][rest - coins[idx] * (nums[idx] + 1)];
                }
                
                dp[idx][rest] = ways;
            }
        }
        return dp[0][aim];
    }

    /**
     * 生成随机硬币数组用于测试
     */
    public static int[] randomArr(int maxLen, int maxVal) {
        int n = (int) ((maxLen + 1) * Math.random());
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
                arr[i] = (int) ((maxVal + 1) * Math.random()) + 1;
        }
        return arr;
    }

    /**
     * 打印数组用于调试
     */
    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    /**
     * 测试方法验证所有方法给出相同结果
     */
    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 10;
        int maxVal = 30;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int aim = (int) ((maxVal + 1) * Math.random());
            int ans1 = ways(arr, aim);
            int ans2 = dp1(arr, aim);
            int ans3 = dp2(arr, aim);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong");
                print(arr);
                System.out.println(aim + "|" + ans1 + "|" + ans2);
                break;
            }
        }
        System.out.println("test end");
    }
}
