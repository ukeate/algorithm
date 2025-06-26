package basic.c34;

/**
 * 子数组最大累加和问题（经典Kadane算法）
 * 
 * 问题描述：
 * 给定一个整数数组，找到其中和最大的连续子数组，返回最大和。
 * 连续子数组至少包含一个元素。
 * 
 * 例如：
 * 数组[-2, -3, -5, 40, -10, -10, 100, 1]
 * 最大子数组为[40, -10, -10, 100, 1]，和为121
 * 
 * 算法思路（Kadane算法）：
 * 核心思想是动态规划的贪心策略：
 * - 维护一个当前累加和cur
 * - 如果cur为负数，说明之前的子数组对后续没有正向贡献，重新开始
 * - 如果cur为正数，继续累加当前元素
 * - 每一步都更新全局最大值
 * 
 * 关键洞察：
 * 负数的累加和不会帮助我们得到更大的和，所以一旦累加和变为负数就重新开始
 * 
 * 时间复杂度：O(N)，只需要一次遍历
 * 空间复杂度：O(1)，只使用常数额外空间
 */
public class SubMaxSum {
    
    /**
     * 计算数组中子数组的最大累加和
     * 
     * 算法步骤：
     * 1. 初始化max为最小值（处理全负数情况）
     * 2. 初始化cur为0（当前累加和）
     * 3. 遍历数组每个元素：
     *    a. 将当前元素加到cur上
     *    b. 更新max为当前最大值
     *    c. 如果cur变为负数，重置为0（重新开始）
     * 
     * @param arr 输入数组
     * @return 最大子数组的累加和
     */
    public static int max(int[] arr) {
        // 边界条件检查
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int max = Integer.MIN_VALUE;  // 全局最大和（必须用最小值初始化，处理全负数）
        int cur = 0;                  // 当前累加和
        
        // 遍历数组应用Kadane算法
        for (int i = 0; i < arr.length; i++) {
            cur += arr[i];                    // 累加当前元素
            max = Math.max(max, cur);         // 更新全局最大值
            cur = cur < 0 ? 0 : cur;          // 如果累加和为负，重新开始
        }
        
        return max;
    }
    
    /**
     * 扩展版本：同时返回最大和及其对应的子数组位置
     * 
     * @param arr 输入数组
     * @return 数组[最大和, 起始位置, 结束位置]
     */
    public static int[] maxWithPosition(int[] arr) {
        if (arr == null || arr.length == 0) {
            return new int[]{0, -1, -1};
        }
        
        int max = Integer.MIN_VALUE;
        int cur = 0;
        int start = 0, end = 0;      // 最大子数组的起始和结束位置
        int tempStart = 0;           // 当前子数组的临时起始位置
        
        for (int i = 0; i < arr.length; i++) {
            cur += arr[i];
            
            // 更新最大值和位置
            if (cur > max) {
                max = cur;
                start = tempStart;
                end = i;
            }
            
            // 如果累加和为负，重新开始
            if (cur < 0) {
                cur = 0;
                tempStart = i + 1;  // 下一个子数组从下一位置开始
            }
        }
        
        return new int[]{max, start, end};
    }

    /**
     * 测试方法：验证算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 子数组最大累加和测试 ===");
        
        // 测试用例1：包含正负数的数组
        int[] arr1 = {-2, -3, -5, 40, -10, -10, 100, 1};
        
        System.out.println("测试用例1：");
        printArray(arr1);
        int result1 = max(arr1);
        int[] detail1 = maxWithPosition(arr1);
        System.out.println("最大子数组和: " + result1);
        System.out.println("位置信息: [" + detail1[1] + ", " + detail1[2] + "]");
        printSubArray(arr1, detail1[1], detail1[2]);
        System.out.println();
        
        // 测试用例2：全为负数的数组
        int[] arr2 = {-5, -2, -8, -1, -4};
        
        System.out.println("测试用例2（全负数）：");
        printArray(arr2);
        int result2 = max(arr2);
        int[] detail2 = maxWithPosition(arr2);
        System.out.println("最大子数组和: " + result2);
        System.out.println("位置信息: [" + detail2[1] + ", " + detail2[2] + "]");
        printSubArray(arr2, detail2[1], detail2[2]);
        System.out.println();
        
        // 测试用例3：全为正数的数组
        int[] arr3 = {1, 2, 3, 4, 5};
        
        System.out.println("测试用例3（全正数）：");
        printArray(arr3);
        int result3 = max(arr3);
        System.out.println("最大子数组和: " + result3);
        System.out.println();
        
        // 测试用例4：单元素数组
        int[] arr4 = {42};
        
        System.out.println("测试用例4（单元素）：");
        printArray(arr4);
        int result4 = max(arr4);
        System.out.println("最大子数组和: " + result4);
        
        System.out.println("\n=== 测试完成 ===");
    }
    
    /**
     * 辅助方法：打印数组
     * @param arr 要打印的数组
     */
    private static void printArray(int[] arr) {
        System.out.print("数组: [");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }
    
    /**
     * 辅助方法：打印子数组
     * @param arr 原数组
     * @param start 起始位置
     * @param end 结束位置
     */
    private static void printSubArray(int[] arr, int start, int end) {
        if (start < 0 || end < 0) {
            System.out.println("最大子数组: 无效位置");
            return;
        }
        
        System.out.print("最大子数组: [");
        for (int i = start; i <= end; i++) {
            System.out.print(arr[i]);
            if (i < end) System.out.print(", ");
        }
        System.out.println("]");
    }
}
