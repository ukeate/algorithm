package leetc.top;

import java.util.HashMap;

/**
 * LeetCode 128. 最长连续序列 (Longest Consecutive Sequence)
 * 
 * 问题描述：
 * 给定一个未排序的整数数组 nums，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
 * 请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
 * 
 * 示例：
 * 输入：nums = [100,4,200,1,3,2]
 * 输出：4
 * 解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
 * 
 * 解法思路：
 * 并查集思想 + 哈希表优化：
 * 1. 使用HashMap存储每个数字及其所在连续序列的长度
 * 2. 对于每个新数字，检查其前驱(num-1)和后继(num+1)是否存在
 * 3. 如果存在，则合并连续序列，更新边界节点的长度信息
 * 4. 维护全局最大长度
 * 
 * 核心思想：
 * - 只存储连续序列边界节点的长度信息
 * - map[num-preLen] 存储左边界的序列长度
 * - map[num+posLen] 存储右边界的序列长度
 * - 中间节点不需要维护准确的长度信息
 * 
 * 算法特点：
 * - O(n)时间复杂度：每个数字最多被处理一次
 * - 动态合并：实时合并相邻的连续序列
 * - 边界维护：只维护序列端点的长度信息
 * 
 * 关键优化：
 * - 跳过重复数字
 * - 只更新必要的边界节点
 * - 利用哈希表实现O(1)查找
 * 
 * 时间复杂度：O(n) - 每个数字处理一次
 * 空间复杂度：O(n) - 哈希表存储
 * 
 * LeetCode链接：https://leetcode.com/problems/longest-consecutive-sequence/
 */
public class P128_LongestConsecutiveSequence {
    
    /**
     * 寻找最长连续序列的长度
     * 
     * @param nums 输入数组
     * @return 最长连续序列长度
     */
    public static int longestConsecutive(int[] nums) {
        // key: 数字, value: 该数字所在连续序列的长度
        HashMap<Integer, Integer> map = new HashMap<>();
        int max = 0;  // 最长序列长度
        
        for (int num : nums) {
            // 跳过已经处理过的数字
            if (!map.containsKey(num)) {
                // 将当前数字标记为已处理
                map.put(num, 1);
                
                // 检查前驱和后继是否存在
                int preLen = map.containsKey(num - 1) ? map.get(num - 1) : 0;  // 左边连续序列长度
                int posLen = map.containsKey(num + 1) ? map.get(num + 1) : 0;  // 右边连续序列长度
                
                // 合并后的总长度
                int all = preLen + posLen + 1;
                
                // 更新边界节点的长度信息
                // 左边界：num - preLen
                map.put(num - preLen, all);
                // 右边界：num + posLen  
                map.put(num + posLen, all);
                
                // 更新全局最大长度
                max = Math.max(max, all);
            }
        }
        
        return max;
    }
}
