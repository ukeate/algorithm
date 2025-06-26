package leetc.top;

/**
 * LeetCode 494. 目标和 (Target Sum)
 * 
 * 问题描述：
 * 给你一个整数数组 nums 和一个整数 target。
 * 向数组中的每个整数前添加 '+' 或 '-'，然后串联起所有整数，可以构造一个表达式：
 * 例如，nums = [2, 1]，可以在 2 之前添加 '+'，在 1 之前添加 '-'，构造表达式 "+2-1" = 1。
 * 返回可以通过上述方法构造的、运算结果等于 target 的不同表达式的数目。
 * 
 * 示例：
 * - 输入：nums = [1,1,1,1,1], target = 3
 * - 输出：5
 * - 解释：一共有 5 种方法让最终目标和为 3
 *   -1 + 1 + 1 + 1 + 1 = 3
 *   +1 - 1 + 1 + 1 + 1 = 3
 *   +1 + 1 - 1 + 1 + 1 = 3
 *   +1 + 1 + 1 - 1 + 1 = 3
 *   +1 + 1 + 1 + 1 - 1 = 3
 * 
 * 解法思路：
 * 转化为子集和问题：
 * 1. 将数组分为两部分：P（加号）和 N（减号）
 * 2. 有 sum(P) - sum(N) = target 和 sum(P) + sum(N) = sum(nums)
 * 3. 解得：sum(P) = (target + sum(nums)) / 2
 * 4. 问题转化为：在nums中选择一些数字，使其和等于sum(P)的方案数
 * 5. 这是经典的"子集和"动态规划问题
 * 
 * 数学推导：
 * - 设选择为正号的数字集合为P，选择为负号的数字集合为N
 * - sum(P) - sum(N) = target ... (1)
 * - sum(P) + sum(N) = sum(nums) ... (2)
 * - 由(1)(2)得：2*sum(P) = target + sum(nums)
 * - 所以：sum(P) = (target + sum(nums)) / 2
 * 
 * 时间复杂度：O(n × sum) - n是数组长度，sum是目标子集和
 * 空间复杂度：O(sum) - 使用一维DP数组进行空间优化
 * 
 * LeetCode链接：https://leetcode.com/problems/target-sum/
 */
public class P494_TargetSum {
    
    /**
     * 暴力递归解法：枚举所有可能的符号组合
     * 
     * @param arr 数组
     * @param idx 当前处理的索引
     * @param rest 剩余需要达到的目标值
     * @return 从idx开始能够达到rest的方法数
     */
    private static int process1(int[] arr, int idx, int rest) {
        // 递归终止条件：处理完所有元素
        if (idx == arr.length) {
            return rest == 0 ? 1 : 0; // 如果剩余目标为0，找到一种方法
        }
        
        // 当前数字可以选择加号或减号
        return process1(arr, idx + 1, rest - arr[idx])  // 选择减号
             + process1(arr, idx + 1, rest + arr[idx]); // 选择加号
    }
    
    /**
     * 暴力递归版本（主要用于理解问题）
     * 
     * @param arr 输入数组
     * @param s 目标和
     * @return 达到目标和的方法数
     */
    public static int findTargetSumWays1(int[] arr, int s) {
        return process1(arr, 0, s);
    }

    /**
     * 子集和DP：计算有多少种方法选择数字使和为s
     * 
     * 经典的"背包问题"变种：
     * dp[i] 表示和为i的子集有多少种选择方法
     * 
     * @param nums 数组
     * @param s 目标和
     * @return 和为s的子集数量
     */
    private static int subset(int[] nums, int s) {
        int[] dp = new int[s + 1];
        dp[0] = 1; // 空集的和为0，有1种方法
        
        // 处理每个数字
        for (int n : nums) {
            // 从大到小遍历，避免重复使用同一个数字
            for (int i = s; i >= n; i--) {
                dp[i] += dp[i - n]; // 选择当前数字n的方法数
            }
        }
        
        return dp[s];
    }

    /**
     * 优化解法：转化为子集和问题
     * 
     * 核心思想：
     * 1. 将问题转化为在数组中选择一个子集，使其和为特定值
     * 2. 利用数学关系：sum(正数集) = (target + sum(全部)) / 2
     * 3. 使用动态规划求解子集和问题
     * 
     * @param arr 输入数组
     * @param s 目标和
     * @return 达到目标和的表达式数量
     */
    public static int findTargetSumWays3(int[] arr, int s) {
        // 计算数组总和
        int sum = 0;
        for (int n : arr) {
            sum += n;
        }
        
        // 边界情况检查
        // 1. 如果目标值的绝对值大于总和，无解
        // 2. 如果 (s + sum) 是奇数，无法整除2，无解
        if (sum < Math.abs(s) || ((s & 1) ^ (sum & 1)) != 0) {
            return 0;
        }
        
        // 计算需要选择为正号的数字的目标和
        int targetSum = (s + sum) >> 1; // 除以2
        
        // 转化为子集和问题
        return subset(arr, targetSum);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        int[] nums1 = {1, 1, 1, 1, 1};
        int target1 = 3;
        System.out.println("测试用例1:");
        System.out.println("输入: nums = " + java.util.Arrays.toString(nums1) + ", target = " + target1);
        System.out.println("暴力递归: " + findTargetSumWays1(nums1, target1));
        System.out.println("优化解法: " + findTargetSumWays3(nums1, target1));
        System.out.println("期望: 5");
        System.out.println();
        
        // 测试用例2：无解情况
        int[] nums2 = {1, 2};
        int target2 = 4;
        System.out.println("测试用例2 (无解):");
        System.out.println("输入: nums = " + java.util.Arrays.toString(nums2) + ", target = " + target2);
        System.out.println("优化解法: " + findTargetSumWays3(nums2, target2));
        System.out.println("期望: 0 (目标值4大于总和3)");
        System.out.println();
        
        // 测试用例3：边界情况
        int[] nums3 = {100};
        int target3 = -200;
        System.out.println("测试用例3 (边界):");
        System.out.println("输入: nums = " + java.util.Arrays.toString(nums3) + ", target = " + target3);
        System.out.println("优化解法: " + findTargetSumWays3(nums3, target3));
        System.out.println("期望: 0 (|-200| > 100)");
        System.out.println();
        
        // 测试用例4：单个元素匹配
        int[] nums4 = {1};
        int target4 = 1;
        System.out.println("测试用例4 (单元素匹配):");
        System.out.println("输入: nums = " + java.util.Arrays.toString(nums4) + ", target = " + target4);
        System.out.println("优化解法: " + findTargetSumWays3(nums4, target4));
        System.out.println("期望: 1 (+1 = 1)");
        System.out.println();
        
        // 测试用例5：包含0的情况
        int[] nums5 = {0, 0, 1};
        int target5 = 1;
        System.out.println("测试用例5 (包含0):");
        System.out.println("输入: nums = " + java.util.Arrays.toString(nums5) + ", target = " + target5);
        System.out.println("优化解法: " + findTargetSumWays3(nums5, target5));
        System.out.println("期望: 4 (0和0可以任选符号，1必须为正)");
        System.out.println();
        
        // 算法对比说明
        System.out.println("算法对比:");
        System.out.println("1. 暴力递归: O(2^n) 时间，O(n) 空间");
        System.out.println("2. 优化解法: O(n×sum) 时间，O(sum) 空间");
        System.out.println("3. 核心优化: 将符号选择问题转化为子集和问题");
        System.out.println("4. 数学技巧: 利用和的性质减少状态空间");
    }
}
