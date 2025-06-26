package leetc.top;

/**
 * LeetCode 334. 递增的三元子序列 (Increasing Triplet Subsequence)
 * 
 * 问题描述：
 * 给你一个整数数组 nums ，判断这个数组中是否存在长度为 3 的递增子序列。
 * 如果存在这样的三元组下标 (i, j, k) 且满足 i < j < k 和 nums[i] < nums[j] < nums[k] ，
 * 返回 true ；否则，返回 false 。
 * 
 * 要求：你能实现时间复杂度为 O(n) ，空间复杂度为 O(1) 的解决方案吗？
 * 
 * 示例：
 * 输入：nums = [1,2,3,4,5]
 * 输出：true
 * 解释：任何 i < j < k 的三元组都满足题意
 * 
 * 输入：nums = [5,4,3,2,1]
 * 输出：false
 * 解释：不存在满足题意的三元组
 * 
 * 输入：nums = [2,1,2,0,1]
 * 输出：false
 * 解释：虽然存在递增的子序列，但长度不够3
 * 
 * 解法思路：
 * 贪心算法 + 双指针维护：
 * 
 * 1. 核心思想：
 *    - 维护两个变量：first（最小值）和 second（中间值）
 *    - 遍历数组，动态更新这两个值
 *    - 一旦找到比 second 更大的数，就找到了递增三元组
 * 
 * 2. 贪心策略：
 *    - first：尽可能小的第一个数
 *    - second：在找到 first 之后，尽可能小的第二个数
 *    - 当遇到比 second 大的数时，立即返回 true
 * 
 * 3. 算法步骤：
 *    - 初始化 first = second = Integer.MAX_VALUE
 *    - 对于每个数 num：
 *      * 如果 num <= first，更新 first = num
 *      * 否则如果 num <= second，更新 second = num
 *      * 否则找到了第三个数，返回 true
 *    - 遍历结束仍未找到，返回 false
 * 
 * 4. 关键洞察：
 *    - 不需要记录具体的三元组，只需判断存在性
 *    - first 和 second 的更新不一定来自同一个三元组
 *    - 贪心选择保证了最大的发现机会
 * 
 * 核心思想：
 * - 贪心维护：始终保持 first 和 second 尽可能小
 * - 状态机：三个状态（寻找第一个、第二个、第三个数）
 * - 一次遍历：O(n) 时间复杂度，O(1) 空间复杂度
 * 
 * 关键技巧：
 * - 使用 Integer.MAX_VALUE 作为哨兵值
 * - 严格的大小关系判断（<= 和 >）
 * - 贪心更新策略确保最优性
 * 
 * 时间复杂度：O(n) - 单次遍历数组
 * 空间复杂度：O(1) - 只使用常数个变量
 * 
 * LeetCode链接：https://leetcode.com/problems/increasing-triplet-subsequence/
 */
public class P334_IncreasingTripletSubsequence {
    
    /**
     * 判断是否存在递增的三元子序列
     * 
     * 算法流程：
     * 1. 初始化两个变量维护当前最小的两个递增数字
     * 2. 遍历数组，根据当前数字与已维护数字的关系更新状态
     * 3. 一旦找到第三个递增数字，立即返回 true
     * 4. 遍历结束未找到，返回 false
     * 
     * @param nums 输入数组
     * @return 是否存在长度为3的递增子序列
     */
    public boolean increasingTriplet(int[] nums) {
        if (nums == null || nums.length < 3) {
            return false; // 数组长度不足3，不可能存在三元组
        }
        
        // 贪心维护：first 是目前为止最小的数，second 是大于 first 的最小数
        int first = Integer.MAX_VALUE;   // 递增三元组的第一个数（最小值）
        int second = Integer.MAX_VALUE;  // 递增三元组的第二个数（中间值）
        
        // 遍历数组寻找递增三元组
        for (int num : nums) {
            if (num <= first) {
                // 当前数字比第一个数更小或相等，更新第一个数
                // 这样可以为后续找到更小的起点
                first = num;
            } else if (num <= second) {
                // 当前数字比第一个数大，但比第二个数小或相等
                // 更新第二个数，此时已经有了一个长度为2的递增序列
                second = num;
            } else {
                // 当前数字比第二个数还大
                // 找到了递增三元组：first < second < num
                return true;
            }
        }
        
        // 遍历完成仍未找到递增三元组
        return false;
    }
}
