package base.dp2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 有限数量硬币的最少硬币问题
 * 
 * 问题描述：
 * 给定一个硬币数组（可能有重复）和一个目标金额，
 * 找出组成目标金额所需的最少硬币数。每个硬币只能使用数组中出现的次数。
 * 
 * 解法分析：
 * 1. 简单情况（唯一硬币，每个1张）：基本的0/1背包方法
 * 2. 一般情况（有重复）：转换为唯一硬币及其数量
 * 3. 标准DP：O(硬币种类 * 金额 * 平均数量)
 * 4. 优化DP：使用滑动窗口技巧 O(硬币种类 * 金额)
 * 
 * 时间复杂度：
 * - 递归：指数级
 * - DP2：O(硬币种类 * 金额 * 平均数量)
 * - DP3：O(硬币种类 * 金额) 带滑动窗口优化
 * 
 * 空间复杂度：O(硬币种类 * 金额)
 * 
 * @author Algorithm Practice
 */
// (不重复,只1张)/(可重复，限张数)，求最小张数
public class MinCoinsLimit {
    
    /**
     * 简单情况的递归方法（每个硬币恰好出现一次）
     * 
     * @param arr 唯一硬币数组
     * @param idx 当前硬币索引
     * @param rest 剩余需要组成的金额
     * @return 所需最少硬币数，无法组成返回Integer.MAX_VALUE
     */
    private static int process(int[] arr, int idx, int rest) {
        // 无效情况：负金额
        if (rest < 0) {
            return Integer.MAX_VALUE;
        }
        
        // base case：处理完所有硬币
        if (idx == arr.length) {
            return rest == 0 ? 0 : Integer.MAX_VALUE;
        }
        
        // 选择1：不使用当前硬币
        int p1 = process(arr, idx + 1, rest);
        
        // 选择2：使用当前硬币（如果可能）
        int p2 = process(arr, idx + 1, rest - arr[idx]);
        if (p2 != Integer.MAX_VALUE) {
            p2++;  // 使用当前硬币加1
        }
        
        return Math.min(p1, p2);
    }

    /**
     * 使用递归方法寻找简单情况的最少硬币数
     * 
     * @param arr 唯一硬币数组（每个出现一次）
     * @param aim 目标金额
     * @return 所需最少硬币数
     */
    public static int minCoins(int[] arr, int aim) {
        return process(arr, 0, aim);
    }

