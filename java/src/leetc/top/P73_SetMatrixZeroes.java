package leetc.top;

/**
 * LeetCode 73. 矩阵置零 (Set Matrix Zeroes)
 * 
 * 问题描述：
 * 给定一个 m x n 的整数矩阵 matrix，如果一个元素为 0，则将其所在行和列的所有元素都设为 0。
 * 请使用原地算法。
 * 
 * 示例：
 * - 输入：matrix = [[1,1,1],[1,0,1],[1,1,1]]
 * - 输出：[[1,0,1],[0,0,0],[1,0,1]]
 * 
 * 进阶要求：
 * - 一个直观的解决方案是使用O(mn)的额外空间，但这并不是一个好的解决方案
 * - 一个简单的改进方案是使用O(m + n)的额外空间，但这仍然不是最好的解决方案
 * - 你能想出一个仅使用常数空间的解决方案吗？
 * 
 * 解法思路：
 * 原地标记法（O(1)空间复杂度）：
 * 1. 使用矩阵的第一行和第一列作为标记空间
 * 2. 先记录第一行和第一列本身是否包含0
 * 3. 遍历矩阵内部，如果发现0，就在对应的第一行和第一列位置标记
 * 4. 根据第一行和第一列的标记，将内部对应位置置零
 * 5. 最后处理第一行和第一列本身
 * 
 * 时间复杂度：O(m×n) - 需要遍历整个矩阵
 * 空间复杂度：O(1) - 只使用常数额外空间
 */
public class P73_SetMatrixZeroes {
    
    /**
     * 矩阵置零方法1（更清晰的版本）
     * 
     * 算法思路：
     * 1. 分别检查第一行和第一列是否包含0
     * 2. 遍历矩阵内部(1,1)到(m-1,n-1)，如果发现0：
     *    - 在第一行对应列标记：matrix[0][j] = 0
     *    - 在第一列对应行标记：matrix[i][0] = 0  
     * 3. 根据第一行和第一列的标记，将内部对应位置置零
     * 4. 如果第一行原本包含0，将整个第一行置零
     * 5. 如果第一列原本包含0，将整个第一列置零
     * 
     * @param matrix 输入矩阵（原地修改）
     */
    public static void setZeroes1(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return;
        }
        
        boolean row0zero = false;  // 第一行是否包含0
        boolean col0zero = false;  // 第一列是否包含0
        
        // 检查第一行是否包含0
        for (int i = 0; i < matrix[0].length; i++) {
            if (matrix[0][i] == 0) {
                row0zero = true;
                break;
            }
        }
        
        // 检查第一列是否包含0
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][0] == 0) {
                col0zero = true;
                break;
            }
        }
        
        // 遍历矩阵内部，标记需要置零的行列
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;  // 在第一列标记该行需要置零
                    matrix[0][j] = 0;  // 在第一行标记该列需要置零
                }
            }
        }
        
        // 根据第一行和第一列的标记，将内部对应位置置零
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[0].length; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }
        
        // 处理第一行
        if (row0zero) {
            for (int i = 0; i < matrix[0].length; i++) {
                matrix[0][i] = 0;
            }
        }
        
        // 处理第一列
        if (col0zero) {
            for (int i = 0; i < matrix.length; i++) {
                matrix[i][0] = 0;
            }
        }
    }

    /**
     * 矩阵置零方法2（优化版本）
     * 
     * 算法优化：
     * 1. 只用一个变量col0记录第一列是否需要置零
     * 2. 将第一行的第一个位置matrix[0][0]用来标记第一行是否置零
     * 3. 从下往上、从右往左处理，避免标记被提前清除
     * 
     * 核心思想：
     * - matrix[0][0]专门用来标记第一行
     * - col0变量专门用来标记第一列
     * - 这样可以减少一些条件判断，逻辑更简洁
     * 
     * @param matrix 输入矩阵（原地修改）
     */
    public static void setZeroes(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return;
        }
        
        boolean col0 = false;  // 第一列是否需要置零
        
        // 第一轮遍历：检测0并设置标记
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;  // 标记该行需要置零
                    if (j == 0) {
                        col0 = true;   // 第一列需要置零
                    } else {
                        matrix[0][j] = 0;  // 标记该列需要置零
                    }
                }
            }
        }
        
        // 第二轮遍历：根据标记置零（从下往上，从右往左）
        // 这样可以避免标记被提前清除
        for (int i = matrix.length - 1; i >= 0; i--) {
            for (int j = 1; j < matrix[0].length; j++) {  // 从第二列开始
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                     matrix[i][j] = 0;
                }
            }
        }
        
        // 最后处理第一列
        if (col0) {
            for (int i = 0; i < matrix.length; i++) {
                matrix[i][0] = 0;
            }
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1
        System.out.println("=== 测试用例1：setZeroes1 ===");
        int[][] matrix1 = {{1, 1, 1}, {1, 0, 1}, {1, 1, 1}};
        System.out.println("原矩阵:");
        printMatrix(matrix1);
        setZeroes1(matrix1);
        System.out.println("置零后:");
        printMatrix(matrix1);
        System.out.println();
        
        // 测试用例2
        System.out.println("=== 测试用例2：setZeroes ===");
        int[][] matrix2 = {{0, 1, 2, 0}, {3, 4, 5, 2}, {1, 3, 1, 5}};
        System.out.println("原矩阵:");
        printMatrix(matrix2);
        setZeroes(matrix2);
        System.out.println("置零后:");
        printMatrix(matrix2);
        System.out.println();
        
        // 测试边界情况：第一行第一列包含0
        System.out.println("=== 边界测试：第一行第一列包含0 ===");
        int[][] matrix3 = {{0, 1, 2}, {3, 4, 5}, {1, 0, 1}};
        System.out.println("原矩阵:");
        printMatrix(matrix3);
        setZeroes1(matrix3);
        System.out.println("置零后:");
        printMatrix(matrix3);
        System.out.println();
        
        // 测试单行矩阵
        System.out.println("=== 边界测试：单行矩阵 ===");
        int[][] matrix4 = {{1, 0, 3}};
        System.out.println("原矩阵:");
        printMatrix(matrix4);
        setZeroes(matrix4);
        System.out.println("置零后:");
        printMatrix(matrix4);
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
