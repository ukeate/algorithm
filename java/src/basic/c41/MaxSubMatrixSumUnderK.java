package basic.c41;

import java.util.TreeSet;

/**
 * 子矩阵最大累加和不超过K的问题
 * 
 * 问题描述：
 * 给定一个m x n的矩阵和一个整数k，
 * 找到矩阵中累加和不超过k的最大子矩阵和
 * 
 * 算法思路：
 * 1. 矩阵压缩：枚举所有可能的行区间，将二维问题转化为一维问题
 * 2. 前缀和 + TreeSet：在一维数组上使用TreeSet快速查找满足条件的区间
 * 3. 核心公式：sum[i,j] = prefixSum[j] - prefixSum[i-1] <= k
 *            即：prefixSum[i-1] >= prefixSum[j] - k
 * 
 * 时间复杂度：O(m²n*logn) - m²个行区间，每个区间O(n*logn)
 * 空间复杂度：O(n) - TreeSet和列和数组
 * 
 * @author 算法学习
 */
public class MaxSubMatrixSumUnderK {
    
    /**
     * 计算矩阵中不超过k的最大子矩阵累加和
     * 
     * @param matrix 输入矩阵
     * @param k 上界值
     * @return 不超过k的最大子矩阵累加和
     * 
     * 算法核心：
     * 1. 枚举所有行区间[s,e]，将其压缩为一维列和数组
     * 2. 在列和数组上使用前缀和+TreeSet找到不超过k的最大子数组和
     * 3. TreeSet维护之前的前缀和，用ceiling方法快速查找
     */
    public static int max(int[][] matrix, int k) {
        if (matrix == null || matrix[0] == null) {
            return 0;
        }
        
        int row = matrix.length;
        int col = matrix[0].length;
        int res = Integer.MIN_VALUE;
        
        // TreeSet用于维护前缀和，支持快速查找
        TreeSet<Integer> set = new TreeSet<>();
        
        // 枚举所有可能的行区间起点
        for (int s = 0; s < row; s++) {
            // colSum[c] 表示第c列在行区间[s,e]内的累加和
            int[] colSum = new int[col];
            
            // 枚举所有可能的行区间终点
            for (int e = s; e < row; e++) {
                // 初始化TreeSet，添加0作为虚拟前缀和（表示空前缀）
                set.add(0);
                int rowSum = 0; // 当前行的前缀和
                
                // 遍历每一列，计算压缩后的一维数组
                for (int c = 0; c < col; c++) {
                    // 更新列和：加入新的一行
                    colSum[c] += matrix[e][c];
                    // 更新行的前缀和
                    rowSum += colSum[c];
                    
                    // 查找满足条件的前缀和
                    // 需要找到最小的prefixSum使得：prefixSum >= rowSum - k
                    // 这样 rowSum - prefixSum <= k
                    Integer it = set.ceiling(rowSum - k);
                    if (it != null) {
                        // 更新最大值：当前前缀和 - 找到的前缀和
                        res = Math.max(res, rowSum - it);
                    }
                    
                    // 将当前前缀和加入TreeSet供后续使用
                    set.add(rowSum);
                }
                
                // 清空TreeSet，准备处理下一个行区间
                set.clear();
            }
        }
        return res;
    }
}
