package basic.c38;

import java.util.Arrays;
import java.util.HashSet;

/**
 * 最大可整合子数组长度问题
 * 
 * 问题描述：
 * 给定一个数组，找到其中最长的可整合子数组。
 * 可整合数组定义：排序后相邻数字都差1（即构成连续序列）。
 * 
 * 例如：
 * - [5, 5, 3, 2, 6, 4, 3] 中，[5, 3, 2, 6, 4] 排序后为 [2, 3, 4, 5, 6]，
 *   相邻元素差1，是可整合的，长度为5
 * - [1, 3, 5] 排序后为 [1, 3, 5]，相邻元素不都差1，不可整合
 * 
 * 算法思路：
 * 方法1：暴力法 - 枚举所有子数组，排序后检验 O(N³*logN)
 * 方法2：优化法 - 利用数学性质，无重复且max-min=length-1 O(N²)
 * 
 * 核心优化思想：
 * 可整合数组必须满足：
 * 1. 无重复元素
 * 2. 最大值 - 最小值 = 数组长度 - 1
 * 
 * 时间复杂度：O(N³*logN) vs O(N²)
 * 空间复杂度：O(N)
 */
public class LongestIntegratedLength {
    
    /**
     * 检查指定区间的子数组是否可整合
     * 
     * 方法：将子数组排序后，检查相邻元素是否都差1
     * 
     * @param arr 原数组
     * @param l 区间左边界（包含）
     * @param r 区间右边界（包含）
     * @return 是否可整合
     */
    private static boolean can(int[] arr, int l, int r) {
        // 复制子数组并排序
        int[] arr2 = Arrays.copyOfRange(arr, l, r + 1);
        Arrays.sort(arr2);
        
        // 检查排序后相邻元素是否都差1
        for (int i = 1; i < arr2.length; i++) {
            if (arr2[i - 1] != arr2[i] - 1) {
                return false;  // 发现不连续的相邻元素
            }
        }
        
        return true;
    }

