package base.stack;

import java.util.Stack;

/**
 * 最大矩形面积问题 - 单调栈的经典应用
 * 
 * 问题描述：
 * 给定一个二维二进制矩阵，找出只包含1的最大矩形，并返回其面积
 * 
 * 例如：
 * [
 *   ["1","0","1","0","0"],
 *   ["1","0","1","1","1"],
 *   ["1","1","1","1","1"],
 *   ["1","0","0","1","0"]
 * ]
 * 最大矩形面积为6（3x2的矩形）
 * 
 * 解决思路：
 * 将二维问题转化为多个一维"柱状图中最大矩形"问题
 * 1. 对于每一行，计算以该行为底的柱状图的高度数组
 * 2. 对每个高度数组，使用单调栈求解柱状图中的最大矩形面积
 * 3. 取所有行中的最大值
 * 
 * 算法原理：
 * 1. 预处理：对于每一行，计算每个位置向上连续1的个数作为高度
 * 2. 对每一行的高度数组，使用单调栈算法求最大矩形面积
 * 3. 单调栈算法：对每个柱子，找到左右边界，计算以该柱子为高的最大矩形
 * 
 * 时间复杂度：O(M*N) - M为行数，N为列数，每个元素最多入栈出栈一次
 * 空间复杂度：O(N) - 高度数组和单调栈的空间
 * 
 * 应用场景：
 * - LeetCode 85. Maximal Rectangle
 * - 图像处理中的矩形检测
 * - 数据可视化中的区域分析
 * - 建筑设计中的空间优化
 */
public class MaxRectangle {
    
