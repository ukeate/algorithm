package leetc.top;

import java.util.*;

/**
 * LeetCode 363. 矩形区域不超过 K 的最大数值和 (Max Sum of Rectangle No Larger Than K)
 * 
 * 问题描述：
 * 给你一个 m x n 的矩阵 matrix 和一个整数 k ，找出并返回矩阵内部矩形区域的不超过 k 的最大数值和。
 * 
 * 题目数据保证总会存在一个数值和不超过 k 的矩形区域。
 * 
 * 示例：
 * 输入：matrix = [[1,0,1],[0,-2,3]], k = 2
 * 输出：2
 * 解释：蓝色边框圈出来的矩形区域 [[0, 1], [-2, 3]] 的数值和是 2，且 2 是不超过 k = 2 的最大数值和。
 * 
 * 输入：matrix = [[2,2,-1]], k = 3
 * 输出：3
 * 
 * 提示：
 * - m == matrix.length
 * - n == matrix[i].length
 * - 1 <= m, n <= 100
 * - -100 <= matrix[i][j] <= 100
 * - -10^5 <= k <= 10^5
 * 
 * 解法思路：
 * 二维前缀和 + 一维子数组问题转换：
 * 
 * 1. 核心思想：
 *    - 将二维矩形问题转换为一维数组问题
 *    - 对于固定的上下边界，计算每列的和，转换为一维数组
 *    - 在一维数组中找不超过k的最大子数组和
 * 
 * 2. 一维子数组不超过k的最大和：
 *    - 使用前缀和 + TreeSet/二分查找
 *    - 对于每个位置i，寻找最小的前缀和j，使得sum[i] - sum[j] <= k
 *    - 利用TreeSet的有序性快速查找
 * 
 * 3. 算法步骤：
 *    - 枚举所有可能的上下边界组合
 *    - 对于每个组合，计算列方向的前缀和数组
 *    - 在一维数组中找不超过k的最大子数组和
 * 
 * 4. 优化策略：
 *    - 当m > n时，转置矩阵优化时间复杂度
 *    - 使用TreeSet优化一维子数组查找
 *    - 早期剪枝：如果找到和为k的矩形，直接返回
 * 
 * 核心思想：
 * - 降维处理：二维问题转换为一维问题
 * - 前缀和技巧：快速计算区间和
 * - 有序查找：利用TreeSet的有序性优化查找
 * 
 * 关键技巧：
 * - 行列转换：根据m和n的大小选择最优遍历方向
 * - 二分查找：在有序集合中快速定位
 * - 剪枝优化：达到最优值时提前返回
 * 
 * 时间复杂度：O(min(m,n)^2 * max(m,n) * log(max(m,n))) 
 * 空间复杂度：O(max(m,n)) - TreeSet和前缀和数组
 * 
 * LeetCode链接：https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/
 */
public class P363_MaxSumOfRectangleNoLargerThanK {
    
    /**
     * 方法一：标准解法（推荐）
     * 
     * 枚举上下边界 + 一维子数组最大和问题
     * 
     * @param matrix 输入矩阵
     * @param k 上限值
     * @return 不超过k的矩形最大数值和
     */
    public int maxSumSubmatrix(int[][] matrix, int k) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        
        int m = matrix.length;
        int n = matrix[0].length;
        
        // 优化：如果行数大于列数，转置矩阵以减少时间复杂度
        if (m > n) {
            return maxSumSubmatrix(transpose(matrix), k);
        }
        
        int maxSum = Integer.MIN_VALUE;
        
        // 枚举上下边界
        for (int top = 0; top < m; top++) {
            int[] colSums = new int[n]; // 存储每列在[top, bottom]范围内的和
            
            for (int bottom = top; bottom < m; bottom++) {
                // 更新每列的和
                for (int col = 0; col < n; col++) {
                    colSums[col] += matrix[bottom][col];
                }
                
                // 在一维数组中找不超过k的最大子数组和
                int currentMax = maxSubarraySum(colSums, k);
                maxSum = Math.max(maxSum, currentMax);
                
                // 如果找到等于k的和，直接返回
                if (maxSum == k) {
                    return k;
                }
            }
        }
        
