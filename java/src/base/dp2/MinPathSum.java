package base.dp2;

/**
 * 最小路径和问题
 * 
 * 问题描述：
 * 给定一个二维数组matrix，表示一个矩阵，其中每个位置都有一个正整数。
 * 从左上角开始，每次只能向右或向下移动，直到到达右下角。
 * 路径上所有数字的和就是路径和，返回所有路径中的最小路径和。
 * 
 * 这是经典的动态规划问题，也是最基础的路径问题之一。
 * 
 * 解法分析：
 * 1. 状态定义：dp[i][j]表示从起点(0,0)到位置(i,j)的最小路径和
 * 2. 状态转移：dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + matrix[i][j]
 * 3. 边界条件：第一行只能从左边来，第一列只能从上边来
 * 4. 空间优化：可以使用一维数组优化空间复杂度
 * 
 * 时间复杂度：O(m*n)，其中m和n分别是矩阵的行数和列数
 * 空间复杂度：O(m*n) -> O(n) 空间优化后
 */
public class MinPathSum {
    
    /**
     * 动态规划解法（二维DP）
     * 
     * @param m 输入矩阵
     * @return 最小路径和
     */
    public static int minPathSum1(int[][] m) {
        if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) {
            return 0;
        }
        
        int row = m.length;
        int col = m[0].length;
        
        // dp[i][j]表示从(0,0)到(i,j)的最小路径和
        int[][] dp = new int[row][col];
        
        // 初始化起点
        dp[0][0] = m[0][0];
        
        // 初始化第一列：只能从上方来
        for (int i = 1; i < row; i++) {
            dp[i][0] = dp[i - 1][0] + m[i][0];
        }
        
        // 初始化第一行：只能从左边来
        for (int j = 1; j < col; j++) {
            dp[0][j] = dp[0][j - 1] + m[0][j];
        }
        
        // 填充DP表：每个位置可以从上方或左方来，选择较小的路径和
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + m[i][j];
            }
        }
        
        return dp[row - 1][col - 1];
    }

    /**
     * 空间优化的动态规划解法（一维DP）
     * 
     * 关键思路：
     * 由于每个位置只依赖于上方和左方的值，我们可以使用一维数组滚动更新。
     * dp[j]在更新前表示上一行第j列的值，dp[j-1]表示当前行第j-1列的值。
     * 
     * @param m 输入矩阵
     * @return 最小路径和
     */
    public static int minPathSum2(int[][] m) {
        if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) {
            return 0;
        }
        
        int row = m.length;
        int col = m[0].length;
        
        // dp[j]表示到达当前行第j列的最小路径和
        int[] dp = new int[col];
        
        // 初始化第一行
        dp[0] = m[0][0];
        for (int j = 1; j < col; j++) {
            dp[j] = dp[j - 1] + m[0][j];
        }
        
        // 逐行更新DP数组
        for (int i = 1; i < row; i++) {
            // 更新第一列：只能从上方来
            dp[0] += m[i][0];
            
            // 更新其他列：min(从上方来, 从左方来) + 当前值
            for (int j = 1; j < col; j++) {
                dp[j] = Math.min(dp[j - 1], dp[j]) + m[i][j];
            }
        }
        
        return dp[col - 1];
    }

    /**
     * 生成随机矩阵用于测试
     * 
     * @param rowSize 行数
     * @param colSize 列数
     * @return 随机矩阵
     */
    private static int[][] randomMatrix(int rowSize, int colSize) {
        if (rowSize < 0 || colSize < 0) {
            return null;
        }
        
        int[][] rst = new int[rowSize][colSize];
        for (int i = 0; i != rst.length; i++) {
            for (int j = 0; j != rst[0].length; j++) {
                rst[i][j] = (int) (Math.random() * 100);
            }
        }
        
        return rst;
    }

    public static void main(String[] args) {
        int[][] m = randomMatrix(10, 10);
        System.out.println(minPathSum1(m));
        System.out.println(minPathSum2(m));
    }
}
