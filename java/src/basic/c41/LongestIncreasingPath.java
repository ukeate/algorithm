package basic.c41;

/**
 * 矩阵中最长递增路径问题
 * 
 * 问题描述：
 * 给定一个m x n的整数矩阵，找出其中最长递增路径的长度
 * 对于每个单元格，可以往上、下、左、右四个方向移动
 * 不能对角线移动或移动到边界外，也不能在同一个单元格上停留
 * 
 * 算法思路：
 * 使用记忆化搜索（DFS + 动态规划）
 * 对每个位置计算以该位置为起点的最长递增路径长度
 * 
 * 时间复杂度：O(m*n) - 每个位置最多计算一次
 * 空间复杂度：O(m*n) - dp数组 + 递归栈空间
 * 
 * @author 算法学习
 */
public class LongestIncreasingPath {
    
    /**
     * 计算从位置(i,j)开始的最长递增路径长度
     * 
     * @param matrix 输入矩阵
     * @param i 当前行坐标
     * @param j 当前列坐标
     * @param dp 记忆化数组，dp[i][j]表示从(i,j)开始的最长路径长度
     * @return 从(i,j)开始的最长递增路径长度
     * 
     * 算法核心：
     * 1. 边界检查：越界返回-1（无效路径）
     * 2. 记忆化检查：已计算过直接返回
     * 3. 四方向搜索：只有下一个位置值更大才能移动
     * 4. 状态更新：当前位置长度 = 1 + 四个方向的最大值
     */
    private static int process(int[][] matrix, int i, int j, int[][] dp) {
        // 边界检查：坐标越界返回-1
        if (i < 0 || i >= matrix.length || j < 0 || j >= matrix[0].length) {
            return -1;
        }
        
        // 记忆化检查：已经计算过的位置直接返回结果
        if (dp[i][j] != 0) {
            return dp[i][j];
        }
        
        // 初始化四个方向的路径长度
        int up = 0;    // 向上的最长路径
        int down = 0;  // 向下的最长路径
        int left = 0;  // 向左的最长路径
        int right = 0; // 向右的最长路径
        
        // 向上搜索：只有上方值更大时才能移动
        if (i - 1 >= 0 && matrix[i - 1][j] > matrix[i][j]) {
            up = process(matrix, i - 1, j, dp);
        }
        
        // 向下搜索：只有下方值更大时才能移动
        if (i + 1 < matrix.length && matrix[i + 1][j] > matrix[i][j]) {
            down = process(matrix, i + 1, j, dp);
        }
        
        // 向左搜索：只有左方值更大时才能移动
        if (j - 1 >= 0 && matrix[i][j - 1] > matrix[i][j]) {
            left = process(matrix, i, j - 1, dp);
        }
        
        // 向右搜索：只有右方值更大时才能移动
        if (j + 1 < matrix[0].length && matrix[i][j + 1] > matrix[i][j]) {
            right = process(matrix, i, j + 1, dp);
        }
        
        // 状态转移：当前位置的最长路径 = 1 + 四个方向的最大值
        dp[i][j] = 1 + Math.max(Math.max(up, down), Math.max(left, right));
        return dp[i][j];
    }

    /**
     * 计算矩阵中最长递增路径的长度
     * 
     * @param matrix 输入矩阵
     * @return 最长递增路径的长度
     * 
     * 算法步骤：
     * 1. 创建dp数组用于记忆化
     * 2. 遍历矩阵中的每个位置作为起点
     * 3. 计算每个起点的最长路径长度
     * 4. 返回所有起点中的最大值
     */
    public static int max(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        
        int ans = Integer.MIN_VALUE;
        // dp[i][j] 表示从位置(i,j)开始的最长递增路径长度
        int[][] dp = new int[matrix.length][matrix[0].length];
        
        // 遍历每个位置作为起点
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                // 更新全局最大值
                ans = Math.max(ans, process(matrix, row, col, dp));
            }
        }
        return ans;
    }
}
