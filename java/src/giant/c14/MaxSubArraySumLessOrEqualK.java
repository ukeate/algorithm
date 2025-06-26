package giant.c14;

import java.util.TreeSet;

/**
 * 不超过K的最大子数组和问题
 * 
 * 问题描述：
 * 给定一个整数数组arr和一个整数k，找出所有累加和不超过k的子数组中，累加和最大的值。
 * 
 * 例如：
 * arr = [1, -2, 3, -1, 2], k = 3
 * 所有可能的子数组和：
 * [1] = 1, [-2] = -2, [3] = 3, [-1] = -1, [2] = 2
 * [1,-2] = -1, [-2,3] = 1, [3,-1] = 2, [-1,2] = 1
 * [1,-2,3] = 2, [-2,3,-1] = 0, [3,-1,2] = 4 (>3,不符合)
 * ...
 * 符合条件(≤3)的最大值是3
 * 
 * 解决方案：
 * 使用前缀和 + TreeSet的方法，时间复杂度O(N*logN)
 * 
 * 核心思想：
 * 对于每个位置i，要找到一个j(j<=i)，使得sum[i] - sum[j-1] <= k且尽可能大
 * 即：sum[j-1] >= sum[i] - k，在所有满足条件的sum[j-1]中选择最小的
 * 
 * 算法复杂度：
 * 时间复杂度：O(N*logN)
 * 空间复杂度：O(N)
 */
public class MaxSubArraySumLessOrEqualK {
    
    /**
     * 找出累加和不超过k的子数组中的最大累加和
     * 
     * 算法思路：
     * 1. 使用前缀和技巧，sum[i]表示arr[0...i]的累加和
     * 2. 对于每个位置i，要找到一个位置j(j<=i)，使得sum[i] - sum[j-1] <= k
     * 3. 等价于找到sum[j-1] >= sum[i] - k的最小值
     * 4. 使用TreeSet维护所有之前出现的前缀和，快速查找满足条件的最小值
     * 
     * 关键操作：
     * - TreeSet.ceiling(x)：返回集合中 >= x 的最小元素
     * - 这正好对应我们需要的 sum[j-1] >= sum[i] - k 的最小值
     * 
     * 时间复杂度：O(N*logN)，N个元素，每次TreeSet操作O(logN)
     * 空间复杂度：O(N)，TreeSet存储前缀和
     * 
     * @param arr 输入的整数数组
     * @param k 累加和的上限
     * @return 不超过k的最大子数组累加和
     */
    public static int max(int[] arr, int k) {
        // TreeSet维护所有出现过的前缀和，支持快速查找
        TreeSet<Integer> set = new TreeSet<>();
        set.add(0);  // 添加初始前缀和0（空子数组的前缀和）
        
        int max = Integer.MIN_VALUE;  // 记录满足条件的最大累加和
        int sum = 0;  // 当前前缀和
        
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];  // 更新前缀和
            
            // 查找满足条件的最小前缀和
            // 需要找到 preSum >= sum - k 的最小值
            Integer preSum = set.ceiling(sum - k);
            
            if (preSum != null) {
                // 如果找到了满足条件的前缀和，更新最大值
                // 子数组和 = 当前前缀和 - 之前的前缀和
                max = Math.max(max, sum - preSum);
            }
            
            // 将当前前缀和加入TreeSet，供后续查找使用
            set.add(sum);
        }
        
        return max;
    }
    
    /**
     * 测试方法：验证算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 不超过K的最大子数组和测试 ===");
        
        // 测试用例1
        int[] test1 = {1, -2, 3, -1, 2};
        int k1 = 3;
        System.out.println("数组: [1, -2, 3, -1, 2], k = " + k1);
        System.out.println("结果: " + max(test1, k1));
        System.out.println("期望: 3 (子数组[3])");
        System.out.println();
        
        // 测试用例2
        int[] test2 = {2, 1, -3, 4, -1, 2, 1, -5, 4};
        int k2 = 5;
        System.out.println("数组: [2, 1, -3, 4, -1, 2, 1, -5, 4], k = " + k2);
        System.out.println("结果: " + max(test2, k2));
        System.out.println();
        
        // 测试用例3：所有元素都大于k
        int[] test3 = {5, 6, 7};
        int k3 = 4;
        System.out.println("数组: [5, 6, 7], k = " + k3);
        System.out.println("结果: " + max(test3, k3));
        System.out.println("期望: 较小的负值或Integer.MIN_VALUE");
        System.out.println();
        
        // 测试用例4：包含负数的情况
        int[] test4 = {-1, -2, -3};
        int k4 = -1;
        System.out.println("数组: [-1, -2, -3], k = " + k4);
        System.out.println("结果: " + max(test4, k4));
        System.out.println("期望: -1 (子数组[-1])");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
