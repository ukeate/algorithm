package leetc.top;

import java.util.Stack;

/**
 * LeetCode 85. 最大矩形 (Maximal Rectangle)
 * 
 * 问题描述：
 * 给定一个仅包含 0 和 1 的二维二进制矩阵，找出只包含 1 的最大矩形，并返回其面积。
 * 
 * 示例：
 * matrix = [["1","0","1","0","0"],
 *           ["1","0","1","1","1"],
 *           ["1","1","1","1","1"],
 *           ["1","0","0","1","0"]]
 * 输出：6
 * 解释：最大矩形如下图所示，面积为 6
 * 
 * 解法思路：
 * 转化为"柱状图中最大的矩形"问题（LeetCode 84）：
 * 1. 将二维矩阵的每一行看作柱状图的底部
 * 2. 对于每一行，计算以该行为底的柱状图中每个位置的高度
 * 3. 高度计算规则：
 *    - 如果当前位置为'1'，高度为上一行高度+1
 *    - 如果当前位置为'0'，高度重置为0
 * 4. 对每一行构成的柱状图，使用单调栈求最大矩形面积
 * 5. 所有行的最大面积的最大值即为答案
 * 
 * 核心算法：单调栈
 * - 维护一个递增的单调栈，存储柱子索引
 * - 当遇到较矮的柱子时，弹出栈中较高的柱子并计算面积
 * - 计算面积时，高度为被弹出柱子的高度，宽度为当前位置到栈顶下一个位置的距离
 * 
 * 时间复杂度：O(m×n) - m和n分别是矩阵的行数和列数，每个位置最多入栈出栈一次
 * 空间复杂度：O(n) - 高度数组和单调栈的空间开销
 * 
 * LeetCode链接：https://leetcode.com/problems/maximal-rectangle/
 */
public class P85_MaximalRectangle {
    
    /**
     * 计算柱状图中的最大矩形面积（单调栈解法）
     * 
     * 算法原理：
     * 1. 使用单调递增栈维护柱子索引
     * 2. 遇到较矮柱子时，弹出较高柱子并计算以其为高的最大矩形
     * 3. 矩形宽度 = 当前位置 - 栈顶下一个位置 - 1
     * 4. 处理完所有柱子后，清空栈中剩余元素
     * 
     * @param height 柱状图的高度数组
     * @return 最大矩形面积
     */
    private static int maxFromBottom(int[] height) {
        if (height == null || height.length == 0) {
            return 0;
        }
        
        int maxArea = 0;
        Stack<Integer> stack = new Stack<>();  // 单调递增栈，存储柱子索引
        
        // 遍历所有柱子
        for (int i = 0; i < height.length; i++) {
            // 当前柱子比栈顶柱子矮，需要弹出栈顶并计算面积
            while (!stack.isEmpty() && height[i] <= height[stack.peek()]) {
                int j = stack.pop();  // 被弹出柱子的索引（作为矩形的高）
                
                // 计算矩形宽度：左边界是栈顶下一个位置，右边界是当前位置i
                int k = stack.isEmpty() ? -1 : stack.peek();  // 左边界
                int curArea = (i - k - 1) * height[j];        // 面积 = 宽 × 高
                maxArea = Math.max(maxArea, curArea);
            }
            stack.push(i);  // 当前柱子入栈
        }
        
        // 处理栈中剩余的柱子（右边界为数组末尾）
        while (!stack.isEmpty()) {
            int j = stack.pop();  // 被弹出柱子的索引
            int k = stack.isEmpty() ? -1 : stack.peek();  // 左边界
            int curArea = (height.length - k - 1) * height[j];  // 宽度到数组末尾
            maxArea = Math.max(maxArea, curArea);
        }
        
        return maxArea;
    }

