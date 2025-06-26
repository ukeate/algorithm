package giant.c4;

/**
 * 子矩阵最大和问题
 * 
 * 问题描述：
 * 给定一个二维整数矩阵，找到其中和最大的子矩阵，并返回该子矩阵的位置信息。
 * 子矩阵是由矩阵中连续的行和连续的列组成的矩形区域。
 * 
 * 算法思路：
 * 这是经典的"最大子序列和"问题在二维空间的扩展。
 * 核心思想是将二维问题降维为一维问题：
 * 1. 枚举所有可能的行区间[i,j]
 * 2. 对于每个行区间，将多行压缩为一行（列方向求和）
 * 3. 在压缩后的一维数组上应用Kadane算法求最大子序列和
 * 4. 在求解过程中记录最优子矩阵的边界信息
 * 
 * Kadane算法原理：
 * 对于一维数组，维护当前子序列和cur，如果cur<0则重新开始累加
 * 
 * 时间复杂度：O(M²*N)，其中M是行数，N是列数
 * 空间复杂度：O(N)，用于存储压缩后的一维数组
 * 
 * 参考链接：https://leetcode-cn.com/problems/max-submatrix-lcci/
 */
public class SubMatrixMaxSum {

    /**
     * 寻找矩阵中和最大的子矩阵
     * 
     * 算法详解：
     * 1. 外层双重循环枚举所有可能的行区间[i,j]
     * 2. 对于每个行区间，将矩阵在列方向上压缩成一维数组
     * 3. 在一维数组上使用Kadane算法求最大子序列和
     * 4. 同时记录最优解的行列边界位置
     * 
     * 变量说明：
     * - ln: 矩阵行数
     * - lm: 矩阵列数
     * - max: 当前找到的最大子矩阵和
     * - a, b, c, d: 最优子矩阵的边界坐标 [a,c]行区间，[b,d]列区间
     * - s[]: 压缩数组，s[k] = sum(m[i..j][k])
     * - cur: Kadane算法中的当前子序列和
     * - begin: Kadane算法中当前子序列的起始列位置
     * 
     * @param m 输入的二维矩阵
     * @return 数组[a,b,c,d]，表示最大子矩阵的左上角(a,b)和右下角(c,d)
     */
    public static int[] getMaxMatrix(int[][] m) {
        int ln = m.length;          // 矩阵行数
        int lm = m[0].length;       // 矩阵列数
        int max = Integer.MIN_VALUE; // 最大子矩阵和
        
        // 记录最优解的边界坐标
        int a = 0, b = 0, c = 0, d = 0;
        
        // 外层循环：枚举行区间的起始行i
        for (int i = 0; i < ln; i++) {
            int[] s = new int[lm];  // 压缩数组：存储从第i行到当前行j的列和
            
            // 内层循环：枚举行区间的结束行j
            for (int j = i; j < ln; j++) {
                // Kadane算法相关变量
                int cur = 0;        // 当前子序列和
                int begin = 0;      // 当前子序列的起始列位置
                
                // 对第j行进行处理：更新压缩数组并应用Kadane算法
                for (int k = 0; k < lm; k++) {
                    // 将第j行的元素累加到压缩数组中
                    s[k] += m[j][k];
                    
                    // Kadane算法：累加当前元素
                    cur += s[k];
                    
                    // 检查是否找到更大的子矩阵和
                    if (max < cur) {
                        max = cur;  // 更新最大值
                        a = i;      // 最优行区间起始
                        b = begin;  // 最优列区间起始
                        c = j;      // 最优行区间结束
                        d = k;      // 最优列区间结束
                    }
                    
                    // Kadane算法：如果当前和为负，重新开始
                    if (cur < 0) {
                        cur = 0;        // 重置当前和
                        begin = k + 1;  // 重置起始位置为下一列
                    }
                }
            }
        }
        
        // 返回最大子矩阵的边界坐标
        return new int[] {a, b, c, d};
    }
    
    /**
     * 辅助方法：打印矩阵
     * @param matrix 要打印的矩阵
     */
    private static void printMatrix(int[][] matrix) {
        System.out.println("矩阵内容:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.printf("%4d ", matrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * 辅助方法：计算指定子矩阵的和（用于验证）
     * @param matrix 原矩阵
     * @param a 起始行
     * @param b 起始列
     * @param c 结束行
     * @param d 结束列
     * @return 子矩阵的和
     */
    private static int calculateSubMatrixSum(int[][] matrix, int a, int b, int c, int d) {
        int sum = 0;
        for (int i = a; i <= c; i++) {
            for (int j = b; j <= d; j++) {
                sum += matrix[i][j];
            }
        }
        return sum;
    }
    
    /**
     * 辅助方法：打印子矩阵内容
     * @param matrix 原矩阵
     * @param a 起始行
     * @param b 起始列
     * @param c 结束行
     * @param d 结束列
     */
    private static void printSubMatrix(int[][] matrix, int a, int b, int c, int d) {
        System.out.println("最大子矩阵内容:");
        for (int i = a; i <= c; i++) {
            for (int j = b; j <= d; j++) {
                System.out.printf("%4d ", matrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * 测试方法：验证算法的正确性
     */
    public static void main(String[] args) {
        // 测试用例1：包含正负数的矩阵
        int[][] matrix1 = {
            {1, -2, 3, 4},
            {0, -1, 2, 1},
            {1, -1, 0, -2},
            {2, 1, -3, 1}
        };
        
        System.out.println("=== 测试用例1 ===");
        printMatrix(matrix1);
        
        int[] result1 = getMaxMatrix(matrix1);
        int sum1 = calculateSubMatrixSum(matrix1, result1[0], result1[1], result1[2], result1[3]);
        
        System.out.printf("最大子矩阵位置: 行[%d,%d], 列[%d,%d]\n", 
                         result1[0], result1[2], result1[1], result1[3]);
        System.out.println("最大子矩阵和: " + sum1);
        printSubMatrix(matrix1, result1[0], result1[1], result1[2], result1[3]);
        
        // 测试用例2：全为负数的矩阵
        int[][] matrix2 = {
            {-1, -2, -3},
            {-4, -5, -6},
            {-7, -8, -9}
        };
        
        System.out.println("=== 测试用例2（全负数）===");
        printMatrix(matrix2);
        
        int[] result2 = getMaxMatrix(matrix2);
        int sum2 = calculateSubMatrixSum(matrix2, result2[0], result2[1], result2[2], result2[3]);
        
        System.out.printf("最大子矩阵位置: 行[%d,%d], 列[%d,%d]\n", 
                         result2[0], result2[2], result2[1], result2[3]);
        System.out.println("最大子矩阵和: " + sum2);
        printSubMatrix(matrix2, result2[0], result2[1], result2[2], result2[3]);
        
        // 测试用例3：单元素矩阵
        int[][] matrix3 = {{42}};
        
        System.out.println("=== 测试用例3（单元素）===");
        printMatrix(matrix3);
        
        int[] result3 = getMaxMatrix(matrix3);
        int sum3 = calculateSubMatrixSum(matrix3, result3[0], result3[1], result3[2], result3[3]);
        
        System.out.printf("最大子矩阵位置: 行[%d,%d], 列[%d,%d]\n", 
                         result3[0], result3[2], result3[1], result3[3]);
        System.out.println("最大子矩阵和: " + sum3);
    }
}
