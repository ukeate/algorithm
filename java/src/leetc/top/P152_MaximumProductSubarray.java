package leetc.top;

/**
 * LeetCode 152. 乘积最大子数组 (Maximum Product Subarray)
 * 
 * 问题描述：
 * 给你一个整数数组 nums ，请你找出数组中乘积最大的非空连续子数组（该子数组中至少包含一个数字），
 * 并返回该子数组所对应的乘积。
 * 
 * 解法思路：
 * 动态规划 + 最值维护：
 * 1. 与最大和子数组不同，乘积问题需要考虑负数的影响
 * 2. 负数乘以负数会变成正数，可能产生更大的乘积
 * 3. 需要同时维护当前位置的最大乘积和最小乘积
 * 4. 最小乘积在遇到负数时可能变成最大乘积
 * 
 * 状态转移：
 * - curMax = max(nums[i], min*nums[i], max*nums[i])
 * - curMin = min(nums[i], min*nums[i], max*nums[i])
 * - 三个候选值：当前数字、最小值乘当前数字、最大值乘当前数字
 * 
 * 核心思想：
 * - 维护到当前位置的最大和最小乘积
 * - 考虑是否延续之前的子数组或重新开始
 * - 负数的特殊性：最小值可能变成最大值
 * 
 * 关键点：
 * - 必须是连续子数组
 * - 处理0的情况：遇到0时重新开始计算
 * - 处理负数：最小负数乘以负数可能变成最大正数
 * 
 * 时间复杂度：O(n) - 一次遍历数组
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/maximum-product-subarray/
 */
public class P152_MaximumProductSubarray {
    
    /**
     * 找到乘积最大的连续子数组
     * 
     * @param nums 整数数组
     * @return 最大乘积
     */
    public static int maxProduct(int[] nums) {
        int ans = nums[0];   // 全局最大乘积
        int min = nums[0];   // 到当前位置的最小乘积
        int max = nums[0];   // 到当前位置的最大乘积
        
        // 从第二个元素开始遍历
        for (int i = 1; i < nums.length; i++) {
            // 计算包含当前元素的所有可能乘积
            // 1. 只包含当前元素（重新开始）
            // 2. 之前的最小乘积乘以当前元素
            // 3. 之前的最大乘积乘以当前元素
            int curMin = Math.min(nums[i], Math.min(min * nums[i], max * nums[i]));
            int curMax = Math.max(nums[i], Math.max(min * nums[i], max * nums[i]));
            
            // 更新当前位置的最小和最大乘积
            min = curMin;
            max = curMax;
            
            // 更新全局最大乘积
            ans = Math.max(ans, max);
        }
        
        return ans;
    }
}
