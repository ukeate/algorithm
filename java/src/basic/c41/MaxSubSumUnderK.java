package basic.c41;

import java.util.TreeSet;

/**
 * 子数组最大累加和不超过K的问题
 * 
 * 问题描述：
 * 给定一个整数数组和一个整数k，
 * 找到数组中累加和不超过k的最大子数组和
 * 
 * 算法思路：
 * 使用前缀和 + TreeSet的方法：
 * 1. 维护到当前位置的前缀和
 * 2. 使用TreeSet存储之前的所有前缀和
 * 3. 对于当前前缀和sum，需要找到最小的preSum使得：
 *    sum - preSum <= k，即：preSum >= sum - k
 * 4. 使用TreeSet的ceiling方法快速查找
 * 
 * 时间复杂度：O(n*logn) - 每次TreeSet操作O(logn)
 * 空间复杂度：O(n) - TreeSet存储前缀和
 * 
 * @author 算法学习
 */
public class MaxSubSumUnderK {
    
    /**
     * 计算数组中不超过k的最大子数组累加和
     * 
     * @param arr 输入数组
     * @param k 上界值
     * @return 不超过k的最大子数组累加和
     * 
     * 算法步骤：
     * 1. 初始化TreeSet，添加0（表示空前缀）
     * 2. 遍历数组，维护前缀和
     * 3. 对每个前缀和，查找满足条件的历史前缀和
     * 4. 更新最大值并将当前前缀和加入TreeSet
     */
    public static int max(int[] arr, int k) {
        // TreeSet用于维护所有历史前缀和，支持快速查找
        TreeSet<Integer> set = new TreeSet<>();
        // 添加0作为虚拟前缀和，表示空前缀的累加和
        set.add(0);
        
        int max = Integer.MIN_VALUE; // 结果的最大值
        int sum = 0; // 当前的前缀和
        
        // 遍历数组中的每个元素
        for (int i = 0; i < arr.length; i++) {
            // 更新前缀和
            sum += arr[i];
            
            // 查找满足条件的历史前缀和
            // 需要找到最小的preSum使得：preSum >= sum - k
            // 这样能保证：sum - preSum <= k
            Integer ceiling = set.ceiling(sum - k);
            if (ceiling != null) {
                // 更新最大值：当前前缀和 - 找到的历史前缀和
                max = Math.max(max, sum - ceiling);
            }
            
            // 将当前前缀和加入TreeSet，供后续查找使用
            set.add(sum);
        }
        
        return max;
    }
}
