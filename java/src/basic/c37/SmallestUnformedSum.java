package basic.c37;

import java.util.Arrays;
import java.util.HashSet;

/**
 * 最小不可组成和问题
 * 
 * 问题描述：
 * 给定一个正整数数组，求最小的正整数，使得该正整数无法通过数组中的元素相加得到。
 * 数组中每个元素最多使用一次。
 * 
 * 例如：
 * 数组[1, 2, 4] -> 最小不可组成和为8
 * 可以组成：1, 2, 3(1+2), 4, 5(1+4), 6(2+4), 7(1+2+4)
 * 无法组成：8
 * 
 * 算法思路：
 * 方法1：暴力枚举 - 生成所有可能的子集和，然后找最小缺失值 O(2^N)
 * 方法2：动态规划 - 计算所有可能的子集和，查找最小缺失值 O(N*Sum)
 * 方法3：优化算法 - 基于有序数组的贪心策略 O(N*logN)
 * 
 * 核心洞察：
 * 如果数组包含1且有序，可以用贪心策略：
 * 维护当前可以组成的范围[1, range]，对于新元素num：
 * - 如果num <= range+1，可以扩展到[1, range+num]
 * - 如果num > range+1，则range+1就是答案
 * 
 * 时间复杂度：O(2^N) / O(N*Sum) / O(N*logN)
 * 空间复杂度：O(2^N) / O(N*Sum) / O(1)
 */
public class SmallestUnformedSum {
    
    /**
     * 递归生成所有可能的子集和
     * 
     * @param arr 数组
     * @param i 当前处理的位置
     * @param sum 当前累加和
     * @param set 存储所有可能和的集合
     */
    private static void process(int[] arr, int i, int sum, HashSet<Integer> set) {
        if (i == arr.length) {
            set.add(sum);  // 到达叶子节点，记录当前和
            return;
        }
        
        // 两种选择：不选当前元素 或 选择当前元素
        process(arr, i + 1, sum, set);                // 不选择arr[i]
        process(arr, i + 1, sum + arr[i], set);       // 选择arr[i]
    }

