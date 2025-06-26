package basic.c52;

/**
 * 三个不重叠子数组的最大和问题
 * 
 * 问题描述：
 * 给定数组nums和整数k，找到三个长度为k的不重叠子数组，
 * 使得它们的和最大。返回这三个子数组的起始索引。
 * 
 * 约束条件：
 * - 三个子数组不能重叠
 * - 每个子数组长度为k
 * - 如果有多个答案，返回字典序最小的
 * 
 * 算法思路：
 * 1. 预处理：计算每个位置开始的长度为k的子数组和
 * 2. 左侧预处理：对于每个位置，记录其左侧最大子数组和的起始位置
 * 3. 右侧预处理：对于每个位置，记录其右侧最大子数组和的起始位置
 * 4. 枚举中间子数组：结合左右预处理结果找到最优解
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * LeetCode: https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/
 * 
 * @author 算法学习
 */
public class MaxSumOf3NonOverlappingSub {
    
    /**
     * 找到三个不重叠子数组的最大和及其起始位置
     * 
     * @param arr 输入数组
     * @param k 子数组长度
     * @return 三个子数组的起始索引数组
     */
    public static int[] max(int[] arr, int k) {
        int n = arr.length;
        
        // 预处理：计算每个位置开始的长度为k的子数组和
        int[] range = new int[n];
        int sum = 0;
        
        // 计算第一个子数组的和
        for (int i = 0; i < k; i++) {
            sum += arr[i];
        }
        range[0] = sum;
        
        // 左侧预处理：记录每个位置左侧的最大子数组起始位置
        int[] left = new int[n];
        left[k - 1] = 0;  // 第一个完整子数组的位置
        int max = sum;
        
        // 滑动窗口计算后续子数组和，并更新左侧最大值
        for (int i = k; i < n; i++) {
            sum = sum - arr[i - k] + arr[i];  // 滑动窗口更新
            range[i - k + 1] = sum;
            
            left[i] = left[i - 1];  // 继承前一位置的最大值位置
            if (sum > max) {
                max = sum;
                left[i] = i - k + 1;  // 更新最大值位置
            }
        }

        // 右侧预处理：记录每个位置右侧的最大子数组起始位置
        sum = 0;
        // 计算最后一个子数组的和
        for (int i = n - 1; i >= n - k; i--) {
            sum += arr[i];
        }
        max = sum;
        
        int[] right = new int[n];
        right[n - k] = n - k;  // 最后一个完整子数组的位置
        
        // 从右向左计算，更新右侧最大值
        for (int i = n - k - 1; i >= 0; i--) {
            sum = sum - arr[i + k] + arr[i];  // 滑动窗口更新
            right[i] = right[i + 1];  // 继承后一位置的最大值位置
            
            // 注意：这里用>=是为了保证字典序最小（选择更左边的位置）
            if (sum >= max) {
                max = sum;
                right[i] = i;
            }
        }

        // 枚举中间子数组位置，找到三个子数组和的最大值
        int a = 0, b = 0, c = 0;  // 三个子数组的起始位置
        max = 0;
        
        // 中间子数组的有效范围：[k, n-2k]
        for (int i = k; i < n - 2 * k + 1; i++) {
            int part1 = range[left[i - 1]];      // 左侧最大子数组和
            int part2 = range[i];                // 当前中间子数组和
            int part3 = range[right[i + k]];     // 右侧最大子数组和
            
            if (part1 + part2 + part3 > max) {
                max = part1 + part2 + part3;
                a = left[i - 1];    // 左侧最大子数组起始位置
                b = i;              // 中间子数组起始位置
                c = right[i + k];   // 右侧最大子数组起始位置
            }
        }
        
        return new int[]{a, b, c};
    }
    
    /**
     * 测试方法
     * 验证三个不重叠子数组最大和算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 三个不重叠子数组最大和测试 ===");
        
        // 测试用例1
        int[] arr1 = {1, 2, 1, 2, 6, 7, 5, 1};
        int k1 = 2;
        int[] result1 = max(arr1, k1);
        System.out.println("数组: [1, 2, 1, 2, 6, 7, 5, 1], k = 2");
        System.out.printf("结果: [%d, %d, %d]%n", result1[0], result1[1], result1[2]);
        System.out.println("子数组: [1,2], [6,7], [5,1], 和 = 3 + 13 + 6 = 22");
        
        // 测试用例2
        int[] arr2 = {1, 2, 1, 2, 1, 2, 1, 2, 1};
        int k2 = 2;
        int[] result2 = max(arr2, k2);
        System.out.println("\n数组: [1, 2, 1, 2, 1, 2, 1, 2, 1], k = 2");
        System.out.printf("结果: [%d, %d, %d]%n", result2[0], result2[1], result2[2]);
        
        // 测试用例3
        int[] arr3 = {4, 3, 2, 1};
        int k3 = 1;
        int[] result3 = max(arr3, k3);
        System.out.println("\n数组: [4, 3, 2, 1], k = 1");
        System.out.printf("结果: [%d, %d, %d]%n", result3[0], result3[1], result3[2]);
        System.out.println("子数组: [4], [3], [2], 和 = 4 + 3 + 2 = 9");
        
        System.out.println("\n=== 算法分析 ===");
        System.out.println("时间复杂度: O(n) - 三次线性扫描");
        System.out.println("空间复杂度: O(n) - 存储预处理结果");
        System.out.println("核心思想: 预处理 + 滑动窗口 + 枚举中间位置");
    }
}
