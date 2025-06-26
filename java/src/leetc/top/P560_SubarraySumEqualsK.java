package leetc.top;

import java.util.HashMap;

/**
 * LeetCode 560. 和为 K 的子数组 (Subarray Sum Equals K)
 * 
 * 问题描述：
 * 给你一个整数数组 nums 和一个整数 k，请你统计并返回该数组中和为 k 的子数组的个数。
 * 
 * 示例：
 * - 输入：nums = [1,1,1], k = 2
 * - 输出：2
 * - 解释：子数组 [1,1] 和 [1,1] 的和都等于 2
 * 
 * - 输入：nums = [1,2,3], k = 3
 * - 输出：2
 * - 解释：子数组 [1,2] 和 [3] 的和都等于 3
 * 
 * 解法思路：
 * 前缀和 + 哈希表：
 * 1. 使用前缀和将子数组和问题转化为两个前缀和的差
 * 2. sum[i,j] = prefixSum[j] - prefixSum[i-1] = k
 * 3. 即 prefixSum[i-1] = prefixSum[j] - k
 * 4. 用哈希表记录每个前缀和出现的次数
 * 5. 对于当前前缀和，查找是否存在 (当前和 - k) 的前缀和
 * 
 * 核心思想：
 * - 边计算前缀和边查找匹配的前缀和
 * - 利用哈希表的O(1)查找特性
 * - 处理从数组开头开始的子数组（前缀和为0的情况）
 * 
 * 时间复杂度：O(n) - 遍历数组一次，哈希表操作为O(1)
 * 空间复杂度：O(n) - 哈希表最多存储n个不同的前缀和
 * 
 * LeetCode链接：https://leetcode.com/problems/subarray-sum-equals-k/
 */
public class P560_SubarraySumEqualsK {
    
    /**
     * 统计和为k的子数组个数
     * 
     * 算法步骤：
     * 1. 初始化哈希表，放入(0,1)表示空前缀的前缀和为0，出现1次
     * 2. 遍历数组，维护当前前缀和
     * 3. 查找 (当前前缀和 - k) 是否在哈希表中
     * 4. 如果存在，说明找到了和为k的子数组，累加其出现次数
     * 5. 将当前前缀和加入哈希表或更新其计数
     * 
     * 数学原理：
     * - 子数组nums[i...j]的和 = prefixSum[j] - prefixSum[i-1]
     * - 要使子数组和为k，需要 prefixSum[j] - prefixSum[i-1] = k
     * - 即 prefixSum[i-1] = prefixSum[j] - k
     * 
     * @param nums 整数数组
     * @param k 目标和
     * @return 和为k的子数组个数
     */
    public static int subarraySum(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 哈希表：前缀和 -> 出现次数
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, 1); // 初始化：空前缀的前缀和为0，出现1次（处理从开头开始的子数组）
        
        int all = 0;   // 当前前缀和
        int ans = 0;   // 满足条件的子数组个数
        
        for (int i = 0; i < nums.length; i++) {
            // 更新当前前缀和
            all += nums[i];
            
            // 查找是否存在前缀和为 (all - k) 的位置
            // 如果存在，说明从那个位置的下一个元素到当前位置的子数组和为k
            if (map.containsKey(all - k)) {
                ans += map.get(all - k);
            }
            
            // 将当前前缀和加入哈希表或更新其计数
            if (!map.containsKey(all)) {
                map.put(all, 1);
            } else {
                map.put(all, map.get(all) + 1);
            }
        }
        
        return ans;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        int[] nums1 = {1, 1, 1};
        int k1 = 2;
        System.out.println("测试用例1:");
        System.out.println("输入: nums = " + java.util.Arrays.toString(nums1) + ", k = " + k1);
        System.out.println("输出: " + subarraySum(nums1, k1));
        System.out.println("期望: 2");
        System.out.println("子数组: [1,1] (索引0-1), [1,1] (索引1-2)");
        System.out.println();
        
        // 测试用例2：不同子数组长度
        int[] nums2 = {1, 2, 3};
        int k2 = 3;
        System.out.println("测试用例2:");
        System.out.println("输入: nums = " + java.util.Arrays.toString(nums2) + ", k = " + k2);
        System.out.println("输出: " + subarraySum(nums2, k2));
        System.out.println("期望: 2");
        System.out.println("子数组: [1,2] (索引0-1), [3] (索引2)");
        System.out.println();
        
        // 测试用例3：包含负数
        int[] nums3 = {1, -1, 0};
        int k3 = 0;
        System.out.println("测试用例3 (包含负数):");
        System.out.println("输入: nums = " + java.util.Arrays.toString(nums3) + ", k = " + k3);
        System.out.println("输出: " + subarraySum(nums3, k3));
        System.out.println("期望: 3");
        System.out.println("子数组: [1,-1] (索引0-1), [0] (索引2), [1,-1,0] (索引0-2)");
        System.out.println();
        
        // 测试用例4：单元素
        int[] nums4 = {5};
        int k4 = 5;
        System.out.println("测试用例4 (单元素):");
        System.out.println("输入: nums = " + java.util.Arrays.toString(nums4) + ", k = " + k4);
        System.out.println("输出: " + subarraySum(nums4, k4));
        System.out.println("期望: 1");
        System.out.println();
        
        // 测试用例5：无匹配子数组
        int[] nums5 = {1, 2, 3};
        int k5 = 10;
        System.out.println("测试用例5 (无匹配):");
        System.out.println("输入: nums = " + java.util.Arrays.toString(nums5) + ", k = " + k5);
        System.out.println("输出: " + subarraySum(nums5, k5));
        System.out.println("期望: 0");
        System.out.println();
        
        // 算法特点说明
        System.out.println("算法特点:");
        System.out.println("- 核心思想: 前缀和 + 哈希表");
        System.out.println("- 时间复杂度: O(n) - 单次遍历");
        System.out.println("- 空间复杂度: O(n) - 哈希表存储前缀和");
        System.out.println("- 关键技巧: 将子数组和问题转化为前缀和差值问题");
        System.out.println("- 边界处理: 初始化(0,1)处理从数组开头开始的子数组");
    }
}
