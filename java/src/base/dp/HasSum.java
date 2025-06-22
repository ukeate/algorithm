package base.dp;

import java.util.HashMap;
import java.util.HashSet;

/**
 * 子集和问题（Subset Sum Problem）
 * 
 * 问题描述：
 * 给定一个整数数组arr，判断是否可以从中选择一些数字（可以重复选择），
 * 使得它们的和等于给定的目标值sum。
 * 
 * 这是经典的动态规划问题，也是0-1背包问题的变种。
 * 
 * 解法分析：
 * 1. 暴力递归：对每个数字有选择和不选择两种决策
 * 2. 记忆化搜索：使用HashMap缓存中间结果
 * 3. 动态规划：考虑负数情况，使用偏移量处理负数索引
 * 4. 折半搜索：将数组分成两部分，分别枚举所有可能的和
 * 
 * 时间复杂度：O(n * sum) 到 O(2^(n/2))
 * 空间复杂度：O(n * sum) 到 O(2^(n/2))
 */
// 自由选择arr的值，能不能累加得到sum
public class HasSum {
    
    /**
     * 暴力递归解法
     * 
     * @param arr 数组
     * @param i 当前考虑的元素索引
     * @param sum 目标和
     * @return 是否能够达到目标和
     */
    private static boolean process1(int[] arr, int i, int sum) {
        // base case：目标和为0，找到一种方案
        if (sum == 0) {
            return true;
        }
        
        // base case：数组遍历完了但和不为0
        if (i == -1) {
            return false;
        }
        
        // 两种选择：不选当前数字 或 选择当前数字
        return process1(arr, i - 1, sum) || process1(arr, i - 1, sum - arr[i]);
    }

    /**
     * 暴力递归解法入口
     * 
     * @param arr 数组
     * @param sum 目标和
     * @return 是否能够达到目标和
     */
    public static boolean hasSum1(int[] arr, int sum) {
        if (sum == 0) {
            return true;
        }
        if (arr == null || arr.length == 0) {
            return false;
        }
        return process1(arr, arr.length - 1, sum);
    }

    /**
     * 记忆化搜索解法
     * 
     * @param arr 数组
     * @param i 当前考虑的元素索引
     * @param sum 目标和
     * @param dp 记忆化缓存，dp[i][sum]表示从索引i开始能否达到和sum
     * @return 是否能够达到目标和
     */
    private static boolean process2(int[] arr, int i, int sum, HashMap<Integer, HashMap<Integer, Boolean>> dp) {
        // 检查缓存
        if (dp.containsKey(i) && dp.get(i).containsKey(sum)) {
            return dp.get(i).get(sum);
        }
        
        boolean ans = false;
        
        if (sum == 0) {
            ans = true;
        } else if (i != -1) {
            ans = process2(arr, i - 1, sum, dp) || process2(arr, i - 1, sum - arr[i], dp);
        }

        // 缓存结果
        if (!dp.containsKey(i)) {
            dp.put(i, new HashMap<>());
        }
        dp.get(i).put(sum, ans);
        return ans;
    }

    /**
     * 记忆化搜索解法入口
     * 
     * @param arr 数组
     * @param sum 目标和
     * @return 是否能够达到目标和
     */
    public static boolean hasSum2(int[] arr, int sum) {
        if (sum == 0) {
            return true;
        }
        if (arr == null || arr.length == 0) {
            return false;
        }
        return process2(arr, arr.length - 1, sum, new HashMap<>());
    }

    /**
     * 动态规划解法（处理负数情况）
     * 
     * 关键思路：
     * 由于sum可能为负数，不能直接作为数组索引，需要计算偏移量。
     * 偏移量 = -min，这样所有可能的和都可以映射到非负索引。
     * 
     * @param arr 数组
     * @param sum 目标和
     * @return 是否能够达到目标和
     */
    public static boolean hasSum3(int[] arr, int sum) {
        if (sum == 0) {
            return true;
        }
        if (arr == null || arr.length == 0) {
            return false;
        }
        
        // 计算所有可能的和的范围[min, max]
        int min = 0;
        int max = 0;
        for (int num : arr) {
            min += num < 0 ? num : 0;  // 所有负数的和
            max += num > 0 ? num : 0;  // 所有正数的和
        }
        
        // 如果目标和超出可能范围，直接返回false
        if (sum < min || sum > max) {
            return false;
        }
        
        int n = arr.length;
        // dp[i][j]表示使用前i个数字能否达到和j-min（j-min是偏移后的索引）
        boolean[][] dp = new boolean[n][max - min + 1];
        
        // 初始化第一行
        dp[0][-min] = true;                    // 不选第一个数字，和为0
        dp[0][arr[0] - min] = true;           // 选择第一个数字，和为arr[0]
        
        // 填充DP表
        for (int i = 1; i < n; i++) {
            for (int j = min; j <= max; j++) {
                // 不选择当前数字
                dp[i][j - min] = dp[i - 1][j - min];
                
                // 选择当前数字（如果索引合法）
                int next = j - min - arr[i];
                dp[i][j - min] |= (next >= 0 && next <= max - min && dp[i - 1][next]);
            }
        }
        
        return dp[n - 1][sum - min];
    }

    /**
     * 枚举所有可能的子集和
     * 
     * @param arr 数组
     * @param i 起始索引
     * @param end 结束索引
     * @param pre 当前累积的和
     * @param ans 存储所有可能和的集合
     */
    private static void process4(int[] arr, int i, int end, int pre, HashSet<Integer> ans) {
        if (i == end) {
            ans.add(pre);
        } else {
            // 不选择当前数字
            process4(arr, i + 1, end, pre, ans);
            // 选择当前数字
            process4(arr, i + 1, end, pre + arr[i], ans);
        }
    }

    /**
     * 折半搜索解法（Meet in the Middle）
     * 
     * 关键思路：
     * 将数组分成两部分，分别枚举所有可能的子集和，
     * 然后检查两部分的和能否组合成目标值。
     * 时间复杂度从O(2^n)优化到O(2^(n/2))。
     * 
     * @param arr 数组
     * @param sum 目标和
     * @return 是否能够达到目标和
     */
    public static boolean hasSum4(int[] arr, int sum) {
        if (sum == 0) {
            return true;
        }
        if (arr == null || arr.length == 0) {
            return false;
        }
        if (arr.length == 1) {
            return arr[0] == sum;
        }
        
        int n = arr.length;
        int mid = n >> 1;  // 中点位置
        
        // 分别枚举左半部分和右半部分的所有可能和
        HashSet<Integer> leftSum = new HashSet<>();
        HashSet<Integer> rightSum = new HashSet<>();
        process4(arr, 0, mid, 0, leftSum);
        process4(arr, mid, n, 0, rightSum);
        
        // 检查左半部分的和l与右半部分的和(sum-l)是否都存在
        for (int l : leftSum) {
            if (rightSum.contains(sum - l)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 生成随机数组用于测试
     * 
     * @param maxLen 最大长度
     * @param maxVal 最大绝对值
     * @return 随机数组
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            // 生成[-maxVal, maxVal]范围内的随机数
            arr[i] = (int) (((maxVal << 1) + 1) * Math.random()) - maxVal;
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 20;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int sum = (int) (((maxVal << 1) + 1) * Math.random()) - maxVal;
            boolean ans1 = hasSum1(arr, sum);
            boolean ans2 = hasSum2(arr, sum);
            boolean ans3 = hasSum3(arr, sum);
            boolean ans4 = hasSum4(arr, sum);
            if (ans1 ^ ans2 || ans3 ^ ans4 || ans1 ^ ans3) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
