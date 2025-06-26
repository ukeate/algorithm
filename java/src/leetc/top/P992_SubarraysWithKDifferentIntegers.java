package leetc.top;

import java.util.HashMap;

/**
 * LeetCode 992. K个不同整数的子数组 (Subarrays with K Different Integers)
 * 
 * 问题描述：
 * 给定一个正整数数组 A，如果 A 的某个子数组中不同整数的个数恰好为 K，
 * 则称 A 的这个连续、不一定不同的子数组为好子数组。
 * 
 * （例如，[1,2,3,1,2] 中有 3 个不同的整数：1，2，以及 3。）
 * 返回 A 中好子数组的数目。
 * 
 * 示例：
 * - 输入：A = [1,2,1,2,3], K = 2
 * - 输出：7
 * - 解释：恰好由 2 个不同整数组成的子数组：[1,2], [2,1], [1,2], [2,3], [1,2,1], [2,1,2], [1,2,1,2]
 * 
 * - 输入：A = [1,2,1,3,4], K = 3
 * - 输出：3
 * - 解释：恰好由 3 个不同整数组成的子数组：[1,2,1,3], [2,1,3], [1,3,4]
 * 
 * 解法思路：
 * 转化思想：恰好K个 = 最多K个 - 最多K-1个
 * 
 * 两种方法：
 * 1. 双指针窗口法：维护两个窗口分别计算最多K个和最多K-1个
 * 2. 单次遍历法：同时维护两个窗口边界
 * 
 * 时间复杂度：O(n) - 每个元素最多被访问两次
 * 空间复杂度：O(k) - 哈希表存储不同整数的计数
 * 
 * LeetCode链接：https://leetcode.com/problems/subarrays-with-k-different-integers/
 */
public class P992_SubarraysWithKDifferentIntegers {
    
    /**
     * 方法1：双指针窗口法，一次遍历同时维护两个窗口
     * 
     * 核心思想：
     * - lessLeft: 最多包含K-1个不同整数的窗口右边界
     * - equalLeft: 最多包含K个不同整数的窗口右边界
     * - 对于每个位置r，以r结尾且恰好包含K个不同整数的子数组数量 = lessLeft - equalLeft
     * 
     * @param nums 输入数组
     * @param k 目标不同整数个数
     * @return 恰好包含k个不同整数的子数组数量
     */
    public static int subarraysWithKDistinct1(int[] nums, int k) {
        int n = nums.length;
        int[] lessCnt = new int[n + 1];    // 最多k-1个不同整数的计数数组
        int[] equalCnt = new int[n + 1];   // 最多k个不同整数的计数数组
        
        int lessLeft = 0, equalLeft = 0;   // 两个窗口的左边界
        int lessKinds = 0, equalKinds = 0; // 两个窗口中不同整数的数量
        int ans = 0;
        
        for (int r = 0; r < n; r++) {
            // 扩展两个窗口的右边界
            if (lessCnt[nums[r]] == 0) {
                lessKinds++;
            }
            if (equalCnt[nums[r]] == 0) {
                equalKinds++;
            }
            lessCnt[nums[r]]++;
            equalCnt[nums[r]]++;
            
            // 收缩第一个窗口：保持最多k-1个不同整数
            while (lessKinds == k) {
                if (lessCnt[nums[lessLeft]] == 1) {
                    lessKinds--;
                }
                lessCnt[nums[lessLeft++]]--;
            }
            
            // 收缩第二个窗口：保持最多k个不同整数
            while (equalKinds > k) {
                if (equalCnt[nums[equalLeft]] == 1) {
                    equalKinds--;
                }
                equalCnt[nums[equalLeft++]]--;
            }
            
            // 以r结尾恰好包含k个不同整数的子数组数量
            ans += lessLeft - equalLeft;
        }
        
        return ans;
    }

    /**
     * 辅助方法：计算最多包含k个不同整数的子数组数量
     * 
     * @param arr 输入数组
     * @param k 最多不同整数个数
     * @return 子数组数量
     */
    private static int numsMostK(int[] arr, int k) {
        int i = 0, res = 0;
        HashMap<Integer, Integer> count = new HashMap<>();
        
        for (int j = 0; j < arr.length; ++j) {
            // 扩展窗口：添加arr[j]
            if (count.getOrDefault(arr[j], 0) == 0) {
                k--; // 新的不同整数
            }
            count.put(arr[j], count.getOrDefault(arr[j], 0) + 1);
            
            // 收缩窗口：保持最多k个不同整数
            while (k < 0) {
                count.put(arr[i], count.get(arr[i]) - 1);
                if (count.get(arr[i]) == 0) {
                    k++; // 移除了一个不同整数
                }
                i++;
            }
            
            // 以j结尾的子数组数量 = j - i + 1
            res += j - i + 1;
        }
        
        return res;
    }

    /**
     * 方法2：转化思想，恰好K个 = 最多K个 - 最多K-1个
     * 
     * @param arr 输入数组
     * @param k 目标不同整数个数
     * @return 恰好包含k个不同整数的子数组数量
     */
    public static int subarraysWithKDistinct2(int[] arr, int k) {
        return numsMostK(arr, k) - numsMostK(arr, k - 1);
    }
}