    /**
     * 求二进制矩阵中最大矩形面积
     * 
     * @param matrix 二进制矩阵（字符数组）
     * @return 最大矩形面积
     */
    public static int maximalRectangle(char[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[] heights = new int[cols];  // 当前行的高度数组
        int maxArea = 0;
        
        // 逐行处理
        for (int i = 0; i < rows; i++) {
            // 更新高度数组
            updateHeights(matrix, i, heights);
            
            // 计算当前行为底的柱状图中的最大矩形面积
            int currentMaxArea = largestRectangleInHistogram(heights);
            maxArea = Math.max(maxArea, currentMaxArea);
        }
        
        return maxArea;
    }

    /**
     * 更新高度数组
     * 
     * 对于每个位置，如果当前位置是'1'，则高度为上一行该位置的高度+1
     * 如果当前位置是'0'，则高度重置为0
     * 
     * @param matrix 二进制矩阵
     * @param row 当前行索引
     * @param heights 高度数组（会被修改）
     */
    private static void updateHeights(char[][] matrix, int row, int[] heights) {
        for (int col = 0; col < matrix[row].length; col++) {
            if (matrix[row][col] == '1') {
                heights[col] += 1;  // 高度累加
            } else {
                heights[col] = 0;   // 高度重置
            }
        }
    }

    /**
     * 柱状图中最大矩形面积 - 单调栈解法
     * 
     * 算法详解：
     * 1. 维护一个单调递增栈，存储柱子的索引
     * 2. 当遇到比栈顶更小的柱子时，弹出栈顶并计算以栈顶为高的矩形面积
     * 3. 矩形的宽度 = 右边界 - 左边界 - 1
     * 4. 右边界是当前位置，左边界是新栈顶位置
     * 
     * 举例说明（heights = [2,1,5,6,2,3]）：
     * - 处理到index=4，height=2时，栈中有[1,2,3]（对应高度[1,5,6]）
     * - 2 < 6，弹出index=3，以高度6计算矩形：宽度=4-2-1=1，面积=6*1=6
     * - 2 < 5，弹出index=2，以高度5计算矩形：宽度=4-1-1=2，面积=5*2=10
     * 
     * @param heights 柱状图高度数组
     * @return 最大矩形面积
     */
    private static int largestRectangleInHistogram(int[] heights) {
        if (heights == null || heights.length == 0) {
            return 0;
        }
        
        Stack<Integer> stack = new Stack<>();  // 单调递增栈，存储索引
        int maxArea = 0;
        
        for (int i = 0; i < heights.length; i++) {
            // 当前高度小于栈顶高度时，计算以栈顶为高的矩形面积
            while (!stack.isEmpty() && heights[i] < heights[stack.peek()]) {
                int height = heights[stack.pop()];  // 弹出栈顶作为矩形高度
                
                // 计算矩形宽度
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                
                // 更新最大面积
                maxArea = Math.max(maxArea, height * width);
            }
            
            // 当前索引入栈
            stack.push(i);
        }
        
        // 处理栈中剩余元素
        while (!stack.isEmpty()) {
            int height = heights[stack.pop()];
            int width = stack.isEmpty() ? heights.length : heights.length - stack.peek() - 1;
            maxArea = Math.max(maxArea, height * width);
        }
        
        return maxArea;
    }

    /**
     * 暴力解法 - 用于验证算法正确性
     * 
     * 枚举所有可能的矩形，检查是否全为1
     * 时间复杂度：O(M²*N²*M*N) = O(M³*N³)
     * 
     * @param matrix 二进制矩阵
     * @return 最大矩形面积
     */
    private static int maximalRectangleSure(char[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        
        int rows = matrix.length;
        int cols = matrix[0].length;
        int maxArea = 0;
        
        // 枚举所有可能的矩形
        for (int r1 = 0; r1 < rows; r1++) {
            for (int c1 = 0; c1 < cols; c1++) {
                for (int r2 = r1; r2 < rows; r2++) {
                    for (int c2 = c1; c2 < cols; c2++) {
                        // 检查矩形[r1,c1]到[r2,c2]是否全为1
                        if (isAllOnes(matrix, r1, c1, r2, c2)) {
                            int area = (r2 - r1 + 1) * (c2 - c1 + 1);
                            maxArea = Math.max(maxArea, area);
                        }
                    }
                }
            }
        }
        
        return maxArea;
    }

    /**
     * 检查指定矩形区域是否全为1
     * 
     * @param matrix 矩阵
     * @param r1 起始行
     * @param c1 起始列
     * @param r2 结束行
     * @param c2 结束列
     * @return 是否全为1
     */
    private static boolean isAllOnes(char[][] matrix, int r1, int c1, int r2, int c2) {
        for (int r = r1; r <= r2; r++) {
            for (int c = c1; c <= c2; c++) {
                if (matrix[r][c] != '1') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 生成随机二进制矩阵用于测试
     * 
     * @param maxRows 最大行数
     * @param maxCols 最大列数
     * @return 随机二进制矩阵
     */
    private static char[][] randomMatrix(int maxRows, int maxCols) {
        int rows = (int) (Math.random() * maxRows) + 1;
        int cols = (int) (Math.random() * maxCols) + 1;
        char[][] matrix = new char[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = Math.random() < 0.5 ? '0' : '1';
            }
        }
        
        return matrix;
    }

    /**
     * 复制矩阵
     * 
     * @param matrix 原矩阵
     * @return 复制的矩阵
     */
    private static char[][] copyMatrix(char[][] matrix) {
        if (matrix == null) {
            return null;
        }
        
        char[][] copy = new char[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            copy[i] = matrix[i].clone();
        }
        return copy;
    }

    /**
     * 打印矩阵
     * 
     * @param matrix 要打印的矩阵
     */
    private static void printMatrix(char[][] matrix) {
        if (matrix == null) {
            System.out.println("null");
            return;
        }
        
        System.out.println("矩阵：");
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("[");
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print("\"" + matrix[i][j] + "\"");
                if (j < matrix[i].length - 1) {
                    System.out.print(",");
                }
            }
            System.out.println("]" + (i < matrix.length - 1 ? "," : ""));
        }
    }

    /**
     * 测试方法 - 验证最大矩形算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 最大矩形面积问题测试 ===");
        
        // 手工测试用例1
        char[][] testMatrix1 = {
            {'1','0','1','0','0'},
            {'1','0','1','1','1'},
            {'1','1','1','1','1'},
            {'1','0','0','1','0'}
        };
        
        System.out.println("\n测试用例1：");
        printMatrix(testMatrix1);
        
        char[][] copy1 = copyMatrix(testMatrix1);
        char[][] copy2 = copyMatrix(testMatrix1);
        
        int result1 = maximalRectangle(copy1);
        int result2 = maximalRectangleSure(copy2);
        
        System.out.println("单调栈解法：" + result1);
        System.out.println("暴力解法：" + result2);
        System.out.println("结果正确：" + (result1 == result2));
        
        // 手工测试用例2 - 全0矩阵
        char[][] testMatrix2 = {
            {'0','0','0'},
            {'0','0','0'},
            {'0','0','0'}
        };
        
        System.out.println("\n测试用例2（全0矩阵）：");
        printMatrix(testMatrix2);
        
        char[][] copy3 = copyMatrix(testMatrix2);
        char[][] copy4 = copyMatrix(testMatrix2);
        
        int result3 = maximalRectangle(copy3);
        int result4 = maximalRectangleSure(copy4);
        
        System.out.println("单调栈解法：" + result3);
        System.out.println("暴力解法：" + result4);
        System.out.println("结果正确：" + (result3 == result4));
        
        // 手工测试用例3 - 全1矩阵
        char[][] testMatrix3 = {
            {'1','1','1'},
            {'1','1','1'},
            {'1','1','1'}
        };
        
        System.out.println("\n测试用例3（全1矩阵）：");
        printMatrix(testMatrix3);
        
        char[][] copy5 = copyMatrix(testMatrix3);
        char[][] copy6 = copyMatrix(testMatrix3);
        
        int result5 = maximalRectangle(copy5);
        int result6 = maximalRectangleSure(copy6);
        
        System.out.println("单调栈解法：" + result5);
        System.out.println("暴力解法：" + result6);
        System.out.println("结果正确：" + (result5 == result6));
        
        // 大规模随机测试
        System.out.println("\n=== 随机测试 ===");
        int times = 1000;  // 减少测试次数，因为暴力解法很慢
        int maxRows = 8;   // 减小矩阵规模
        int maxCols = 8;
        System.out.println("测试次数：" + times);
        System.out.println("最大矩阵规模：" + maxRows + "x" + maxCols);
        
        boolean success = true;
        for (int i = 0; i < times; i++) {
            char[][] matrix = randomMatrix(maxRows, maxCols);
            
            char[][] copy1Test = copyMatrix(matrix);
            char[][] copy2Test = copyMatrix(matrix);
            
            int ans1 = maximalRectangle(copy1Test);
            int ans2 = maximalRectangleSure(copy2Test);
            
            if (ans1 != ans2) {
                System.out.println("第" + (i + 1) + "次测试失败！");
                System.out.println("单调栈解法：" + ans1);
                System.out.println("暴力解法：" + ans2);
                System.out.println("测试矩阵：");
                printMatrix(matrix);
                success = false;
                break;
            }
        }
        
        if (success) {
            System.out.println("所有测试通过！最大矩形面积算法实现正确。");
        } else {
            System.out.println("测试失败！请检查算法实现。");
        }
        
        System.out.println("=== 测试结束 ===");
    }
}
