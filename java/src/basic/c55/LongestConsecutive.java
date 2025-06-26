package basic.c55;

import java.util.HashMap;

/**
 * 最长连续序列长度问题
 * 
 * 问题描述：
 * 给定一个未排序的整数数组nums，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
 * 
 * 示例：
 * 输入: nums = [100,4,200,1,3,2]
 * 输出: 4
 * 解释: 最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
 * 
 * 算法思路：
 * 使用哈希表来存储每个数字所在连续序列的长度信息：
 * 1. 只在序列的两端存储长度信息（避免重复更新）
 * 2. 当插入新数字时，检查相邻数字是否存在
 * 3. 如果存在，则合并连续序列并更新端点的长度信息
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * LeetCode: https://leetcode.com/problems/longest-consecutive-sequence/
 * 
 * @author 算法学习
 */
public class LongestConsecutive {
    
    /**
     * 合并两个相邻的连续序列
     * 
     * @param map 存储序列长度信息的哈希表
     * @param preEnd 前一个序列的结束位置
     * @param curStart 当前序列的开始位置
     * @return 合并后序列的长度
     */
    private static int merge(HashMap<Integer, Integer> map, int preEnd, int curStart) {
        // 计算前一个序列的开始位置
        int preStart = preEnd - map.get(preEnd) + 1;
        
        // 计算当前序列的结束位置
        int curEnd = curStart + map.get(curStart) - 1;
        
        // 计算合并后的总长度
        int len = curEnd - preStart + 1;
        
        // 只在新序列的两端存储长度信息
        map.put(preStart, len);  // 新序列的开始位置
        map.put(curEnd, len);    // 新序列的结束位置
        
        return len;
    }

    /**
     * 找到数组中最长连续序列的长度
     * 
     * @param arr 输入数组
     * @return 最长连续序列的长度
     */
    public static int longest(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int max = 1;  // 最长序列长度，至少为1
        
        // key: 数字, value: 该数字所在连续序列的长度
        // 只在序列的端点存储长度信息
        HashMap<Integer, Integer> map = new HashMap<>();
        
        for (int i = 0; i < arr.length; i++) {
            // 跳过重复数字
            if (map.containsKey(arr[i])) {
                continue;
            }
            
            // 初始时每个数字自成一个长度为1的序列
            map.put(arr[i], 1);
            
            // 检查左邻居是否存在
            if (map.containsKey(arr[i] - 1)) {
                // 与左边的序列合并
                max = Math.max(max, merge(map, arr[i] - 1, arr[i]));
            }
            
            // 检查右邻居是否存在
            if (map.containsKey(arr[i] + 1)) {
                // 与右边的序列合并
                max = Math.max(max, merge(map, arr[i], arr[i] + 1));
            }
        }
        
        return max;
    }

    /**
     * 测试方法
     * 验证最长连续序列算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 最长连续序列长度测试 ===");
        
        // 测试用例1
        int[] arr1 = {100, 4, 200, 1, 3, 2};
        int result1 = longest(arr1);
        System.out.println("数组: [100, 4, 200, 1, 3, 2]");
        System.out.println("最长连续序列长度: " + result1);
        System.out.println("连续序列: [1, 2, 3, 4]");
        
        // 测试用例2
        int[] arr2 = {0, 3, 7, 2, 5, 8, 4, 6, 0, 1};
        int result2 = longest(arr2);
        System.out.println("\n数组: [0, 3, 7, 2, 5, 8, 4, 6, 0, 1]");
        System.out.println("最长连续序列长度: " + result2);
        System.out.println("连续序列: [0, 1, 2, 3, 4, 5, 6, 7, 8]");
        
        // 测试用例3：单个元素
        int[] arr3 = {5};
        int result3 = longest(arr3);
        System.out.println("\n数组: [5]");
        System.out.println("最长连续序列长度: " + result3);
        
        // 测试用例4：空数组
        int[] arr4 = {};
        int result4 = longest(arr4);
        System.out.println("\n数组: []");
        System.out.println("最长连续序列长度: " + result4);
        
        // 测试用例5：无连续序列
        int[] arr5 = {1, 3, 5, 7, 9};
        int result5 = longest(arr5);
        System.out.println("\n数组: [1, 3, 5, 7, 9]");
        System.out.println("最长连续序列长度: " + result5);
        
        System.out.println("\n=== 算法分析 ===");
        System.out.println("时间复杂度: O(n) - 每个元素最多被访问常数次");
        System.out.println("空间复杂度: O(n) - 哈希表存储");
        System.out.println("核心思想: 哈希表 + 端点存储 + 序列合并");
        System.out.println("关键优化: 只在序列端点存储长度信息，避免全序列更新");
    }
}
