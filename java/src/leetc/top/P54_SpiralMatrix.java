package leetc.top;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 54. 螺旋矩阵 (Spiral Matrix)
 * 
 * 问题描述：
 * 给你一个 m x n 矩阵 matrix，请按照顺时针螺旋顺序，返回矩阵中的所有元素。
 * 
 * 示例：
 * - 输入：matrix = [[1,2,3],[4,5,6],[7,8,9]]
 * - 输出：[1,2,3,6,9,8,7,4,5]
 * - 解释：按螺旋顺序：右→下→左→上
 * 
 * 解法思路：
 * 分层遍历（洋葱剥皮法）：
 * 1. 定义四个边界：上边界a，左边界b，下边界c，右边界d
 * 2. 每次处理矩阵的最外层，按顺时针方向遍历一圈
 * 3. 遍历完一层后，边界内缩，继续处理内层
 * 4. 直到所有元素都被访问
 * 
 * 遍历顺序：
 * - 上边：从左到右 (a, b) → (a, d)
 * - 右边：从上到下 (a, d) → (c, d)  
 * - 下边：从右到左 (c, d) → (c, b)
 * - 左边：从下到上 (c, b) → (a, b)
 * 
 * 边界情况：
 * - 只有一行：只需要从左到右
 * - 只有一列：只需要从上到下
 * - 1x1矩阵：只有一个元素
 * 
 * 时间复杂度：O(m×n) - 需要访问每个元素一次
 * 空间复杂度：O(1) - 除结果数组外，只使用常数额外空间
 */
public class P54_SpiralMatrix {
    
    /**
     * 添加矩形边界的所有元素到结果列表
     * 
     * 算法详解：
     * 1. 特殊情况处理：
     *    - 如果a==c（只有一行）：从左到右添加
     *    - 如果b==d（只有一列）：从上到下添加
     * 2. 一般情况：按顺时针方向添加四条边
     *    - 上边：(a,b) → (a,d-1)
     *    - 右边：(a,d) → (c-1,d)  
     *    - 下边：(c,d) → (c,b+1)
     *    - 左边：(c,b) → (a+1,b)
     * 
     * @param m 矩阵
     * @param a 上边界行号
     * @param b 左边界列号
     * @param c 下边界行号
     * @param d 右边界列号
     * @param ans 结果列表
     */
    private static void addEdge(int[][] m, int a, int b, int c, int d, List<Integer> ans) {
        if (a == c) {
            // 特殊情况：只有一行，从左到右遍历
            for (int i = b; i <= d; i++) {
                ans.add(m[a][i]);
            }
        } else if (b == d) {
            // 特殊情况：只有一列，从上到下遍历
            for (int i = a; i <= c; i++) {
                ans.add(m[i][b]);
            }
        } else {
            // 一般情况：按顺时针方向遍历四条边
            
            // 第一步：遍历上边 - 从左到右
            int curC = b;   // 当前列位置
            int curR = a;   // 当前行位置
            while (curC != d) {
                ans.add(m[a][curC]);
                curC++;
            }
            
            // 第二步：遍历右边 - 从上到下
            while (curR != c) {
                ans.add(m[curR][d]);
                curR++;
            }
            
            // 第三步：遍历下边 - 从右到左
            while (curC != b) {
                ans.add(m[c][curC]);
                curC--;
            }
            
            // 第四步：遍历左边 - 从下到上
            while (curR != a) {
                ans.add(m[curR][b]);
                curR--;
            }
        }
    }

    /**
     * 螺旋顺序遍历矩阵
     * 
     * 算法步骤：
     * 1. 边界检查：处理空矩阵的情况
     * 2. 初始化四个边界：a=0, b=0, c=行数-1, d=列数-1
     * 3. 循环处理每一层：
     *    - 调用addEdge处理当前层的边界
     *    - 边界内缩：a++, b++, c--, d--
     * 4. 当a > c 或 b > d时，所有元素已遍历完成
     * 
     * @param matrix 输入矩阵
     * @return 螺旋顺序的元素列表
     */
    public static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> ans = new ArrayList<>();
        
        // 边界检查：处理空矩阵
        if (matrix == null || matrix.length == 0 || matrix[0] == null || matrix[0].length == 0) {
            return ans;
        }
        
        // 初始化四个边界
        int a = 0;                      // 上边界
        int b = 0;                      // 左边界  
        int c = matrix.length - 1;      // 下边界
        int d = matrix[0].length - 1;   // 右边界
        
        // 分层遍历：从外层到内层
        while (a <= c && b <= d) {
            // 处理当前层的边界
            addEdge(matrix, a++, b++, c--, d--, ans);
        }
        
        return ans;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：3x3矩阵
        int[][] matrix1 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        List<Integer> result1 = spiralOrder(matrix1);
        System.out.println("输入矩阵:");
        printMatrix(matrix1);
        System.out.println("螺旋遍历结果: " + result1);
        System.out.println("期望结果: [1, 2, 3, 6, 9, 8, 7, 4, 5]");
        System.out.println();
        
        // 测试用例2：3x4矩阵
        int[][] matrix2 = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
        List<Integer> result2 = spiralOrder(matrix2);
        System.out.println("输入矩阵:");
        printMatrix(matrix2);
        System.out.println("螺旋遍历结果: " + result2);
        System.out.println("期望结果: [1, 2, 3, 4, 8, 12, 11, 10, 9, 5, 6, 7]");
        System.out.println();
        
        // 测试用例3：单行矩阵
        int[][] matrix3 = {{2, 3}};
        List<Integer> result3 = spiralOrder(matrix3);
        System.out.println("单行矩阵: [[2, 3]]");
        System.out.println("螺旋遍历结果: " + result3);
        System.out.println("期望结果: [2, 3]");
        System.out.println();
        
        // 测试用例4：单列矩阵
        int[][] matrix4 = {{2}, {3}};
        List<Integer> result4 = spiralOrder(matrix4);
        System.out.println("单列矩阵: [[2], [3]]");
        System.out.println("螺旋遍历结果: " + result4);
        System.out.println("期望结果: [2, 3]");
    }
    
    /**
     * 辅助方法：打印矩阵
     */
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.print("[");
            for (int i = 0; i < row.length; i++) {
                System.out.print(row[i]);
                if (i < row.length - 1) System.out.print(", ");
            }
            System.out.println("]");
        }
    }
}
