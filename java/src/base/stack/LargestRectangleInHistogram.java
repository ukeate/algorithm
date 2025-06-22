package base.stack;

import java.util.Stack;

/**
 * 直方图中最大矩形面积问题 - 单调栈的经典应用
 * 
 * 问题描述：
 * 给定一个直方图，每个柱子的宽度为1，高度由数组heights表示
 * 求直方图中能构成的最大矩形面积
 * 
 * 例如：heights = [2,1,5,6,2,3]
 * 最大矩形是高度为5，宽度为2的矩形，面积为10
 * 
 * 解题思路：
 * 使用单调栈（单调递增）来解决
 * 1. 遍历每个柱子，维护一个单调递增的栈
 * 2. 当遇到比栈顶矮的柱子时，说明找到了某些柱子的右边界
 * 3. 依次弹出栈顶元素，计算以这些柱子为高的最大矩形面积
 * 4. 左边界由栈中的前一个元素确定，右边界是当前元素
 * 
 * 核心洞察：
 * 对于每个柱子，我们需要找到它能扩展的最大宽度
 * 即找到左边和右边第一个比它矮的柱子
 * 
 * 时间复杂度：O(n) - 每个元素最多入栈出栈一次
 * 空间复杂度：O(n) - 栈的空间
 * 
 * LeetCode链接：https://leetcode.com/problems/largest-rectangle-in-histogram
 */
public class LargestRectangleInHistogram {
    
    /**
     * 使用Java内置栈求解直方图中最大矩形面积
     * 
     * 算法详解：
     * 1. 维护一个单调递增的栈，存储柱子的索引
     * 2. 遍历每个柱子：
     *    - 如果当前柱子高度 >= 栈顶柱子高度，直接入栈
     *    - 否则，说明栈顶柱子找到了右边界，开始计算面积
     * 3. 对于被弹出的柱子j：
     *    - 高度：heights[j]
     *    - 右边界：当前位置i（不包含）
     *    - 左边界：栈中j的前一个元素k（不包含），如果栈空则为-1
     *    - 宽度：i - k - 1
     *    - 面积：heights[j] * (i - k - 1)
     * 4. 遍历结束后，处理栈中剩余元素（它们的右边界是数组末尾）
     * 
     * 举例说明（heights = [2,1,5,6,2,3]）：
     * 
     * i=0, h=2: 栈空，push(0)，stack=[0]
     * i=1, h=1: 1<2，弹出0，计算面积=(1-(-1)-1)*2=2，push(1)，stack=[1]
     * i=2, h=5: 5>1，push(2)，stack=[1,2]
     * i=3, h=6: 6>5，push(3)，stack=[1,2,3]
     * i=4, h=2: 2<6，弹出3，面积=(4-2-1)*6=6
     *          2<5，弹出2，面积=(4-1-1)*5=10
     *          2>1，push(4)，stack=[1,4]
     * i=5, h=3: 3>2，push(5)，stack=[1,4,5]
     * 
     * 处理剩余元素：
     * 弹出5，面积=(6-4-1)*3=3
     * 弹出4，面积=(6-1-1)*2=8
     * 弹出1，面积=(6-(-1)-1)*1=6
     * 
     * 最大面积：max(2,6,10,3,8,6)=10
     * 
     * @param heights 直方图高度数组
     * @return 最大矩形面积
     */
    public static int largest1(int[] heights) {
        if (heights == null || heights.length == 0) {
            return 0;
        }
        
        int max = 0;
        Stack<Integer> stack = new Stack<>();  // 单调递增栈，存储索引
        
        // 遍历每个柱子
        for (int i = 0; i < heights.length; i++) {
            // 当前柱子比栈顶柱子矮，开始计算面积
            while (!stack.isEmpty() && heights[i] <= heights[stack.peek()]) {
                int j = stack.pop();  // 被弹出的柱子，以它为高计算矩形面积
                
                // 确定左边界：栈中j的前一个元素，如果栈空则为-1
                int k = stack.isEmpty() ? -1 : stack.peek();
                
                // 计算矩形面积：高度 * 宽度
                // 宽度 = 右边界i - 左边界k - 1
                int area = (i - k - 1) * heights[j];
                max = Math.max(max, area);
            }
            stack.push(i);  // 当前柱子入栈
        }
        
        // 处理栈中剩余元素，它们的右边界是数组末尾
        while (!stack.isEmpty()) {
            int j = stack.pop();
            int k = stack.isEmpty() ? -1 : stack.peek();
            int area = (heights.length - k - 1) * heights[j];
            max = Math.max(max, area);
        }
        
        return max;
    }

    /**
     * 使用数组模拟栈求解（性能优化版本）
     * 
     * 与largest1算法完全相同，只是用数组代替Stack类以提高性能
     * 避免了Stack类的额外开销，运行更快
     * 
     * @param heights 直方图高度数组
     * @return 最大矩形面积
     */
    public static int largest2(int[] heights) {
        if (heights == null || heights.length == 0) {
            return 0;
        }
        
        int n = heights.length;
        int[] stack = new int[n];  // 用数组模拟栈
        int si = -1;               // 栈顶指针，-1表示栈空
        int max = 0;
        
        // 遍历每个柱子
        for (int i = 0; i < heights.length; i++) {
            // 当前柱子比栈顶柱子矮，开始计算面积
            while (si != -1 && heights[i] <= heights[stack[si]]) {
                int j = stack[si--];  // 出栈
                
                // 确定左边界
                int k = si == -1 ? -1 : stack[si];
                
                // 计算矩形面积
                int area = (i - k - 1) * heights[j];
                max = Math.max(max, area);
            }
            stack[++si] = i;  // 入栈
        }
        
        // 处理栈中剩余元素
        while (si != -1) {
            int j = stack[si--];
            int k = si == -1 ? -1 : stack[si];
            int area = (heights.length - k - 1) * heights[j];
            max = Math.max(max, area);
        }
        
        return max;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 直方图中最大矩形面积测试 ===");
        
        // 测试用例1
        int[] heights1 = {2, 1, 5, 6, 2, 3};
        System.out.println("测试数组1：" + java.util.Arrays.toString(heights1));
        System.out.println("方法1结果：" + largest1(heights1));
        System.out.println("方法2结果：" + largest2(heights1));
        
        // 测试用例2
        int[] heights2 = {2, 4};
        System.out.println("\n测试数组2：" + java.util.Arrays.toString(heights2));
        System.out.println("方法1结果：" + largest1(heights2));
        System.out.println("方法2结果：" + largest2(heights2));
        
        // 测试用例3：递增序列
        int[] heights3 = {1, 2, 3, 4, 5};
        System.out.println("\n测试数组3：" + java.util.Arrays.toString(heights3));
        System.out.println("方法1结果：" + largest1(heights3));
        System.out.println("方法2结果：" + largest2(heights3));
        
        // 测试用例4：递减序列
        int[] heights4 = {5, 4, 3, 2, 1};
        System.out.println("\n测试数组4：" + java.util.Arrays.toString(heights4));
        System.out.println("方法1结果：" + largest1(heights4));
                 System.out.println("方法2结果：" + largest2(heights4));
         
         System.out.println("\n=== 测试完成 ===");
     }
}