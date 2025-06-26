package basic.c55;

import java.util.Stack;

/**
 * 矩阵中最大矩形面积问题
 * 
 * 问题描述：
 * 给定一个仅包含 0 和 1 、大小为 rows x cols 的二维二进制矩阵，
 * 找出只包含 1 的最大矩形，并返回其面积。
 * 
 * 示例：
 * 输入: matrix = [["1","0","1","0","0"],
 *                ["1","0","1","1","1"],
 *                ["1","1","1","1","1"],
 *                ["1","0","0","1","0"]]
 * 输出: 6
 * 解释: 最大矩形如上图所示，面积为 6
 * 
 * 算法思路：
 * 1. 将问题转化为多个"柱状图中最大矩形"问题
 * 2. 对每一行，计算以该行为底的各列的高度
 * 3. 使用单调栈求解每一行对应的柱状图中的最大矩形
 * 4. 取所有行中的最大值
 * 
 * 时间复杂度：O(rows * cols)
 * 空间复杂度：O(cols)
 * 
 * LeetCode: https://leetcode.com/problems/maximal-rectangle/
 * 
 * @author 算法学习
 */
public class MaxRectangle {
    
    /**
     * 使用单调栈求解柱状图中的最大矩形面积
     * 
     * @param height 柱状图的高度数组
     * @return 最大矩形面积
     */
    private static int maxFromBottom(int[] height) {
        if (height == null || height.length == 0) {
            return 0;
        }
        
        int max = 0;
        Stack<Integer> stack = new Stack<>();  // 存储下标的单调递增栈
        
        // 遍历每个柱子
        for (int i = 0; i < height.length; i++) {
            // 当前柱子比栈顶柱子矮，需要计算以栈顶柱子为高的矩形面积
            while (!stack.isEmpty() && height[i] <= height[stack.peek()]) {
                int j = stack.pop();  // 弹出的柱子作为矩形的高
                
                // 计算矩形的宽度
                int k = stack.isEmpty() ? -1 : stack.peek();  // 左边界
                int cur = (i - k - 1) * height[j];  // 宽度 * 高度
                
                max = Math.max(max, cur);
            }
            stack.push(i);
        }
        
        // 处理栈中剩余的柱子
        while (!stack.isEmpty()) {
            int j = stack.pop();
            int k = stack.isEmpty() ? -1 : stack.peek();
            int cur = (height.length - k - 1) * height[j];
            max = Math.max(max, cur);
        }
        
        return max;
    }

    /**
     * 计算矩阵中最大矩形的面积
     * 
     * @param map 二进制矩阵（0和1组成）
     * @return 最大矩形面积（1的数量）
     */
    public static int max(int[][] map) {
        if (map == null || map.length == 0 || map[0].length == 0) {
            return 0;
        }
        
        int max = 0;
        int[] height = new int[map[0].length];  // 记录每列的连续1的高度
        
        // 逐行处理
        for (int i = 0; i < map.length; i++) {
            // 更新每列的高度
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 0) {
                    height[j] = 0;  // 遇到0，高度重置
                } else {
                    height[j] = height[j] + 1;  // 遇到1，高度累加
                }
            }
            
            // 计算以当前行为底的柱状图中的最大矩形
            max = Math.max(maxFromBottom(height), max);
        }
        
        return max;
    }

    /**
     * 测试方法
     * 验证最大矩形面积算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 矩阵中最大矩形面积测试 ===");
        
        // 测试用例1
        int[][] map1 = {
            {1, 0, 1, 1}, 
            {1, 1, 1, 1}, 
            {1, 1, 1, 0}
        };
        
        int result1 = max(map1);
        System.out.println("矩阵1:");
        printMatrix(map1);
        System.out.println("最大矩形面积: " + result1);
        
        // 测试用例2
        int[][] map2 = {
            {1, 0, 1, 0, 0},
            {1, 0, 1, 1, 1},
            {1, 1, 1, 1, 1},
            {1, 0, 0, 1, 0}
        };
        
        int result2 = max(map2);
        System.out.println("\n矩阵2:");
        printMatrix(map2);
        System.out.println("最大矩形面积: " + result2);
        
        // 测试用例3：全为1的矩阵
        int[][] map3 = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };
        
        int result3 = max(map3);
        System.out.println("\n矩阵3:");
        printMatrix(map3);
        System.out.println("最大矩形面积: " + result3);
        
        // 测试用例4：全为0的矩阵
        int[][] map4 = {
            {0, 0, 0},
            {0, 0, 0}
        };
        
        int result4 = max(map4);
        System.out.println("\n矩阵4:");
        printMatrix(map4);
        System.out.println("最大矩形面积: " + result4);
        
        System.out.println("\n=== 算法分析 ===");
        System.out.println("时间复杂度: O(rows * cols) - 每个元素访问常数次");
        System.out.println("空间复杂度: O(cols) - 高度数组和单调栈");
        System.out.println("核心思想: 问题转化 + 单调栈");
        System.out.println("关键技巧: 将2D问题转化为多个1D柱状图问题");
    }
    
    /**
     * 打印矩阵的辅助方法
     * 
     * @param matrix 要打印的矩阵
     */
    private static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