    /**
     * 方法1：暴力枚举解法
     * 
     * 算法思路：
     * 1. 递归生成所有可能的子集和
     * 2. 从数组最小值开始，找到第一个不在集合中的正整数
     * 
     * 时间复杂度：O(2^N)，生成所有子集
     * 空间复杂度：O(2^N)，存储所有可能的和
     * 
     * @param arr 正整数数组
     * @return 最小不可组成和
     */
    public static int min1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 1;  // 空数组，最小不可组成和为1
        }
        
        // 生成所有可能的子集和
        HashSet<Integer> set = new HashSet<>();
        process(arr, 0, 0, set);
        
        // 找到数组中的最小值，作为搜索起点
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            min = Math.min(min, arr[i]);
        }
        
        // 从最小值开始搜索第一个不可组成的正整数
        for (int i = min; i < Integer.MAX_VALUE; i++) {
            if (!set.contains(i)) {
                return i;
            }
        }
        
        return 0;  // 理论上不会到达这里
    }

    /**
     * 方法2：动态规划解法
     * 
     * 算法思路：
     * 1. 计算数组总和sum，构建DP表
     * 2. dp[i][j]表示前i个元素能否组成和j
     * 3. 状态转移：dp[i][j] = dp[i-1][j] || dp[i-1][j-arr[i]]
     * 4. 从最小值开始查找第一个false的位置
     * 
     * 时间复杂度：O(N*Sum)
     * 空间复杂度：O(N*Sum)
     * 
     * @param arr 正整数数组
     * @return 最小不可组成和
     */
    public static int min2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 1;
        }
        
        // 计算总和和最小值
        int sum = 0;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            min = Math.min(min, arr[i]);
        }
        
        int n = arr.length;
        
        // DP表：dp[i][j]表示前i个元素能否组成和j
        boolean[][] dp = new boolean[n][sum + 1];
        
        // 初始化：和为0总是可以组成（选择空集）
        for (int i = 0; i < n; i++) {
            dp[i][0] = true;
        }
        
        // 初始化：只用第一个元素能组成的和
        dp[0][arr[0]] = true;
        
        // 填充DP表
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= sum; j++) {
                // 两种选择：不选arr[i] 或 选arr[i]
                dp[i][j] = dp[i - 1][j] || 
                          ((j - arr[i] >= 0) ? dp[i - 1][j - arr[i]] : false);
            }
        }
        
        // 从最小值开始查找第一个不可组成的和
        for (int j = min; j <= sum; j++) {
            if (!dp[n - 1][j]) {
                return j;
            }
        }
        
        // 如果1到sum都可以组成，那么答案是sum+1
        return sum + 1;
    }

    /**
     * 方法3：优化算法（前提：数组中包含1）
     * 
     * 算法思路：
     * 基于有序数组的贪心策略：
     * 1. 对数组排序
     * 2. 维护当前可组成的范围[1, range]
     * 3. 对于每个元素arr[i]：
     *    - 如果arr[i] <= range+1，可以扩展范围到[1, range+arr[i]]
     *    - 如果arr[i] > range+1，则range+1就是最小不可组成和
     * 
     * 核心洞察：
     * 如果能组成[1, k]，加入元素x（x <= k+1）后能组成[1, k+x]
     * 
     * 时间复杂度：O(N*logN)，主要是排序
     * 空间复杂度：O(1)
     * 
     * @param arr 包含1的正整数数组
     * @return 最小不可组成和
     */
    public static int min3(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;  // 注意：这里返回0与前面方法不同，可能是特殊约定
        }
        
        // 排序数组
        Arrays.sort(arr);
        
        int range = 1;  // 当前可以组成的范围上限[1, range]
        
        // 从第二个元素开始处理（假设第一个元素是1）
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > range + 1) {
                // 出现跳跃，range+1就是最小不可组成和
                return range + 1;
            } else {
                // 扩展可组成范围
                range += arr[i];
            }
        }
        
        // 所有元素都处理完，最小不可组成和是range+1
        return range + 1;
    }

    /**
     * 生成随机数组用于测试
     * 
     * @param len 数组长度
     * @param maxVal 最大值
     * @return 随机数组
     */
    private static int[] randomArr(int len, int maxVal) {
        int[] res = new int[len];
        for (int i = 0; i < res.length; i++) {
            res[i] = (int) (maxVal * Math.random()) + 1;
        }
        return res;
    }

    /**
     * 打印数组
     * 
     * @param arr 要打印的数组
     */
    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    /**
     * 测试方法：验证三种算法的一致性和性能
     */
    public static void main(String[] args) {
        System.out.println("=== 最小不可组成和问题测试 ===");
        
        int len = 27;       // 数组长度
        int maxVal = 30;    // 最大值
        int[] arr = randomArr(len, maxVal);
        
        System.out.println("测试数组：");
        print(arr);
        
        // 测试方法1：暴力枚举
        long start = System.currentTimeMillis();
        int result1 = min1(arr);
        long end = System.currentTimeMillis();
        System.out.println("方法1（暴力枚举）结果: " + result1);
        System.out.println("耗时: " + (end - start) + " ms");
        System.out.println("=====");
        
        // 测试方法2：动态规划
        start = System.currentTimeMillis();
        int result2 = min2(arr);
        end = System.currentTimeMillis();
        System.out.println("方法2（动态规划）结果: " + result2);
        System.out.println("耗时: " + (end - start) + " ms");
        System.out.println("=====");
        
        // 测试方法3：优化算法（需要包含1）
        arr[0] = 1;  // 确保数组包含1
        start = System.currentTimeMillis();
        int result3 = min3(arr);
        end = System.currentTimeMillis();
        System.out.println("方法3（优化算法，arr[0]=1）结果: " + result3);
        System.out.println("耗时: " + (end - start) + " ms");
        
        System.out.println("\n算法结果对比：");
        System.out.println("方法1 vs 方法2: " + (result1 == result2 ? "一致" : "不一致"));
        System.out.println("注意：方法3有特殊前提条件（包含1），结果可能不同");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