        return maxSum;
    }
    
    /**
     * 在一维数组中找不超过k的最大子数组和
     * 
     * 使用前缀和 + TreeSet优化查找
     * 
     * @param nums 一维数组
     * @param k 上限值
     * @return 不超过k的最大子数组和
     */
    private int maxSubarraySum(int[] nums, int k) {
        int maxSum = Integer.MIN_VALUE;
        int prefixSum = 0;
        
        // TreeSet用于存储前缀和，保持有序
        TreeSet<Integer> prefixSums = new TreeSet<>();
        prefixSums.add(0); // 添加初始前缀和0
        
        for (int num : nums) {
            prefixSum += num;
            
            // 寻找最小的前缀和prev，使得prefixSum - prev <= k
            // 即prev >= prefixSum - k
            Integer prev = prefixSums.ceiling(prefixSum - k);
            
            if (prev != null) {
                maxSum = Math.max(maxSum, prefixSum - prev);
            }
            
            prefixSums.add(prefixSum);
        }
        
        return maxSum;
    }
    
    /**
     * 矩阵转置
     * 
     * @param matrix 原矩阵
     * @return 转置后的矩阵
     */
    private int[][] transpose(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int[][] transposed = new int[n][m];
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }
        
        return transposed;
    }
    
    /**
     * 方法二：暴力解法（用于对比和理解）
     * 
     * 枚举所有可能的矩形区域
     * 
     * @param matrix 输入矩阵
     * @param k 上限值
     * @return 不超过k的矩形最大数值和
     */
    public int maxSumSubmatrixBruteForce(int[][] matrix, int k) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        
        int m = matrix.length;
        int n = matrix[0].length;
        int maxSum = Integer.MIN_VALUE;
        
        // 计算二维前缀和
        int[][] prefixSum = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                prefixSum[i][j] = matrix[i-1][j-1] + prefixSum[i-1][j] + 
                                 prefixSum[i][j-1] - prefixSum[i-1][j-1];
            }
        }
        
        // 枚举所有可能的矩形
        for (int top = 0; top < m; top++) {
            for (int left = 0; left < n; left++) {
                for (int bottom = top; bottom < m; bottom++) {
                    for (int right = left; right < n; right++) {
                        // 计算矩形[top, left, bottom, right]的和
                        int sum = prefixSum[bottom + 1][right + 1] - 
                                 prefixSum[top][right + 1] - 
                                 prefixSum[bottom + 1][left] + 
                                 prefixSum[top][left];
                        
                        if (sum <= k) {
                            maxSum = Math.max(maxSum, sum);
                        }
                    }
                }
            }
        }
        
        return maxSum;
    }
    
    /**
     * 方法三：优化的一维子数组解法
     * 
     * 使用更高效的一维子数组算法
     * 
     * @param matrix 输入矩阵
     * @param k 上限值
     * @return 不超过k的矩形最大数值和
     */
    public int maxSumSubmatrixOptimized(int[][] matrix, int k) {
        int m = matrix.length;
        int n = matrix[0].length;
        int maxSum = Integer.MIN_VALUE;
        
        // 选择较小的维度作为外层循环，减少时间复杂度
        boolean transposeFlag = m > n;
        if (transposeFlag) {
            matrix = transpose(matrix);
            int temp = m;
            m = n;
            n = temp;
        }
        
        for (int top = 0; top < m; top++) {
            int[] colSums = new int[n];
            
            for (int bottom = top; bottom < m; bottom++) {
                // 更新列和
                for (int col = 0; col < n; col++) {
                    colSums[col] += matrix[bottom][col];
                }
                
                // 优化的一维子数组算法
                int currentMax = maxSubarraySumOptimized(colSums, k);
                maxSum = Math.max(maxSum, currentMax);
                
                // 早期终止
                if (maxSum == k) {
                    return k;
                }
            }
        }
        
        return maxSum;
    }
    
    /**
     * 优化的一维子数组最大和算法
     * 
     * @param nums 一维数组
     * @param k 上限值
     * @return 不超过k的最大子数组和
     */
    private int maxSubarraySumOptimized(int[] nums, int k) {
        int n = nums.length;
        int maxSum = Integer.MIN_VALUE;
        
        // 如果数组较小，使用O(n^2)的简单算法
        if (n <= 100) {
            for (int i = 0; i < n; i++) {
                int sum = 0;
                for (int j = i; j < n; j++) {
                    sum += nums[j];
                    if (sum <= k) {
                        maxSum = Math.max(maxSum, sum);
                    }
                }
            }
            return maxSum;
        }
        
        // 对于较大数组，使用TreeSet优化
        return maxSubarraySum(nums, k);
    }
    
    /**
     * 方法四：分治法实现
     * 
     * 使用分治思想解决问题
     * 
     * @param matrix 输入矩阵
     * @param k 上限值
     * @return 不超过k的矩形最大数值和
     */
    public int maxSumSubmatrixDivideConquer(int[][] matrix, int k) {
        int m = matrix.length;
        int n = matrix[0].length;
        
        // 递归分治处理
        return divideConquer(matrix, 0, 0, m - 1, n - 1, k);
    }
    
    /**
     * 分治递归函数
     * 
     * @param matrix 矩阵
     * @param r1 起始行
     * @param c1 起始列
     * @param r2 结束行
     * @param c2 结束列
     * @param k 上限值
     * @return 该区域内不超过k的最大矩形和
     */
    private int divideConquer(int[][] matrix, int r1, int c1, int r2, int c2, int k) {
        // 基础情况：单个元素
        if (r1 == r2 && c1 == c2) {
            return matrix[r1][c1] <= k ? matrix[r1][c1] : Integer.MIN_VALUE;
        }
        
        int maxSum = Integer.MIN_VALUE;
        
        // 如果区域较小，直接使用暴力法
        if ((r2 - r1 + 1) * (c2 - c1 + 1) <= 16) {
            return bruteForceSubregion(matrix, r1, c1, r2, c2, k);
        }
        
        // 分治：水平分割
        if (r2 - r1 >= c2 - c1) {
            int midRow = r1 + (r2 - r1) / 2;
            maxSum = Math.max(maxSum, divideConquer(matrix, r1, c1, midRow, c2, k));
            maxSum = Math.max(maxSum, divideConquer(matrix, midRow + 1, c1, r2, c2, k));
            
            // 跨越中线的矩形
            maxSum = Math.max(maxSum, crossBoundaryMaxSum(matrix, r1, c1, r2, c2, midRow, k, true));
        } else {
            // 垂直分割
            int midCol = c1 + (c2 - c1) / 2;
            maxSum = Math.max(maxSum, divideConquer(matrix, r1, c1, r2, midCol, k));
            maxSum = Math.max(maxSum, divideConquer(matrix, r1, midCol + 1, r2, c2, k));
            
            // 跨越中线的矩形
            maxSum = Math.max(maxSum, crossBoundaryMaxSum(matrix, r1, c1, r2, c2, midCol, k, false));
        }
        
        return maxSum;
    }
    
    /**
     * 计算跨越分割线的矩形最大和
     * 
     * @param matrix 矩阵
     * @param r1 起始行
     * @param c1 起始列
     * @param r2 结束行
     * @param c2 结束列
     * @param mid 分割线位置
     * @param k 上限值
     * @param isRowSplit 是否为行分割
     * @return 跨越分割线的最大矩形和
     */
    private int crossBoundaryMaxSum(int[][] matrix, int r1, int c1, int r2, int c2, 
                                   int mid, int k, boolean isRowSplit) {
        // 实现跨越分割线的矩形和计算
        // 这里简化实现，实际中需要更复杂的处理
        return bruteForceSubregion(matrix, r1, c1, r2, c2, k);
    }
    
    /**
     * 暴力计算子区域的最大矩形和
     * 
     * @param matrix 矩阵
     * @param r1 起始行
     * @param c1 起始列
     * @param r2 结束行
     * @param c2 结束列
     * @param k 上限值
     * @return 该子区域内不超过k的最大矩形和
     */
    private int bruteForceSubregion(int[][] matrix, int r1, int c1, int r2, int c2, int k) {
        int maxSum = Integer.MIN_VALUE;
        
        for (int top = r1; top <= r2; top++) {
            for (int left = c1; left <= c2; left++) {
                int sum = 0;
                for (int bottom = top; bottom <= r2; bottom++) {
                    for (int right = left; right <= c2; right++) {
                        sum += matrix[bottom][right];
                        if (sum <= k) {
                            maxSum = Math.max(maxSum, sum);
                        }
                    }
                    // 重置sum以计算下一行
                    if (bottom < r2) {
                        sum = 0;
                        for (int col = left; col <= c2; col++) {
                            for (int row = top; row <= bottom; row++) {
                                sum += matrix[row][col];
                            }
                        }
                    }
                }
            }
        }
        
        return maxSum;
    }
    
    /**
     * 方法五：Kadane算法的变种
     * 
     * 结合Kadane算法的思想处理二维问题
     * 
     * @param matrix 输入矩阵
     * @param k 上限值
     * @return 不超过k的矩形最大数值和
     */
    public int maxSumSubmatrixKadane(int[][] matrix, int k) {
        int m = matrix.length;
        int n = matrix[0].length;
        int result = Integer.MIN_VALUE;
        
        for (int top = 0; top < m; top++) {
            int[] temp = new int[n];
            
            for (int bottom = top; bottom < m; bottom++) {
                // 更新temp数组
                for (int col = 0; col < n; col++) {
                    temp[col] += matrix[bottom][col];
                }
                
                // 在一维数组temp中找最大子数组和 <= k
                int currentMax = kadaneVariant(temp, k);
                result = Math.max(result, currentMax);
            }
        }
        
        return result;
    }
    
    /**
     * Kadane算法的变种：找不超过k的最大子数组和
     * 
     * @param nums 一维数组
     * @param k 上限值
     * @return 不超过k的最大子数组和
     */
    private int kadaneVariant(int[] nums, int k) {
        int maxSum = Integer.MIN_VALUE;
        
        // 使用前缀和 + TreeSet
        TreeSet<Integer> prefixSums = new TreeSet<>();
        prefixSums.add(0);
        int prefixSum = 0;
        
        for (int num : nums) {
            prefixSum += num;
            
            // 找到最小的前缀和，使得当前前缀和减去它不超过k
            Integer ceiling = prefixSums.ceiling(prefixSum - k);
            if (ceiling != null) {
                maxSum = Math.max(maxSum, prefixSum - ceiling);
            }
            
            prefixSums.add(prefixSum);
        }
        
        return maxSum;
    }
    
    /**
     * 辅助方法：打印矩阵（用于调试）
     * 
     * @param matrix 矩阵
     */
    private void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println();
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P363_MaxSumOfRectangleNoLargerThanK solution = new P363_MaxSumOfRectangleNoLargerThanK();
        
        // 测试用例1
        int[][] matrix1 = {{1, 0, 1}, {0, -2, 3}};
        int k1 = 2;
        
        System.out.println("测试用例1:");
        solution.printMatrix(matrix1);
        System.out.println("k = " + k1);
        
        int result1a = solution.maxSumSubmatrix(matrix1, k1);
        int result1b = solution.maxSumSubmatrixBruteForce(matrix1, k1);
        int result1c = solution.maxSumSubmatrixOptimized(matrix1, k1);
        int result1d = solution.maxSumSubmatrixKadane(matrix1, k1);
        
        System.out.println("标准解法: " + result1a);
        System.out.println("暴力解法: " + result1b);
        System.out.println("优化解法: " + result1c);
        System.out.println("Kadane变种: " + result1d);
        System.out.println("期望结果: 2\n");
        
        // 测试用例2
        int[][] matrix2 = {{2, 2, -1}};
        int k2 = 3;
        
        System.out.println("测试用例2:");
        solution.printMatrix(matrix2);
        System.out.println("k = " + k2);
        
        int result2a = solution.maxSumSubmatrix(matrix2, k2);
        int result2b = solution.maxSumSubmatrixBruteForce(matrix2, k2);
        int result2c = solution.maxSumSubmatrixOptimized(matrix2, k2);
        int result2d = solution.maxSumSubmatrixKadane(matrix2, k2);
        
        System.out.println("标准解法: " + result2a);
        System.out.println("暴力解法: " + result2b);
        System.out.println("优化解法: " + result2c);
        System.out.println("Kadane变种: " + result2d);
        System.out.println("期望结果: 3\n");
        
        // 测试用例3：负数矩阵
        int[][] matrix3 = {{-1, -2}, {-3, -4}};
        int k3 = -2;
        
        System.out.println("测试用例3（负数矩阵）:");
        solution.printMatrix(matrix3);
        System.out.println("k = " + k3);
        
        int result3a = solution.maxSumSubmatrix(matrix3, k3);
        int result3b = solution.maxSumSubmatrixBruteForce(matrix3, k3);
        
        System.out.println("标准解法: " + result3a);
        System.out.println("暴力解法: " + result3b);
        System.out.println("期望结果: -2\n");
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        int[][] largeMatrix = generateRandomMatrix(50, 50);
        int kLarge = 100;
        
        long start = System.currentTimeMillis();
        int resultLarge = solution.maxSumSubmatrix(largeMatrix, kLarge);
        long end = System.currentTimeMillis();
        
        System.out.println("50x50矩阵处理时间: " + (end - start) + "ms");
        System.out.println("结果: " + resultLarge);
    }
    
    /**
     * 生成随机矩阵用于性能测试
     * 
     * @param m 行数
     * @param n 列数
     * @return 随机矩阵
     */
    private static int[][] generateRandomMatrix(int m, int n) {
        Random random = new Random();
        int[][] matrix = new int[m][n];
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = random.nextInt(201) - 100; // -100 到 100
            }
        }
        
        return matrix;
    }
}