    /**
     * 方法1：暴力解法
     * 枚举所有可能的子数组，逐一检验是否可整合
     * 
     * 算法流程：
     * 1. 双重循环枚举所有子数组[i,j]
     * 2. 对每个子数组进行排序和验证
     * 3. 记录最长的可整合子数组长度
     * 
     * 时间复杂度：O(N³*logN)
     * - 外层循环：O(N²)个子数组
     * - 内层排序：O(N*logN)
     * - 验证：O(N)
     * 
     * 空间复杂度：O(N)，用于复制子数组
     * 
     * @param arr 输入数组
     * @return 最大可整合子数组长度
     */
    public static int longest1(int[] arr) {
        // 边界条件检查
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int len = 0;  // 最大可整合子数组长度
        
        // 枚举所有子数组
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++) {
                // 检查子数组[i,j]是否可整合
                if (can(arr, i, j)) {
                    len = Math.max(len, j - i + 1);
                }
            }
        }
        
        return len;
    }

    /**
     * 方法2：优化解法
     * 利用可整合数组的数学性质进行优化
     * 
     * 核心观察：
     * 数组可整合 ⟺ 无重复元素 且 max - min = length - 1
     * 
     * 证明：
     * - 必要性：可整合数组排序后为连续序列，显然无重复且max-min=length-1
     * - 充分性：无重复且max-min=length-1的数组，必然包含[min, max]区间的所有整数
     * 
     * 算法流程：
     * 1. 枚举左边界l
     * 2. 扩展右边界r，维护当前子数组的最大值、最小值、无重复性
     * 3. 检查是否满足 max - min = r - l，若满足则更新最大长度
     * 4. 一旦发现重复元素，结束当前左边界的扩展
     * 
     * 时间复杂度：O(N²)
     * 空间复杂度：O(N)，用于HashSet去重
     * 
     * @param arr 输入数组
     * @return 最大可整合子数组长度
     */
    public static int longest2(int[] arr) {
        // 边界条件检查
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int len = 0;  // 最大可整合子数组长度
        int max = 0;  // 当前子数组最大值
        int min = 0;  // 当前子数组最小值
        HashSet<Integer> set = new HashSet<>();  // 去重集合
        
        // 枚举左边界
        for (int l = 0; l < arr.length; l++) {
            set.clear();  // 清空上一个左边界的记录
            max = Integer.MIN_VALUE;
            min = Integer.MAX_VALUE;
            
            // 扩展右边界
            for (int r = l; r < arr.length; r++) {
                // 检查重复元素
                if (set.contains(arr[r])) {
                    break;  // 发现重复，结束当前左边界的扩展
                }
                
                // 更新当前子数组的状态
                set.add(arr[r]);
                max = Math.max(max, arr[r]);
                min = Math.min(min, arr[r]);
                
                // 检查可整合条件：max - min = length - 1
                if (max - min == r - l) {
                    len = Math.max(len, r - l + 1);
                }
            }
        }
        
        return len;
    }
    
    /**
     * 辅助方法：打印数组
     * @param arr 要打印的数组
     */
    private static void printArray(int[] arr) {
        if (arr == null) {
            System.out.println("null");
            return;
        }
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }
    
    /**
     * 辅助方法：暴力验证（用于测试正确性）
     * @param arr 数组
     * @param start 起始位置
     * @param end 结束位置
     * @return 是否可整合
     */
    private static boolean verifyIntegrable(int[] arr, int start, int end) {
        if (start > end) return false;
        
        HashSet<Integer> set = new HashSet<>();
        for (int i = start; i <= end; i++) {
            if (set.contains(arr[i])) {
                return false;  // 有重复
            }
            set.add(arr[i]);
        }
        
        int[] subArr = Arrays.copyOfRange(arr, start, end + 1);
        Arrays.sort(subArr);
        
        for (int i = 1; i < subArr.length; i++) {
            if (subArr[i] != subArr[i-1] + 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 测试方法：验证算法正确性和性能对比
     */
    public static void main(String[] args) {
        System.out.println("=== 最大可整合子数组长度测试 ===");
        
        // 测试用例1：基本情况
        int[] arr1 = {5, 5, 3, 2, 6, 4, 3};
        
        System.out.println("测试用例1：");
        printArray(arr1);
        int result1_1 = longest1(arr1);
        int result1_2 = longest2(arr1);
        System.out.println("暴力算法结果: " + result1_1);
        System.out.println("优化算法结果: " + result1_2);
        System.out.println("结果一致性: " + (result1_1 == result1_2));
        System.out.println("分析：最长可整合子数组可能是 [5,3,2,6,4] 排序后为 [2,3,4,5,6]");
        System.out.println();
        
        // 测试用例2：无可整合子数组（除单元素外）
        int[] arr2 = {1, 3, 5, 7, 9};
        
        System.out.println("测试用例2：");
        printArray(arr2);
        int result2_1 = longest1(arr2);
        int result2_2 = longest2(arr2);
        System.out.println("暴力算法结果: " + result2_1);
        System.out.println("优化算法结果: " + result2_2);
        System.out.println("结果一致性: " + (result2_1 == result2_2));
        System.out.println("分析：都是奇数，间隔为2，最大可整合长度为1");
        System.out.println();
        
        // 测试用例3：整个数组都可整合
        int[] arr3 = {3, 1, 4, 2};
        
        System.out.println("测试用例3：");
        printArray(arr3);
        int result3_1 = longest1(arr3);
        int result3_2 = longest2(arr3);
        System.out.println("暴力算法结果: " + result3_1);
        System.out.println("优化算法结果: " + result3_2);
        System.out.println("结果一致性: " + (result3_1 == result3_2));
        System.out.println("分析：排序后为 [1,2,3,4]，整个数组可整合");
        System.out.println();
        
        // 测试用例4：包含重复元素
        int[] arr4 = {1, 1, 2, 3};
        
        System.out.println("测试用例4：");
        printArray(arr4);
        int result4_1 = longest1(arr4);
        int result4_2 = longest2(arr4);
        System.out.println("暴力算法结果: " + result4_1);
        System.out.println("优化算法结果: " + result4_2);
        System.out.println("结果一致性: " + (result4_1 == result4_2));
        System.out.println("分析：有重复元素1，最长可整合子数组长度受限");
        System.out.println();
        
        // 测试用例5：单元素
        int[] arr5 = {42};
        
        System.out.println("测试用例5：");
        printArray(arr5);
        int result5_1 = longest1(arr5);
        int result5_2 = longest2(arr5);
        System.out.println("暴力算法结果: " + result5_1);
        System.out.println("优化算法结果: " + result5_2);
        System.out.println("结果一致性: " + (result5_1 == result5_2));
        
        System.out.println("\n=== 测试完成 ===");
    }
}