    /**
     * 简单情况的动态规划解法
     * 
     * @param arr 唯一硬币数组（每个出现一次）
     * @param aim 目标金额
     * @return 所需最少硬币数
     */
    public static int dp1(int[] arr, int aim) {
        if (aim == 0) {
            return 0;
        }
        int n = arr.length;
        
        // dp[i][j] = 使用索引i及之后的硬币组成金额j的最少硬币数
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 0;  // base case：组成0金额需要0个硬币
        for (int j = 1; j <= aim; j++) {
            dp[n][j] = Integer.MAX_VALUE;  // 没有硬币时无法组成
        }
        
        // 自底向上填充DP表
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int rest = 0; rest <= aim; rest++) {
                // 选择1：不使用当前硬币
                int p1 = dp[idx + 1][rest];
                
                // 选择2：使用当前硬币（如果可能）
                int p2 = rest - arr[idx] >= 0 ? dp[idx + 1][rest - arr[idx]] : Integer.MAX_VALUE;
                if (p2 != Integer.MAX_VALUE) {
                    p2++;
                }
                
                dp[idx][rest] = Math.min(p1, p2);
            }
        }
        return dp[0][aim];
    }

    /**
     * 用于存储唯一硬币及其数量的辅助类
     */
    private static class Info {
        public int[] coins;  // 唯一硬币面值
        public int[] nums;   // 每种硬币的数量

        public Info(int[] c, int[] z) {
            coins = c;
            nums = z;
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
     * 有限硬币数量一般情况的标准DP解法
     * 时间复杂度：O(硬币种类 * 金额 * 平均数量)
     * 
     * @param arr 硬币数组（可能包含重复）
     * @param aim 目标金额
     * @return 所需最少硬币数
     */
    // O(arr长度) + O(货币种数 * aim * 货币平均张数)
    public static int dp2(int[] arr, int aim) {
        if (aim == 0) {
            return 0;
        }
        Info info = getInfo(arr);
        int[] coins = info.coins;
        int[] nums = info.nums;
        int n = coins.length;
        
        // dp[i][j] = 使用索引i及之后的硬币类型组成金额j的最少硬币数
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 0;  // base case
        for (int j = 1; j <= aim; j++) {
            dp[n][j] = Integer.MAX_VALUE;  // 没有硬币时无法组成
        }
        
        for (int i = n - 1; i >= 0; i--) {
            for (int rest = 0; rest <= aim; rest++) {
                // 从不使用当前硬币类型开始
                dp[i][rest] = dp[i + 1][rest];
                
                // 尝试使用1到nums[i]个当前类型的硬币
                for (int num = 1; num * coins[i] <= rest && num <= nums[i]; num++) {
                    if (dp[i + 1][rest - num * coins[i]] != Integer.MAX_VALUE) {
                        dp[i][rest] = Math.min(dp[i][rest], num + dp[i + 1][rest - num * coins[i]]);
                    }
                }
            }
        }
        return dp[0][aim];
    }

    /**
     * 计算滑动窗口中使用的硬币数的辅助方法
     * 
     * @param pre 前一个金额
     * @param cur 当前金额
     * @param coin 硬币面值
     * @return 使用的这种硬币的数量
     */
    private static int num(int pre, int cur, int coin) {
        return (cur - pre) / coin;
    }

    /**
     * 使用滑动窗口技巧的优化DP解法
     * 时间复杂度：O(硬币种类 * 金额)
     * 
     * 这个优化利用了对每种硬币类型，我们可以根据金额除以硬币面值的余数
     * 将金额分组处理。在每组内，我们使用滑动窗口高效计算最小值。
     * 
     * @param arr 硬币数组（可能包含重复）
     * @param aim 目标金额
     * @return 所需最少硬币数
     */
    // O(arr长度) + O(货币种数 * aim)
    public static int dp3(int[] arr, int aim) {
        if (aim == 0) {
            return 0;
        }
        Info info = getInfo(arr);
        int[] c = info.coins;
        int[] z = info.nums;
        int n = c.length;
        
        // dp[i][j] = 使用索引i及之后的硬币类型组成金额j的最少硬币数
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 0;
        for (int j = 1; j <= aim; j++) {
            dp[n][j] = Integer.MAX_VALUE;
        }
        
        for (int i = n - 1; i >= 0; i--) {
            // 根据金额模c[i]的余数分组处理
            for (int mod = 0; mod < Math.min(aim + 1, c[i]); mod++) {
                // 使用滑动窗口维护有效范围内的最小值
                LinkedList<Integer> w = new LinkedList<>();
                w.add(mod);
                dp[i][mod] = dp[i + 1][mod];
                
                // 处理等差数列：mod, mod+c[i], mod+2*c[i], ...
                for (int r = mod + c[i]; r <= aim; r += c[i]) {
                    // 移除不再最优的元素
                    while (!w.isEmpty() && (dp[i + 1][w.peekLast()] == Integer.MAX_VALUE
                            || dp[i + 1][w.peekLast()] + num(w.peekLast(), r, c[i]) >= dp[i + 1][r])) {
                        w.pollLast();
                    }
                    w.addLast(r);
                    
                    // 移除超出有效窗口的元素（使用太多硬币）
                    int overdue = r - c[i] * (z[i] + 1);
                    if (w.peekFirst() == overdue) {
                        w.pollFirst();
                    }
                    
                    // 计算当前金额的最小值
                    if (dp[i + 1][w.peekFirst()] == Integer.MAX_VALUE) {
                        dp[i][r] = Integer.MAX_VALUE;
                    } else {
                        dp[i][r] = dp[i + 1][w.peekFirst()] + num(w.peekFirst(), r, c[i]);
                    }
                }
            }
        }
        return dp[0][aim];
    }

    /**
     * 生成随机硬币数组用于测试
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
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
     * 带性能分析的综合测试方法
     */
    public static void main(String[] args) {
        dp2(new int[]{25, 15, 26}, 15);
        dp3(new int[]{25, 15, 26}, 15);
        int times = 100000;
        int maxLen = 20;
        int maxVal = 30;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int aim = (int) ((maxVal + 1) * 2 * Math.random());
            int ans1 = minCoins(arr, aim);
            int ans2 = dp1(arr, aim);
            int ans3 = dp2(arr, aim);
            int ans4 = dp3(arr, aim);
            if (ans1 != ans2 || ans3 != ans4 || ans1 != ans3) {
                System.out.println("Wrong");
                print(arr);
                System.out.println(aim);
                System.out.println(ans1 + "|" + ans2 + "|" + ans3 + "|" + ans4);
                break;
            }
        }
        System.out.println("test end");

        System.out.println("==========");

        int aim = 0;
        int[] arr = null;
        long start;
        long end;
        int ans2;
        int ans3;

        System.out.println("性能测试开始");
        maxLen = 30000;
        maxVal = 20;
        aim = 60000;
        arr = randomArr(maxLen, maxVal);

        start = System.currentTimeMillis();
        ans2 = dp2(arr, aim);
        end = System.currentTimeMillis();
        System.out.println("dp2答案 : " + ans2 + ", dp2运行时间 : " + (end - start) + " ms");

        start = System.currentTimeMillis();
        ans3 = dp3(arr, aim);
        end = System.currentTimeMillis();
        System.out.println("dp3答案 : " + ans3 + ", dp3运行时间 : " + (end - start) + " ms");
        System.out.println("性能测试结束");

        System.out.println("===========");

        System.out.println("货币大量重复出现情况下，");
        System.out.println("大数据量测试dp3开始");
        maxLen = 20000000;
        aim = 10000;
        maxVal = 10000;
        arr = randomArr(maxLen, maxVal);
        start = System.currentTimeMillis();
        ans3 = dp3(arr, aim);
        end = System.currentTimeMillis();
        System.out.println("dp3运行时间 : " + (end - start) + " ms");
        System.out.println("大数据量测试dp3结束");

        System.out.println("===========");

        System.out.println("当货币很少出现重复，dp2比dp3有常数时间优势");
        System.out.println("当货币大量出现重复，dp3时间复杂度明显优于dp2");
        System.out.println("dp3的优化用到了窗口内最小值的更新结构");
    }
}
