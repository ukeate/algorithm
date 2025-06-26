package giant.c1;

import java.util.HashMap;

/**
 * 目标和问题（添加加减号达到目标值）
 * LeetCode 494: https://leetcode.com/problems/target-sum/
 * 
 * 问题描述：
 * 给定一个非负整数数组和一个目标数S，为数组中的每个整数前添加'+'或'-'符号，
 * 使得它们的计算结果等于目标数S。返回添加符号的不同方法数。
 * 
 * 例如：
 * 数组: [1, 1, 1, 1, 1], 目标值: 3
 * 可能的表达式: -1+1+1+1+1 = 3, +1-1+1+1+1 = 3, +1+1-1+1+1 = 3, +1+1+1-1+1 = 3, +1+1+1+1-1 = 3
 * 共5种方法
 * 
 * 解法对比：
 * 1. 方法1：暴力递归 - 时间O(2^n)，空间O(n)
 * 2. 方法2：记忆化搜索 - 时间O(n*sum)，空间O(n*sum)
 * 3. 方法3：动态规划转化 - 时间O(n*sum)，空间O(sum)
 * 
 * 核心洞察：方法3将问题转化为"子集和"问题，这是最优解法
 * 
 * @author algorithm learning
 */
public class TargetSum {
    
    /**
     * 方法1：暴力递归解法
     * 
     * 算法思路：
     * 对每个数字，尝试添加'+'或'-'两种符号，递归计算所有可能的结果
     * 
     * 时间复杂度：O(2^n)，每个数字有两种选择
     * 空间复杂度：O(n)，递归栈深度
     * 
     * @param arr 数组
     * @param idx 当前处理的位置
     * @param rest 剩余需要达到的目标值
     * @return 从当前位置开始能达到目标值的方法数
     */
    private static int process1(int[] arr, int idx, int rest) {
        // 基础情况：已处理完所有数字
        if (idx == arr.length) {
            return rest == 0 ? 1 : 0; // 如果剩余目标为0则找到一种方法
        }
        
        // 当前数字添加'-'符号（从目标值中减去）
        int negative = process1(arr, idx + 1, rest - arr[idx]);
        // 当前数字添加'+'符号（目标值加上当前数字）
        int positive = process1(arr, idx + 1, rest + arr[idx]);
        
        return negative + positive;
    }

    /**
     * 方法1：暴力递归的接口方法
     * @param arr 输入数组
     * @param s 目标值
     * @return 达到目标值的方法数
     */
    public static int ways1(int[] arr, int s) {
        return process1(arr, 0, s);
    }

    /**
     * 方法2：记忆化搜索递归解法
     * 
     * 算法思路：
     * 在暴力递归的基础上添加备忘录，避免重复计算相同的子问题
     * 使用嵌套HashMap作为缓存：外层key是位置idx，内层key是剩余目标值rest
     * 
     * 时间复杂度：O(n * sum)，其中sum是数组元素总和的两倍（因为rest可能为负）
     * 空间复杂度：O(n * sum)，备忘录大小
     * 
     * @param arr 数组
     * @param idx 当前处理的位置
     * @param rest 剩余需要达到的目标值
     * @param dp 备忘录，dp[idx][rest]存储子问题的答案
     * @return 从当前位置开始能达到目标值的方法数
     */
    private static int process2(int[] arr, int idx, int rest, HashMap<Integer, HashMap<Integer, Integer>> dp) {
        // 检查备忘录中是否已有答案
        if (dp.containsKey(idx) && dp.get(idx).containsKey(rest)) {
            return dp.get(idx).get(rest);
        }
        
        int ans = 0;
        if (idx == arr.length) {
            ans = rest == 0 ? 1 : 0;
        } else {
            // 递归计算，逻辑与方法1相同
            ans = process2(arr, idx + 1, rest - arr[idx], dp) 
                + process2(arr, idx + 1, rest + arr[idx], dp);
        }
        
        // 将答案存入备忘录
        if (!dp.containsKey(idx)) {
            dp.put(idx, new HashMap<>());
        }
        dp.get(idx).put(rest, ans);
        return ans;
    }

    /**
     * 方法2：记忆化搜索的接口方法
     * @param arr 输入数组
     * @param s 目标值
     * @return 达到目标值的方法数
     */
    public static int ways2(int[] arr, int s) {
        return process2(arr, 0, s, new HashMap<>());
    }

