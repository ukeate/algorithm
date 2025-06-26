package leetc.top;

import java.util.Stack;

/**
 * LeetCode 84. 柱状图中最大的矩形 (Largest Rectangle in Histogram)
 * 
 * 问题描述：
 * 给定 n 个非负整数，用来表示柱状图中各个柱子的高度。
 * 每个柱子彼此相邻，且宽度为 1。
 * 求在该柱状图中，能够勾勒出来的矩形的最大面积。
 * 
 * 示例：
 * - 输入：heights = [2,1,5,6,2,3]，输出：10
 * - 解释：最大的矩形为图中红色区域，面积为 10
 * 
 * 解法思路：
 * 单调栈算法：
 * 1. 维护一个单调递增的栈，存储柱子的索引
 * 2. 当遇到较小的柱子时，说明栈顶柱子不能继续向右扩展
 * 3. 弹出栈顶柱子，计算以该柱子为高度的最大矩形面积
 * 4. 左边界：栈中前一个元素位置，右边界：当前位置
 * 5. 处理完所有柱子后，栈中可能还有剩余柱子需要处理
 * 
 * 核心思想：
 * - 对于每个柱子，找到它能扩展的最大宽度
 * - 左边界：第一个比它小的柱子位置
 * - 右边界：第一个比它小的柱子位置
 * - 面积 = 高度 × 宽度
 * 
 * 时间复杂度：O(n) - 每个元素最多入栈和出栈一次
 * 空间复杂度：O(n) - 栈的空间开销
 */
public class P84_LargestRectangleInHistogram {
    
    /**
     * 使用单调栈求柱状图中的最大矩形面积
     * 
     * 算法步骤：
     * 1. 遍历所有柱子，维护单调递增栈
     * 2. 当前柱子高度小于栈顶：
     *    - 弹出栈顶柱子j，计算以heights[j]为高度的矩形面积
     *    - 左边界k：栈中j的前一个元素（如果栈空则为-1）
     *    - 右边界：当前位置i
     *    - 面积 = heights[j] × (i - k - 1)
     * 3. 将当前柱子索引入栈
     * 4. 处理栈中剩余元素（右边界为数组末尾）
     * 
     * @param heights 柱子高度数组
     * @return 最大矩形面积
     */
    public static int largestRectangleArea(int[] heights) {
        if (heights == null || heights.length == 0) {
            return 0;
        }
        
        int maxArea = 0;                    // 最大矩形面积
        Stack<Integer> stack = new Stack<>(); // 单调递增栈，存储柱子索引
        
        // 遍历所有柱子
        for (int i = 0; i < heights.length; i++) {
            // 当前柱子高度小于栈顶柱子，需要结算栈顶柱子
            while (!stack.isEmpty() && heights[i] <= heights[stack.peek()]) {
                int j = stack.pop();        // 弹出栈顶柱子索引
                
                // 确定左边界：栈中j的前一个元素
                int k = stack.isEmpty() ? -1 : stack.peek();
                
                // 计算以heights[j]为高度的矩形面积
                // 宽度 = 右边界(i) - 左边界(k) - 1
                int curArea = (i - k - 1) * heights[j];
                maxArea = Math.max(maxArea, curArea);
            }
            
            // 将当前柱子索引入栈
            stack.push(i);
        }
        
        // 处理栈中剩余的柱子（右边界为数组末尾）
        while (!stack.isEmpty()) {
            int j = stack.pop();            // 弹出栈顶柱子索引
            
            // 确定左边界：栈中j的前一个元素
            int k = stack.isEmpty() ? -1 : stack.peek();
            
            // 计算以heights[j]为高度的矩形面积
            // 宽度 = 数组长度 - 左边界(k) - 1
            int curArea = (heights.length - k - 1) * heights[j];
            maxArea = Math.max(maxArea, curArea);
        }
        
        return maxArea;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：[2,1,5,6,2,3]
        int[] heights1 = {2, 1, 5, 6, 2, 3};
        System.out.println("输入: [2,1,5,6,2,3]");
        System.out.println("最大矩形面积: " + largestRectangleArea(heights1)); // 输出: 10
        
        // 测试用例2：[2,4]
        int[] heights2 = {2, 4};
        System.out.println("输入: [2,4]");
        System.out.println("最大矩形面积: " + largestRectangleArea(heights2)); // 输出: 4
        
        // 测试用例3：[6,7,5,2,4,5,9,3]
        int[] heights3 = {6, 7, 5, 2, 4, 5, 9, 3};
        System.out.println("输入: [6,7,5,2,4,5,9,3]");
        System.out.println("最大矩形面积: " + largestRectangleArea(heights3)); // 输出: 16
    }
}
