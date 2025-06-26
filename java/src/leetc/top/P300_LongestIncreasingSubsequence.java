package leetc.top;

/**
 * LeetCode 300. 最长递增子序列 (Longest Increasing Subsequence)
 * 
 * 问题描述：
 * 给你一个整数数组 nums，找到其中最长严格递增子序列的长度。
 * 
 * 子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
 * 例如，[3,6,2,7] 是数组 [0,3,1,6,2,2,7] 的子序列。
 * 
 * 示例：
 * 输入：nums = [10,22,9,33,21,50,41,60]
 * 输出：5
 * 解释：最长递增子序列是 [10,22,33,50,60]，因此长度为 5。
 * 
 * 解法思路：
 * 贪心 + 二分查找优化的动态规划：
 * 1. 维护一个ends数组，ends[i]表示长度为i+1的递增子序列的最小尾部元素
 * 2. 对于每个新元素，使用二分查找在ends数组中找到合适的位置
 * 3. 如果新元素大于所有已有元素，扩展ends数组
 * 4. 否则，替换第一个大于等于新元素的位置，保持最小性
 * 
 * 核心思想：
 * - 贪心策略：对于相同长度的递增子序列，尾部元素越小越好
 * - 二分优化：在有序的ends数组中快速定位插入位置
 * - 状态压缩：ends数组长度即为LIS长度，无需额外存储
 * 
 * 关键洞察：
 * - ends数组本身是递增的
 * - 新元素要么扩展LIS，要么优化某个长度的LIS尾部
 * 
 * 时间复杂度：O(n log n) - n次二分查找
 * 空间复杂度：O(n) - ends数组空间
 * 
 * LeetCode链接：https://leetcode.com/problems/longest-increasing-subsequence/
 */
public class P300_LongestIncreasingSubsequence {
    
    /**
     * 计算最长递增子序列的长度
     * 
     * 算法步骤：
     * 1. 初始化ends数组，ends[0] = arr[0]
     * 2. 对每个新元素arr[i]：
     *    - 使用二分查找找到ends中第一个≥arr[i]的位置
     *    - 如果找到，更新该位置为arr[i]（优化该长度的LIS尾部）
     *    - 如果未找到，说明arr[i]比所有元素都大，扩展ends数组
     * 3. 维护当前LIS的最大长度
     * 
     * @param arr 输入数组
     * @return 最长递增子序列的长度
     */
    public int lengthOfLIS(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int[] ends = new int[arr.length]; // ends[i]: 长度为i+1的LIS的最小尾部元素
        ends[0] = arr[0];                 // 初始化：长度为1的LIS尾部是arr[0]
        int endsR = 0;                    // ends数组的有效右边界
        int max = 1;                      // 当前最长LIS长度
        
        // 处理数组中的每个元素
        for (int i = 1; i < arr.length; i++) {
            int l = 0, r = endsR;
            
            // 二分查找：在ends中找到第一个 >= arr[i] 的位置
            // 不变式：ends[l] >= arr[i] 或 l = r + 1
            while (l <= r) {
                int m = (l + r) / 2;
                if (arr[i] > ends[m]) {
                    l = m + 1;  // arr[i]更大，在右半部分查找
                } else {
                    r = m - 1;  // ends[m] >= arr[i]，在左半部分查找更小的位置
                }
            }
            
            // 更新ends数组和相关状态
            endsR = Math.max(endsR, l);     // 可能扩展了ends的有效范围
            ends[l] = arr[i];               // 更新位置l的最小尾部元素
            max = Math.max(max, l + 1);     // 更新最大LIS长度
        }
        
        return max;
    }
}
