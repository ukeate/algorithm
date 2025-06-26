package basic.c53;

import java.util.HashMap;

/**
 * 最高广告牌问题
 * 
 * 问题描述：
 * 你正在安装一个广告牌，并希望它尽可能的高。这个广告牌将有两个钢铁支架，
 * 两个支架必须等高。你有一堆可以焊接的钢筋，每根钢筋有一定的长度。
 * 你最多可以将钢筋焊接在一起来制作两个支架。
 * 
 * 给定数组rods，其中rods[i]是第i根钢筋的长度。
 * 返回广告牌的最大可能安装高度。如果无法支撑广告牌，请返回0。
 * 
 * 算法思路：
 * 使用动态规划，状态定义为：
 * dp[diff] = smaller 表示当两个支架高度差为diff时，较小支架的最大高度
 * 
 * 对于每根钢筋，有三种选择：
 * 1. 不使用
 * 2. 添加到较高的支架
 * 3. 添加到较低的支架
 * 
 * 时间复杂度：O(n * sum)
 * 空间复杂度：O(sum)
 * 
 * LeetCode: https://leetcode.com/problems/tallest-billboard/
 * 
 * @author 算法学习
 */
public class TallestBillboard {
    
    /**
     * 计算最高广告牌的高度
     * 
     * @param arr 钢筋长度数组
     * @return 最高广告牌的高度
     */
    public int max(int[] arr) {
        // dp存储状态：<高度差, 较小支架的高度>
        HashMap<Integer, Integer> dp = new HashMap<>(), cur;
        
        // 初始状态：两个支架都为0，差值为0，较小值为0
        dp.put(0, 0);
        
        // 遍历每根钢筋
        for (int num : arr) {
            // 跳过长度为0的钢筋
            if (num == 0) {
                continue;
            }
            
            // 复制当前状态，避免在遍历时修改
            cur = new HashMap<>(dp);
            
            // 对于每个现有状态
            for (int diff : cur.keySet()) {
                int less = cur.get(diff);  // 较小支架的当前高度
                
                // 选择1：将当前钢筋添加到较高的支架
                // 新的高度差 = 原差值 + 钢筋长度
                // 较小支架高度不变
                int newDiff1 = diff + num;
                dp.put(newDiff1, Math.max(less, dp.getOrDefault(newDiff1, 0)));
                
                // 选择2：将当前钢筋添加到较低的支架
                int oldLess = dp.getOrDefault(Math.abs(num - diff), 0);
                
                if (diff >= num) {
                    // 如果原来的差值 >= 钢筋长度
                    // 新差值 = 原差值 - 钢筋长度
                    // 较小支架高度 = 原较小高度 + 钢筋长度
                    dp.put(diff - num, Math.max(less + num, oldLess));
                } else {
                    // 如果原来的差值 < 钢筋长度
                    // 添加钢筋后，原来较小的支架变成较高的
                    // 新差值 = 钢筋长度 - 原差值
                    // 新的较小支架高度 = 原较小高度 + 原差值
                    dp.put(num - diff, Math.max(less + diff, oldLess));
                }
            }
        }
        
        // 返回两个支架高度相等时的高度（差值为0时的较小值）
        return dp.get(0);
    }
    
    /**
     * 测试方法
     * 验证最高广告牌算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 最高广告牌问题测试 ===");
        
        TallestBillboard solution = new TallestBillboard();
        
        // 测试用例1
        int[] rods1 = {1, 2, 3, 6};
        int result1 = solution.max(rods1);
        System.out.println("钢筋: [1, 2, 3, 6]");
        System.out.println("最大高度: " + result1);
        System.out.println("解释: 选择钢筋1+2+3=6和钢筋6，两个支架都是高度6");
        
        // 测试用例2
        int[] rods2 = {1, 2, 3, 4, 5, 6};
        int result2 = solution.max(rods2);
        System.out.println("\n钢筋: [1, 2, 3, 4, 5, 6]");
        System.out.println("最大高度: " + result2);
        System.out.println("解释: 选择1+2+3+4=10和5+5，但没有两个5，所以选择其他组合");
        
        // 测试用例3
        int[] rods3 = {1, 2};
        int result3 = solution.max(rods3);
        System.out.println("\n钢筋: [1, 2]");
        System.out.println("最大高度: " + result3);
        System.out.println("解释: 无法构造两个等高的支架");
        
        // 测试用例4
        int[] rods4 = {2, 2, 1, 1};
        int result4 = solution.max(rods4);
        System.out.println("\n钢筋: [2, 2, 1, 1]");
        System.out.println("最大高度: " + result4);
        System.out.println("解释: 两边各放2+1=3，最大高度为3");
        
        // 测试用例5：空数组
        int[] rods5 = {};
        int result5 = solution.max(rods5);
        System.out.println("\n钢筋: []");
        System.out.println("最大高度: " + result5);
        System.out.println("解释: 无钢筋，高度为0");
        
        System.out.println("\n=== 算法分析 ===");
        System.out.println("时间复杂度: O(n * sum) - n为钢筋数量，sum为钢筋总长度");
        System.out.println("空间复杂度: O(sum) - 存储所有可能的高度差状态");
        System.out.println("核心思想: 动态规划 + 状态压缩");
        System.out.println("关键技巧: 用高度差作为状态维度，记录较小支架的最大高度");
    }
}
