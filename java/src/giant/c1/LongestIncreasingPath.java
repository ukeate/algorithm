package giant.c1;

/**
 * 矩阵中最长递增路径问题
 * 
 * 问题描述：
 * 给定一个二维矩阵，找到矩阵中最长的递增路径的长度。
 * 路径可以从任意一个单元格开始，并且每次只能向上下左右四个方向移动。
 * 路径上的数字必须是严格递增的。
 * 
 * 算法思路：
 * 这是一个经典的动态规划问题，使用记忆化搜索来解决。
 * 1. 对矩阵中的每个位置，计算从该位置开始的最长递增路径
 * 2. 使用dp数组作为缓存，避免重复计算
 * 3. 状态转移：dp[i][j] = max(dp[i-1][j], dp[i+1][j], dp[i][j-1], dp[i][j+1]) + 1
 *    其中转移条件是相邻单元格的值大于当前单元格的值
 * 
 * 时间复杂度：O(m*n)，每个单元格最多被计算一次
 * 空间复杂度：O(m*n)，用于存储dp数组
 */
public class LongestIncreasingPath {
    /**
     * 记忆化搜索：计算从位置(i,j)开始的最长递增路径长度
     * 
     * @param m 输入矩阵
     * @param i 当前行索引
     * @param j 当前列索引
     * @param dp 记忆化数组，dp[i][j]表示从(i,j)开始的最长递增路径长度
     * @return 从(i,j)开始的最长递增路径长度
     */
    private static int process(int[][] m, int i, int j, int[][] dp) {
        // 如果已经计算过，直接返回缓存结果
        if (dp[i][j] != 0) {
            return dp[i][j];
        }
        
        // 尝试向四个方向移动，只有当相邻单元格的值大于当前单元格时才能移动
        int up = i > 0 && m[i][j] < m[i - 1][j] ? process(m, i - 1, j, dp) : 0;                    // 向上
        int down = i < (m.length - 1) && m[i][j] < m[i + 1][j] ? process(m, i + 1, j, dp) : 0;     // 向下
        int left = j > 0 && m[i][j] < m[i][j - 1] ? process(m, i, j - 1, dp) : 0;                  // 向左
        int right = j < (m[0].length - 1) && m[i][j] < m[i][j + 1] ? process(m, i, j + 1, dp) : 0; // 向右
        
        // 当前位置的最长路径 = 四个方向中的最大值 + 1（当前位置本身）
        int ans = Math.max(Math.max(up, down), Math.max(left, right)) + 1;
        
        // 缓存结果并返回
        dp[i][j] = ans;
        return ans;
    }

    /**
     * 主函数：计算矩阵中的最长递增路径长度
     * 
     * @param matrix 输入矩阵
     * @return 最长递增路径的长度
     */
    public static int longest(int[][] matrix) {
        int ans = 0;
        int n = matrix.length;    // 矩阵行数
        int m = matrix[0].length; // 矩阵列数
        
        // 初始化dp数组，默认值为0表示没有计算过
        int[][] dp = new int[n][m];
        
        // 遍历矩阵中的每个位置，计算从该位置开始的最长递增路径
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // 更新全局最大值
                ans = Math.max(ans, process(matrix, i, j, dp));
            }
        }
        return ans;
    }

}