    /**
     * 计算二进制矩阵中的最大矩形面积
     * 
     * 算法流程：
     * 1. 初始化高度数组height，长度为矩阵列数
     * 2. 逐行处理矩阵：
     *    - 如果当前位置为'0'，高度重置为0
     *    - 如果当前位置为'1'，高度累加1
     * 3. 对每一行形成的柱状图，计算最大矩形面积
     * 4. 返回所有行的最大面积
     * 
     * @param map 二进制字符矩阵，'0'表示空，'1'表示有效位置
     * @return 最大矩形面积
     */
    public static int maximalRectangle(char[][] map) {
        if (map == null || map.length == 0 || map[0].length == 0) {
            return 0;
        }
        
        int maxArea = 0;
        int[] height = new int[map[0].length];  // 高度数组，记录每列的连续1的高度
        
        // 逐行处理，将每一行看作柱状图的底部
        for (int i = 0; i < map.length; i++) {
            // 更新高度数组
            for (int j = 0; j < map[0].length; j++) {
                // 如果当前位置为'0'，高度重置为0；否则高度加1
                height[j] = map[i][j] == '0' ? 0 : height[j] + 1;
            }
            
            // 计算当前行为底的柱状图中的最大矩形面积
            maxArea = Math.max(maxFromBottom(height), maxArea);
        }
        
        return maxArea;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        char[][] matrix1 = {
            {'1','0','1','0','0'},
            {'1','0','1','1','1'},
            {'1','1','1','1','1'},
            {'1','0','0','1','0'}
        };
        System.out.println("测试矩阵1:");
        printMatrix(matrix1);
        System.out.println("最大矩形面积: " + maximalRectangle(matrix1));
        System.out.println("期望结果: 6");
        System.out.println();
        
        // 测试用例2：全0矩阵
        char[][] matrix2 = {
            {'0','0','0'},
            {'0','0','0'},
            {'0','0','0'}
        };
        System.out.println("测试矩阵2 (全0):");
        printMatrix(matrix2);
        System.out.println("最大矩形面积: " + maximalRectangle(matrix2));
        System.out.println("期望结果: 0");
        System.out.println();
        
        // 测试用例3：全1矩阵
        char[][] matrix3 = {
            {'1','1','1'},
            {'1','1','1'},
            {'1','1','1'}
        };
        System.out.println("测试矩阵3 (全1):");
        printMatrix(matrix3);
        System.out.println("最大矩形面积: " + maximalRectangle(matrix3));
        System.out.println("期望结果: 9");
        System.out.println();
        
        // 测试用例4：单行矩阵
        char[][] matrix4 = {{'1','1','0','1','1'}};
        System.out.println("测试矩阵4 (单行):");
        printMatrix(matrix4);
        System.out.println("最大矩形面积: " + maximalRectangle(matrix4));
        System.out.println("期望结果: 2");
        System.out.println();
        
        // 测试用例5：单列矩阵
        char[][] matrix5 = {{'1'},{'1'},{'0'},{'1'}};
        System.out.println("测试矩阵5 (单列):");
        printMatrix(matrix5);
        System.out.println("最大矩形面积: " + maximalRectangle(matrix5));
        System.out.println("期望结果: 2");
        System.out.println();
        
        // 性能测试
        char[][] largeMatrix = generateLargeMatrix(100, 100);
        long start = System.currentTimeMillis();
        int result = maximalRectangle(largeMatrix);
        long end = System.currentTimeMillis();
        System.out.println("100x100矩阵性能测试:");
        System.out.println("最大矩形面积: " + result);
        System.out.println("执行时间: " + (end - start) + "ms");
    }
    
    /**
     * 打印字符矩阵
     */
    private static void printMatrix(char[][] matrix) {
        for (char[] row : matrix) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }
    
    /**
     * 生成测试用的大矩阵（随机生成0和1）
     */
    private static char[][] generateLargeMatrix(int rows, int cols) {
        char[][] matrix = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = Math.random() > 0.3 ? '1' : '0';  // 70%概率为1
            }
        }
        return matrix;
    }
}
