package leetc.top;

/**
 * LeetCode 53. 最大子数组和 (Maximum Subarray)
 * 
 * 问题描述：
 * 给你一个整数数组 nums，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），
 * 返回其最大和。
 * 
 * 示例：
 * - 输入：nums = [-2,1,-3,4,-1,2,1,-5,4]，输出：6
 * - 解释：连续子数组 [4,-1,2,1] 的和最大，为 6
 * 
 * 解法思路：
 * Kadane算法（动态规划的优化实现）：
 * 1. 维护两个变量：cur表示以当前位置结尾的最大子数组和，max表示全局最大值
 * 2. 对于每个位置，有两种选择：
 *    - 将当前元素加入前面的子数组：cur + nums[i]
 *    - 从当前元素重新开始：nums[i]
 * 3. 如果前面累计的和为负数，不如重新开始（贪心思想）
 * 4. 实时更新全局最大值
 * 
 * 核心思想：
 * - 当累计和变为负数时，立即舍弃重新开始
 * - 这样保证了每个子数组的前缀和都是非负的
 * 
 * 状态转移：
 * dp[i] = max(dp[i-1] + nums[i], nums[i])
 * 简化为：cur = max(cur + nums[i], nums[i])
 * 
 * 时间复杂度：O(n) - 只需要遍历一次数组
 * 空间复杂度：O(1) - 只使用常数额外空间
 */
public class P53_MaximumSubarray {
    
    /**
     * 使用Kadane算法求最大子数组和
     * 
     * 算法步骤：
     * 1. 初始化cur=0（当前子数组和），max=最小值（全局最大值）
     * 2. 遍历数组，累加当前元素到cur
     * 3. 更新全局最大值max
     * 4. 如果cur变为负数，重置为0（舍弃负前缀）
     * 
     * @param nums 输入的整数数组
     * @return 最大连续子数组的和
     */
    public static int maxSubArray(int[] nums) {
        int cur = 0;                    // 当前子数组的累计和
        int max = Integer.MIN_VALUE;    // 全局最大子数组和
        
        for (int i = 0; i < nums.length; i++) {
            cur += nums[i];             // 将当前元素加入子数组
            max = Math.max(max, cur);   // 更新全局最大值
            cur = cur < 0 ? 0 : cur;    // 如果累计和为负，重新开始
        }
        
        return max;
    }
}
