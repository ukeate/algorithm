package leetc.top;

import java.util.HashMap;

/**
 * LeetCode 1. 两数之和 (Two Sum)
 * 
 * 问题描述：
 * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的两个整数，
 * 并返回它们的数组下标。
 * 
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
 * 你可以按任意顺序返回答案。
 * 
 * 解法思路：
 * 使用哈希表(HashMap)一次遍历法：
 * 1. 遍历数组，对于每个元素nums[i]，计算target - nums[i]
 * 2. 检查哈希表中是否存在这个差值，如果存在，说明找到了两个数
 * 3. 如果不存在，将当前数值和索引存入哈希表
 * 4. 继续遍历直到找到答案
 * 
 * 优势：
 * - 只需要一次遍历，时间复杂度优于暴力双重循环的O(n²)
 * - 空间换时间的经典应用
 * 
 * 时间复杂度：O(n) - 只需要遍历数组一次
 * 空间复杂度：O(n) - 最坏情况下哈希表需要存储n-1个元素
 */
public class P1_TwoSum {
    
    /**
     * 两数之和核心算法
     * 
     * @param nums 输入的整数数组
     * @param target 目标和
     * @return 两个数的索引组成的数组，如果不存在返回{-1, -1}
     */
    public static int[] twoSum(int[] nums, int target) {
        // key存储数值，value存储该数值在数组中的索引
        HashMap<Integer, Integer> map = new HashMap<>();
        
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];  // 计算当前数的补数
            
            // 如果补数在哈希表中存在，说明找到了一对和为target的数
            if (map.containsKey(complement)) {
                return new int[] {map.get(complement), i};
            }
            
            // 将当前数值和索引加入哈希表
            map.put(nums[i], i);
        }
        
        // 如果没有找到满足条件的两个数，返回{-1, -1}
        return new int[] {-1, -1};
    }
}
