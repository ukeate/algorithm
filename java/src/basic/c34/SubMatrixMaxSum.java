package basic.c34;

/**
 * 子矩阵最大累加和问题
 * 
 * 问题描述：
 * 给定一个二维整数矩阵，找到其中和最大的子矩阵，返回最大和值。
 * 子矩阵是由矩阵中连续的行和连续的列组成的矩形区域。
 * 
 * 算法思路：
 * 这是一维"最大子数组和"（Kadane算法）在二维空间的扩展：
 * 1. 枚举所有可能的行区间[i,j]
 * 2. 将行区间[i,j]的所有行按列压缩成一维数组
 * 3. 在压缩后的一维数组上应用Kadane算法
 * 4. 所有行区间的最大值就是答案
 * 
 * Kadane算法核心：
 * - 维护当前子数组和cur，如果cur<0则重新开始
 * - 每次更新最大值max
 * 
 * 与giant/c4/SubMatrixMaxSum.java的区别：
 * - 这个版本只求最大值，不返回位置信息
 * - 代码更简洁，专注于核心算法
 * 
 * 时间复杂度：O(M²*N)，其中M是行数，N是列数
 * 空间复杂度：O(N)，用于存储压缩后的一维数组
 */
public class SubMatrixMaxSum {
    
    /**
     * 计算矩阵中子矩阵的最大累加和
     * 
     * 算法详解：
     * 1. 外层循环i：枚举子矩阵的起始行
     * 2. 内层循环j：枚举子矩阵的结束行（从i开始）
     * 3. 对于每个行区间[i,j]，将多行压缩为一维数组s[]
     * 4. 在一维数组s[]上应用Kadane算法求最大子数组和
     * 
     * @param m 输入的二维矩阵
     * @return 子矩阵的最大累加和
     */
    public static int max(int[][] m) {
        // 边界条件检查
        if (m == null || m.length == 0 || m[0].length == 0) {
            return 0;
        }
        
        int max = Integer.MIN_VALUE;  // 全局最大和
        int cur = 0;                  // Kadane算法的当前和
        int[] s = null;               // 压缩数组
        
        // 外层循环：枚举子矩阵的起始行i
        for (int i = 0; i < m.length; i++) {
            s = new int[m[0].length];  // 重新初始化压缩数组
            
            // 内层循环：枚举子矩阵的结束行j（j >= i）
            for (int j = i; j < m.length; j++) {
                cur = 0;  // 重置Kadane算法的当前和
                
                // 处理第j行：将第j行加到压缩数组中，同时应用Kadane算法
                for (int k = 0; k < s.length; k++) {
                    s[k] += m[j][k];      // 将第j行第k列加到压缩数组
                    cur += s[k];          // Kadane算法：累加当前元素
                    max = Math.max(max, cur);  // 更新全局最大值
                    cur = cur < 0 ? 0 : cur;   // Kadane算法：负数时重新开始
                }
            }
        }
        
        return max;
    }

    /**
     * 测试方法：验证算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 子矩阵最大累加和测试 ===");
        
        // 测试用例1：包含正负数的矩阵
        int[][] m1 = {{-90, 48, 78}, {64, -40, 64}, {-81, -7, 66}};
        
        System.out.println("测试用例1：");
        printMatrix(m1);
        System.out.println("最大子矩阵和: " + max(m1));
        System.out.println();
        
        // 测试用例2：全为负数的矩阵
        int[][] m2 = {{-1, -2, -3}, {-4, -5, -6}, {-7, -8, -9}};
        
        System.out.println("测试用例2（全负数）：");
        printMatrix(m2);
        System.out.println("最大子矩阵和: " + max(m2));
        System.out.println();
        
        // 测试用例3：单元素矩阵
        int[][] m3 = {{42}};
        
        System.out.println("测试用例3（单元素）：");
        printMatrix(m3);
        System.out.println("最大子矩阵和: " + max(m3));
        System.out.println();
        
        // 测试用例4：包含零的矩阵
        int[][] m4 = {{1, -2, 3}, {0, 4, -1}, {-3, 2, 1}};
        
        System.out.println("测试用例4：");
        printMatrix(m4);
        System.out.println("最大子矩阵和: " + max(m4));
        
        System.out.println("=== 测试完成 ===");
    }
    
    /**
     * 辅助方法：打印矩阵
     * @param matrix 要打印的矩阵
     */
    private static void printMatrix(int[][] matrix) {
        System.out.println("矩阵:");
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("  ");
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.printf("%4d ", matrix[i][j]);
            }
            System.out.println();
        }
    }
}