    /**
     * 辅助方法：计算子集和等于指定值的方案数（二维DP版本）
     * 
     * 算法思路：
     * dp[i][j]表示使用前i个数字，能够组成和为j的方案数
     * 状态转移：dp[i][j] = dp[i-1][j] + dp[i-1][j-nums[i-1]]
     * - dp[i-1][j]：不选第i个数字
     * - dp[i-1][j-nums[i-1]]：选择第i个数字（如果j >= nums[i-1]）
     * 
     * @param nums 数组
     * @param s 目标和
     * @return 子集和等于s的方案数
     */
    private static int subset1(int[] nums, int s) {
        if (s < 0) {
            return 0; // 负数目标不可能达到（数组元素都非负）
        }
        
        int n = nums.length;
        int[][] dp = new int[n + 1][s + 1];
        
        // 初始化：空集的和为0，有1种方案
        dp[0][0] = 1;
        
        // 填充DP表
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= s; j++) {
                // 不选第i个数字
                dp[i][j] = dp[i - 1][j];
                
                // 选择第i个数字（如果可能）
                if (j - nums[i - 1] >= 0) {
                    dp[i][j] += dp[i - 1][j - nums[i - 1]];
                }
            }
        }
        
        return dp[n][s];
    }

    /**
     * 辅助方法：计算子集和等于指定值的方案数（一维DP优化版本）
     * 
     * 空间优化思路：
     * 由于dp[i][j]只依赖于dp[i-1][*]，可以使用滚动数组优化空间
     * 注意：内层循环必须从右往左，避免覆盖还未使用的状态
     * 
     * @param nums 数组
     * @param s 目标和
     * @return 子集和等于s的方案数
     */
    private static int subset2(int[] nums, int s) {
        if (s < 0) {
            return 0;
        }
        
        int[] dp = new int[s + 1];
        dp[0] = 1; // 空集的和为0，有1种方案
        
        // 对每个数字进行处理
        for (int n : nums) {
            // 从右往左更新，避免同一轮中的重复使用
            for (int i = s; i >= n; i--) {
                dp[i] += dp[i - n];
            }
        }
        
        return dp[s];
    }

    /**
     * 方法3：最优化版本 - 将原问题转化为子集和问题
     * 
     * 数学转化思路：
     * 设数组总和为sum，添加'+'号的数字集合为P，添加'-'号的数字集合为N
     * 则有：
     * 1. P ∪ N = 数组所有元素，P ∩ N = ∅
     * 2. sum(P) + sum(N) = sum（数组总和）
     * 3. sum(P) - sum(N) = target（目标值）
     * 
     * 解得：sum(P) = (target + sum) / 2
     * 
     * 因此原问题转化为：从数组中选择一些数字，使其和等于(target + sum) / 2的方案数
     * 
     * 优化策略：
     * 1. 可行性检查：target不能超过sum
     * 2. 奇偶性检查：(target + sum)必须是偶数
     * 3. 使用一维DP进行空间优化
     * 
     * 时间复杂度：O(n * sum)
     * 空间复杂度：O(sum)
     * 
     * @param arr 输入数组
     * @param target 目标值
     * @return 达到目标值的方案数
     */
    public static int ways3(int[] arr, int target) {
        // 计算数组元素总和
        int sum = 0;
        for (int n : arr) {
            sum += n;
        }
        
        // 可行性和奇偶性检查：
        // 1. target不能超过sum（所有数都加正号的情况）
        // 2. target和sum必须同奇偶性（因为(target + sum)必须能被2整除）
        return sum < target || ((target & 1) ^ (sum & 1)) != 0 
            ? 0 
            : subset2(arr, (target + sum) >> 1);
    }
    
    /**
     * 测试方法：验证三种方法的正确性和性能
     */
    public static void main(String[] args) {
        System.out.println("=== 目标和问题测试 ===");
        
        // 测试用例1
        int[] arr1 = {1, 1, 1, 1, 1};
        int target1 = 3;
        
        System.out.println("测试用例1: " + java.util.Arrays.toString(arr1) + ", 目标值: " + target1);
        System.out.println("方法1(暴力递归): " + ways1(arr1, target1));
        System.out.println("方法2(记忆化搜索): " + ways2(arr1, target1));
        System.out.println("方法3(DP优化): " + ways3(arr1, target1));
        System.out.println();
        
        // 测试用例2
        int[] arr2 = {1, 0, 1};
        int target2 = 0;
        
        System.out.println("测试用例2: " + java.util.Arrays.toString(arr2) + ", 目标值: " + target2);
        System.out.println("方法1(暴力递归): " + ways1(arr2, target2));
        System.out.println("方法2(记忆化搜索): " + ways2(arr2, target2));
        System.out.println("方法3(DP优化): " + ways3(arr2, target2));
        System.out.println();
        
        System.out.println("=== 测试完成 ===");
        System.out.println("推荐使用方法3，时间复杂度最优且空间使用最少");
    }
}
